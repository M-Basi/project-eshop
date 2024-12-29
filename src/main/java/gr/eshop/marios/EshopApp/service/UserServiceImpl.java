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


/**
 * Saves a new user to the database.
 * <p>
 * This method creates a new {@link User} based on the provided {@link UserInsertDTO}.
 * If a user with the same username already exists, an exception is thrown.
 * </p>
 *
 * @param dto the {@link UserInsertDTO} containing the user details.
 * @return a {@link UserReadOnlyDTO} representing the saved user.
 * @throws AppServerException if a server error occurs.
 * @throws AppObjectAlreadyExists if a user with the provided username already exists.
 * @throws AppObjectInvalidArgumentException if the provided input arguments are invalid.
 */
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

/**
 * Updates an existing user's information.
 * <p>
 * This method updates the username and password of a user identified by their UUID.
 * </p>
 *
 * @param dto the {@link UserUpdateDTO} containing the updated user details.
 * @return a {@link UserReadOnlyDTO} representing the updated user.
 * @throws AppServerException if a server error occurs.
 * @throws AppObjectNotFoundException if the user with the given UUID is not found.
 */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserReadOnlyDTO updateUser(UserUpdateDTO dto) throws AppServerException, AppObjectNotFoundException {

        User user = userRepository.findByUuid(dto.getUuid())
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User with uuid: " + dto.getUuid() + " not found"));
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);

        return mapper.mapToUserReadOnlyDTO(user);
    }

/**
 * Deletes a user by their UUID.
 * <p>
 * This method removes the user entity identified by their UUID from the database.
 * </p>
 *
 * @param uuid the UUID of the user to delete.
 * @return a {@link UserReadOnlyDTO} representing the deleted user.
 * @throws AppServerException if a server error occurs.
 * @throws AppObjectNotFoundException if the user with the given UUID is not found.
 */
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

    /**
     * Retrieves a filtered list of users.
     * <p>
     * This method applies the provided {@link UserFilter} criteria to fetch a list of users
     * that match the filters. The results are mapped to {@link UserReadOnlyDTO}.
     * </p>
     *
     * @param filters the {@link UserFilter} containing the criteria for filtering users.
     * @return a {@link List} of {@link UserReadOnlyDTO} representing the filtered users.
     */
    @Override
    public Paginated<UserReadOnlyDTO> getUsersFilteredPaginated(UserFilter filters) {
        var filtered = userRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        return new Paginated<>(filtered.map(mapper::mapToUserReadOnlyDTO));
    }

    /**
     * Retrieves paginated users with default sorting.
     * <p>
     * This method returns a paginated list of users sorted by their ID in ascending order.
     * </p>
     *
     * @param page the page number to retrieve.
     * @param size the number of users per page.
     * @return a {@link Page} of {@link UserReadOnlyDTO}.
     */
    @Override
    public Page<UserReadOnlyDTO> getPaginatedUsers(int page, int size) {
        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());
        return userRepository.findAll(pageable).map(mapper::mapToUserReadOnlyDTO);
    }

    /**
     * Retrieves paginated users with custom sorting.
     * <p>
     * This method returns a paginated list of users sorted by the specified field and direction.
     * </p>
     *
     * @param page the page number to retrieve.
     * @param size the number of users per page.
     * @param sortBy the field to sort by.
     * @param sortDirection the direction of sorting ("ASC" or "DESC").
     * @return a {@link Page} of {@link UserReadOnlyDTO}.
     */
    @Override
    public Page<UserReadOnlyDTO> getPaginatedUser(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable).map(mapper::mapToUserReadOnlyDTO);
    }


    /**
     * Retrieves a filtered list of users.
     * <p>
     * This method applies the provided {@link UserFilter} criteria to fetch a list of users
     * that match the filters. The results are mapped to {@link UserReadOnlyDTO}.
     * </p>
     *
     * @param filters the {@link UserFilter} containing the criteria for filtering users.
     * @return a {@link List} of {@link UserReadOnlyDTO} representing the filtered users.
     */
    @Override
    public List<UserReadOnlyDTO> getUsersFiltered(UserFilter filters) {
        return userRepository.findAll(getSpecsFromFilters(filters))
                .stream().map(mapper::mapToUserReadOnlyDTO).toList();
    }

/**
 * Retrieves a user by their UUID.
 * <p>
 * This method fetches a user identified by their UUID and maps it to a {@link UserReadOnlyDTO}.
 * </p>
 *
 * @param uuid the UUID of the user to retrieve.
 * @return a {@link UserReadOnlyDTO} representing the user.
 * @throws AppObjectNotFoundException if the user with the given UUID is not found.
 */
    @Override
    public UserReadOnlyDTO getUserByUuid(String uuid) throws AppObjectNotFoundException {
        return mapper.mapToUserReadOnlyDTO(userRepository.findByUuid(uuid)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User with uuid: " + uuid + " not found")));
    }

/**
 * Builds a dynamic query specification based on provided filters.
 * <p>
 * This method constructs a {@link Specification} for querying users using the criteria provided
 * in the {@link UserFilter}.
 * </p>
 *
 * @param filters the {@link UserFilter} containing the filtering criteria.
 * @return a {@link Specification} for querying users.
 */
    private Specification<User> getSpecsFromFilters(UserFilter filters) {
        return Specification
                .where(UserSpecification.trStringFieldLike("uuid", filters.getUuid()))
                .and(UserSpecification.trStringFieldLike("id",filters.getId()))
                .and(UserSpecification.trStringFieldLike("username", filters.getUsername()))
                .and(UserSpecification.trUsersIsActive(filters.getIsActive()));


    }


}
