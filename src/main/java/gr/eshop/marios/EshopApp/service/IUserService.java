package gr.eshop.marios.EshopApp.service;

import gr.eshop.marios.EshopApp.core.exceptions.AppObjectAlreadyExists;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectInvalidArgumentException;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectNotFoundException;
import gr.eshop.marios.EshopApp.core.exceptions.AppServerException;
import gr.eshop.marios.EshopApp.core.filters.Paginated;
import gr.eshop.marios.EshopApp.core.filters.UserFilter;
import gr.eshop.marios.EshopApp.dto.UserInsertDTO;
import gr.eshop.marios.EshopApp.dto.UserReadOnlyDTO;
import gr.eshop.marios.EshopApp.dto.UserUpdateDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserService {

    UserReadOnlyDTO saveUser(UserInsertDTO dto) throws AppServerException, AppObjectAlreadyExists, AppObjectInvalidArgumentException;
    UserReadOnlyDTO updateUser(UserUpdateDTO dto) throws AppServerException, AppObjectNotFoundException;
    UserReadOnlyDTO deleteUserByUuid(String uuid) throws AppServerException, AppObjectNotFoundException;
    Paginated<UserReadOnlyDTO> getUsersFilteredPaginated(UserFilter filters);
    Page<UserReadOnlyDTO> getPaginatedUsers(int page, int size) ;
    Page<UserReadOnlyDTO> getPaginatedUser(int page, int size, String sortBy, String sortDirection);
    List<UserReadOnlyDTO> getUsersFiltered(UserFilter filters);
    UserReadOnlyDTO getUserByUuid(String uuid) throws AppObjectNotFoundException;

}
