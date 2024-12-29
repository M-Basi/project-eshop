package gr.eshop.marios.EshopApp.service;

import gr.eshop.marios.EshopApp.core.exceptions.AppObjectAlreadyExists;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectInvalidArgumentException;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectNotFoundException;
import gr.eshop.marios.EshopApp.core.exceptions.AppServerException;
import gr.eshop.marios.EshopApp.core.filters.Paginated;
import gr.eshop.marios.EshopApp.core.filters.UserFilter;
import gr.eshop.marios.EshopApp.core.specifications.UserSpecification;
import gr.eshop.marios.EshopApp.dto.UserInsertDTO;
import gr.eshop.marios.EshopApp.dto.UserReadOnlyDTO;
import gr.eshop.marios.EshopApp.dto.UserUpdateDTO;
import gr.eshop.marios.EshopApp.mapper.Mapper;
import gr.eshop.marios.EshopApp.model.User;
import gr.eshop.marios.EshopApp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserReadOnlyDTO saveUser(UserInsertDTO dto) throws AppServerException, AppObjectAlreadyExists, AppObjectInvalidArgumentException {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new AppObjectAlreadyExists("User", "User with username: " + dto.getUsername() + " already exists");
        }

        User user = mapper.mapToUser(dto);
        User savedUser = userRepository.save(user);
        return mapper.mapToUserReadOnlyDTO(savedUser);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserReadOnlyDTO updateUser(UserUpdateDTO dto) throws AppServerException, AppObjectNotFoundException {
//        if (userRepository.findById(dto.getId()).isEmpty()) {
//            throw new AppObjectNotFoundException("User", "User with ID: " + dto.getId() + " not found");
//        }
//        if (userRepository.findByUuid(dto.getUuid()).isEmpty()) {
//            throw new AppObjectNotFoundException("User", "User with uuid: " + dto.getUuid() + " not found");
//        }
        User user = userRepository.findByUuid(dto.getUuid())
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User with uuid: " + dto.getUuid() + " not found"));
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);

        return mapper.mapToUserReadOnlyDTO(user);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserReadOnlyDTO deleteUserByUuid(String uuid) throws AppServerException, AppObjectNotFoundException {
        if (userRepository.findByUuid(uuid).isEmpty()) {
            throw new AppObjectNotFoundException("User", "User with: " + uuid + " not found");
        }
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User with uuid" + uuid + "not found"));
        UserReadOnlyDTO dto = mapper.mapToUserReadOnlyDTO(user);
        userRepository.delete(user);
        return dto;
    }

    @Override
    public Paginated<UserReadOnlyDTO> getUsersFilteredPaginated(UserFilter filters) {
        var filtered = userRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        return new Paginated<>(filtered.map(mapper::mapToUserReadOnlyDTO));
    }

    @Override
    public Page<UserReadOnlyDTO> getPaginatedUsers(int page, int size) {
        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());
        return userRepository.findAll(pageable).map(mapper::mapToUserReadOnlyDTO);
    }

    @Override
    public Page<UserReadOnlyDTO> getPaginatedUser(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable).map(mapper::mapToUserReadOnlyDTO);
    }


    @Override
    public List<UserReadOnlyDTO> getUsersFiltered(UserFilter filters) {
        return userRepository.findAll(getSpecsFromFilters(filters))
                .stream().map(mapper::mapToUserReadOnlyDTO).toList();
    }

    @Override
    public UserReadOnlyDTO getUserByUuid(String uuid) throws AppObjectNotFoundException {
        return mapper.mapToUserReadOnlyDTO(userRepository.findByUuid(uuid)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User with uuid: " + uuid + " not found")));
    }


    private Specification<User> getSpecsFromFilters(UserFilter filters) {
        return Specification
                .where(UserSpecification.trStringFieldLike("uuid", filters.getUuid()))
                .and(UserSpecification.trStringFieldLike("id",filters.getId()))
                .and(UserSpecification.trStringFieldLike("username", filters.getUsername()))
                .and(UserSpecification.trUsersIsActive(filters.getIsActive()));


    }


}
