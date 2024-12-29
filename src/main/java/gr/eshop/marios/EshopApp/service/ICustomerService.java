package gr.eshop.marios.EshopApp.service;

import gr.eshop.marios.EshopApp.core.exceptions.AppObjectAlreadyExists;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectInvalidArgumentException;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectNotFoundException;
import gr.eshop.marios.EshopApp.core.exceptions.AppServerException;
import gr.eshop.marios.EshopApp.core.filters.CustomerFilters;
import gr.eshop.marios.EshopApp.core.filters.Paginated;
import gr.eshop.marios.EshopApp.dto.*;
import org.springframework.data.domain.Page;


public interface ICustomerService {

    CustomerReadOnlyDTO saveCustomer(CustomerInsertDTO dto) throws AppServerException, AppObjectAlreadyExists, AppObjectNotFoundException, AppObjectInvalidArgumentException;
    CustomerReadOnlyDTO updateCustomer(CustomerUpdateDTO dto) throws AppServerException, AppObjectNotFoundException;
    CustomerReadOnlyDTO deleteCustomer(String uuid) throws AppServerException, AppObjectNotFoundException;
    Paginated<CustomerReadOnlyDTO> getCustomersFilteredPaginated(CustomerFilters filters);
    Page<CustomerReadOnlyDTO> getPaginatedCustomers(int page, int size) ;
    Page<CustomerReadOnlyDTO> getPaginatedCustomers(int page, int size, String sortBy, String sortDirection);
    CustomerReadOnlyDTO getCustomerByUserUuid(String uuid) throws AppObjectNotFoundException;
    CustomerReadOnlyDTO getCustomerByUuid(String uuid) throws AppObjectNotFoundException;
    PaymentInfoReadOnlyDTO getPaymentInfoByCustomerUuid(String customerUuid) throws AppObjectNotFoundException;
    CustomerInfoReadOnlyDTO getCustomerInfoByCustomerUuid(String customerUuid) throws AppObjectNotFoundException;
    CustomerInfoReadOnlyDTO saveCustomerInfo(CustomerInfoInsertDTO dto)
            throws AppServerException, AppObjectAlreadyExists, AppObjectNotFoundException, AppObjectInvalidArgumentException;
    CustomerInfoReadOnlyDTO updateCustomerInfo(CustomerInfoUpdateDTO dto)
            throws AppServerException, AppObjectAlreadyExists, AppObjectNotFoundException, AppObjectInvalidArgumentException;
    CustomerInfoReadOnlyDTO getCustomerInfoById(Long id) throws AppObjectNotFoundException;
    PaymentInfoReadOnlyDTO getPaymentInfoById(Long id) throws AppObjectNotFoundException;
    CustomerInfoReadOnlyDTO deleteCustomerInfo(Long id)
            throws AppServerException, AppObjectNotFoundException;
    CustomerInfoReadOnlyDTO deleteCustomerInfoByCustomerUuid(String customerUuid)
            throws AppServerException, AppObjectNotFoundException;
    PaymentInfoReadOnlyDTO savePaymentInfo(PaymentInfoInsertDTO dto)
            throws AppServerException, AppObjectAlreadyExists, AppObjectNotFoundException, AppObjectInvalidArgumentException;
    PaymentInfoReadOnlyDTO updatePaymentInfo(PaymentInfoUpdateDTO dto)
            throws AppServerException, AppObjectAlreadyExists, AppObjectNotFoundException, AppObjectInvalidArgumentException;
    PaymentInfoReadOnlyDTO deletePaymentInfo(Long id)
            throws AppServerException, AppObjectNotFoundException;
    PaymentInfoReadOnlyDTO deletePaymentInfoByCustomerUuid(String customerUuid)
            throws AppServerException, AppObjectNotFoundException;
}
