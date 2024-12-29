package gr.eshop.marios.EshopApp.service;


import gr.eshop.marios.EshopApp.core.exceptions.AppObjectAlreadyExists;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectInvalidArgumentException;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectNotFoundException;
import gr.eshop.marios.EshopApp.core.exceptions.AppServerException;
import gr.eshop.marios.EshopApp.core.filters.CustomerFilters;
import gr.eshop.marios.EshopApp.core.filters.Paginated;
import gr.eshop.marios.EshopApp.core.specifications.CustomerSpecification;
import gr.eshop.marios.EshopApp.dto.*;
import gr.eshop.marios.EshopApp.mapper.Mapper;
import gr.eshop.marios.EshopApp.model.Customer;
import gr.eshop.marios.EshopApp.model.CustomerInfo;
import gr.eshop.marios.EshopApp.model.PaymentInfo;
import gr.eshop.marios.EshopApp.model.static_data.Region;
import gr.eshop.marios.EshopApp.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final PaymentInfoRepository paymentInfoRepository;
    private final CustomerInfoRepository customerInfoRepository;
    private final Mapper mapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;


    /**
     * Saves a new customer in the system.
     *
     * @param dto the {@link CustomerInsertDTO} containing customer data.
     * @return a {@link CustomerReadOnlyDTO} with the saved customer's details.
     * @throws AppServerException if a server error occurs.
     * @throws AppObjectAlreadyExists if the customer already exists.
     * @throws AppObjectNotFoundException if the referenced user is not found.
     * @throws AppObjectInvalidArgumentException if input arguments are invalid.
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public CustomerReadOnlyDTO saveCustomer(CustomerInsertDTO dto)
            throws AppServerException, AppObjectAlreadyExists, AppObjectNotFoundException, AppObjectInvalidArgumentException{

        if (userRepository.findByUuid(dto.getUserUuid()).isEmpty()) {
            LOGGER.error("User with uuid {} not found", dto.getUserUuid());
            throw new AppObjectNotFoundException("User", "User with uuid: " + dto.getUserUuid() + " not found");
        }
        LOGGER.info("User with uuid {} found", userRepository.findByUuid(dto.getUserUuid()).get().getUsername());
        if (customerRepository.findByUserUuid(dto.getUserUuid()).isPresent()) {
            LOGGER.error("User with uuid {} has already a customer account", dto.getUserUuid());
            throw new AppObjectAlreadyExists("User", "User with uuid: " + dto.getUserUuid() + " has already a customer account");
        }
        LOGGER.info("User with uuid {} has already a customer account", dto.getUserUuid());
        Customer customer = mapper.mapToCustomer(dto);
        LOGGER.info("Entering saveCustomer with customer: {}", customer);

        Customer savedCustomer = customerRepository.save(customer);
        LOGGER.info("Entering saveCustomer with customer: {}", customer);
        CustomerReadOnlyDTO customerReadOnlyDTO = mapper.mapToCustomerReadOnlyDTO(savedCustomer);
        LOGGER.info("Customer saved successfully with Uuid: {}", customerReadOnlyDTO.getUuid());
        return customerReadOnlyDTO;

    }

    /**
     * Updates an existing customer.
     *
     * @param dto the {@link CustomerUpdateDTO} with updated customer details.
     * @return a {@link CustomerReadOnlyDTO} with the updated customer's details.
     * @throws AppServerException if a server error occurs.
     * @throws AppObjectNotFoundException if the customer is not found.
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public CustomerReadOnlyDTO updateCustomer(CustomerUpdateDTO dto)
            throws AppServerException, AppObjectNotFoundException {
        Customer customer = customerRepository.findByUuid(dto.getUuid())
                .orElseThrow(() -> new AppObjectNotFoundException("Customer", "Customer with Uuid: " + dto.getUuid() +
                        " not found"));

        customer.setFirstname(dto.getFirstname());
        customer.setLastname(dto.getLastname());
        Customer updatedCustomer = customerRepository.save(customer);
        return mapper.mapToCustomerReadOnlyDTO(updatedCustomer);
    }

    /**
     * Deletes a customer by UUID.
     *
     * @param uuid the UUID of the customer to delete.
     * @return a {@link CustomerReadOnlyDTO} with the deleted customer's details.
     * @throws AppServerException if a server error occurs.
     * @throws AppObjectNotFoundException if the customer is not found.
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public CustomerReadOnlyDTO deleteCustomer(String uuid)
            throws AppServerException, AppObjectNotFoundException {
        Customer customer = customerRepository.findByUuid(uuid)
                .orElseThrow(() -> new AppObjectNotFoundException("Customer", "Customer with Uuid: " + uuid + " not found"));

        CustomerReadOnlyDTO dto = mapper.mapToCustomerReadOnlyDTO(customer);
        customerRepository.delete(customer);
        LOGGER.info("Customer deleted successfully with UUID: {}", dto.getUuid());
        return dto;
    }


    @Override
    public Paginated<CustomerReadOnlyDTO> getCustomersFilteredPaginated(CustomerFilters filters) {
        var filtered = customerRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        return new Paginated<>(filtered.map(mapper::mapToCustomerReadOnlyDTO));
    }

    /**
     * Retrieves paginated customers.
     *
     * @param page the page number to retrieve.
     * @param size the number of records per page.
     * @return a {@link Page} of {@link CustomerReadOnlyDTO}.
     */
    @Override
    public Page<CustomerReadOnlyDTO> getPaginatedCustomers(int page, int size) {
        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());
        return customerRepository.findAll(pageable).map(mapper::mapToCustomerReadOnlyDTO);
    }

    @Override
    public Page<CustomerReadOnlyDTO> getPaginatedCustomers(int page, int size, String sortBy,
                                                           String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return customerRepository.findAll(pageable).map(mapper::mapToCustomerReadOnlyDTO);
    }


    @org.springframework.transaction.annotation.Transactional
    public List<CustomerReadOnlyDTO> getCustomerFiltered(CustomerFilters filters) {
        return customerRepository.findAll(getSpecsFromFilters(filters))
                .stream().map(mapper::mapToCustomerReadOnlyDTO).toList();
    }



    @Override
    public CustomerReadOnlyDTO getCustomerByUserUuid(String uuid) throws AppObjectNotFoundException {

        Customer customer = customerRepository.findByUserUuid(uuid)
                .orElseThrow(() -> new AppObjectNotFoundException("Customer", "Customer for User with Uuid: " + uuid + " not found"));
        LOGGER.info("Customer  {} was found", mapper.mapToCustomerReadOnlyDTO(customer));
        return mapper.mapToCustomerReadOnlyDTO(customer);
    }

    /**
     * Retrieves customer information by UUID.
     *
     * @param uuid the UUID of the customer.
     * @return a {@link CustomerReadOnlyDTO} with the customer's details.
     * @throws AppObjectNotFoundException if the customer is not found.
     */
    @Override
    public CustomerReadOnlyDTO getCustomerByUuid(String uuid) throws AppObjectNotFoundException {

        Customer customer = customerRepository.findByUuid(uuid)
                .orElseThrow(() -> new AppObjectNotFoundException("Customer", "Customer with Uuid: " + uuid + " not found"));
        LOGGER.info("Customer  {} was found", customer);

        return mapper.mapToCustomerReadOnlyDTO(customer);
    }

    /**
     * Saves customer information for a specific customer.
     * <p>
     * This method creates a new {@link CustomerInfo} record in the database using the details
     * provided in the {@link CustomerInfoInsertDTO}. It validates that the customer exists and
     * that there is no existing customer information associated with the given customer UUID.
     * The operation is transactional and will roll back in case of an exception.
     * </p>
     *
     * @param dto the {@link CustomerInfoInsertDTO} containing the details of the customer information to be saved.
     * @return a {@link CustomerInfoReadOnlyDTO} containing the details of the saved customer information.
     * @throws AppServerException if a server error occurs during the operation.
     * @throws AppObjectAlreadyExists if customer information for the given customer UUID already exists.
     * @throws AppObjectNotFoundException if the customer with the provided UUID is not found.
     * @throws AppObjectInvalidArgumentException if input arguments are invalid (not used in the current logic).
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public CustomerInfoReadOnlyDTO saveCustomerInfo(CustomerInfoInsertDTO dto)
            throws AppServerException, AppObjectAlreadyExists, AppObjectNotFoundException, AppObjectInvalidArgumentException {
        LOGGER.info("Entering method saveCustomerInfo with DTO: {}", dto);

// Αρχή
        LOGGER.info("Step 1: Check if Customer exists");
        try {
            LOGGER.info("Entering saveCustomerInfo with DTO: {}", dto);

            Customer customer = customerRepository.findByUuid(dto.getCustomerUuid())
                    .orElseThrow(() -> {
                        String errorMessage = "Customer with Uuid: " + dto.getCustomerUuid() + " not found";
                        LOGGER.error(errorMessage);
                        return new AppObjectNotFoundException("Customer", errorMessage);
                    });

            if (customerInfoRepository.findByCustomerUuid(dto.getCustomerUuid()).isPresent()) {
                String errorMessage = "CustomerInfo for Customer with Uuid: " + dto.getCustomerUuid() + " already exists";
                LOGGER.error(errorMessage);
                throw new AppObjectAlreadyExists("CustomerInfo", errorMessage);
            }

            CustomerInfo customerInfo = mapper.mapToCustomerInfo(dto);
            LOGGER.info("Entering saveCustomerInfo with DTO: {}", customerInfo);

            CustomerInfo savedCustomerInfo = customerInfoRepository.save(customerInfo);
            LOGGER.info("CustomerInfo saved successfully: {}", savedCustomerInfo);

            return mapper.mapToCustomerInfoReadOnlyDTO(savedCustomerInfo);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while saving CustomerInfo", e);
            throw e; // Επιτρέπει την αναφορά του σφάλματος στον client.
        }
    }

    /**
     * Updates the customer information with the provided details.
     * <p>
     * This method updates an existing {@link CustomerInfo} record in the database using
     * the data provided in the {@link CustomerInfoUpdateDTO}. The operation is transactional
     * and will roll back in case of an exception.
     * </p>
     *
     * @param dto the {@link CustomerInfoUpdateDTO} containing the updated customer information.
     * @return a {@link CustomerInfoReadOnlyDTO} containing the details of the updated customer information.
     * @throws AppServerException if a server error occurs during the operation.
     * @throws AppObjectAlreadyExists if there is a conflict with existing records (not used in the current logic).
     * @throws AppObjectNotFoundException if the customer information or the specified region is not found.
     * @throws AppObjectInvalidArgumentException if input arguments are invalid (not used in the current logic).
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public CustomerInfoReadOnlyDTO updateCustomerInfo(CustomerInfoUpdateDTO dto)
            throws AppServerException, AppObjectAlreadyExists, AppObjectNotFoundException, AppObjectInvalidArgumentException{
        CustomerInfo customerInfo = customerInfoRepository.findById(dto.getId())
                .orElseThrow(() -> new AppObjectNotFoundException("CustomerInfo", "CustomerInfo with id: "
                        + dto.getId() + " not found"));
        customerInfo.setPhoneNumber(dto.getPhoneNumber());
        customerInfo.setCity(dto.getCity());
        customerInfo.setCountry(dto.getCountry());
        Region region= regionRepository.findByName(dto.getRegion())
                .orElseThrow(() -> new RuntimeException("Region with name: "
                        + dto.getRegion() + " not found"));
        customerInfo.setRegion(region);
        customerInfo.setZipCode(dto.getZipCode());
        customerInfo.setStreetNumber(dto.getStreetNumber());
        customerInfo.setStreet(dto.getStreet());
        CustomerInfo updatedCustomerInfo = customerInfoRepository.save(customerInfo);
        return mapper.mapToCustomerInfoReadOnlyDTO(updatedCustomerInfo);

    }

    /**
     * Deletes customer information by its ID.
     * <p>
     * This method removes the {@link CustomerInfo} record identified by its ID from the database.
     * It also clears the association between the customer and the customer information.
     * The operation is transactional and will roll back in case of an exception.
     * </p>
     *
     * @param id the ID of the customer information to delete.
     * @return a {@link CustomerInfoReadOnlyDTO} containing the details of the deleted customer information.
     * @throws AppServerException if a server error occurs during the operation.
     * @throws AppObjectNotFoundException if the customer information or the associated customer is not found.
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public CustomerInfoReadOnlyDTO deleteCustomerInfo(Long id)
            throws AppServerException, AppObjectNotFoundException {
        CustomerInfo customerInfo = customerInfoRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("CustomerInfo", "CustomerInfo with id: " + id + " not found"));
        CustomerInfoReadOnlyDTO dto = mapper.mapToCustomerInfoReadOnlyDTO(customerInfo);
        Customer customer = customerRepository.findByCustomerInfoId(id)
                .orElseThrow(()-> new AppObjectNotFoundException("Customer", "Customer not found"));
        customer.setCustomerInfo(null);
        customerRepository.save(customer);

        customerInfoRepository.delete(customerInfo);
        return dto;
    }

    /**
     * Deletes customer information associated with a customer's UUID.
     * <p>
     * This method removes the {@link CustomerInfo} record linked to a customer identified by their UUID.
     * Additionally, it removes the reference to the customer information from the customer entity.
     * The operation is transactional and will roll back in case of an exception.
     * </p>
     *
     * @param customerUuid the UUID of the customer whose information is to be deleted.
     * @return a {@link CustomerInfoReadOnlyDTO} containing the details of the deleted customer information.
     * @throws AppServerException if a server error occurs during the operation.
     * @throws AppObjectNotFoundException if the customer or their information is not found.
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public CustomerInfoReadOnlyDTO deleteCustomerInfoByCustomerUuid(String customerUuid)
            throws AppServerException, AppObjectNotFoundException {

        Customer customer = customerRepository.findByUuid(customerUuid)
                .orElseThrow(()-> new AppObjectNotFoundException("Customer","Customer with uuid" + customerUuid +" not found"));
        CustomerInfo customerInfo = customerInfoRepository.findByCustomerUuid(customerUuid)
                .orElseThrow(() -> new AppObjectNotFoundException("CustomerInfo", "CustomerInfo with Uuid: " + customerUuid + " not found"));
        CustomerInfoReadOnlyDTO dto = mapper.mapToCustomerInfoReadOnlyDTO(customerInfo);
        customer.setCustomerInfo(null);
        customerRepository.save(customer);
        customerInfoRepository.delete(customerInfo);
        return dto;
    }


    /**
     * Retrieves customer information using the customer's UUID.
     * <p>
     * This method fetches the {@link CustomerInfo} associated with a customer identified by their UUID.
     * If the customer information is not found, an {@link AppObjectNotFoundException} is thrown.
     * The found customer information is mapped to a {@link CustomerInfoReadOnlyDTO} for the response.
     * </p>
     *
     * @param customerUuid the UUID of the customer whose information is to be retrieved.
     * @return a {@link CustomerInfoReadOnlyDTO} containing the details of the customer's information.
     * @throws AppObjectNotFoundException if the customer information with the provided UUID is not found.
     */
    @Override
    public CustomerInfoReadOnlyDTO getCustomerInfoByCustomerUuid(String customerUuid) throws AppObjectNotFoundException {
        LOGGER.info("Entering getCustomerInfoByCustomerUuid");
        CustomerInfo customerInfo = customerInfoRepository.findByCustomerUuid(customerUuid)
                .orElseThrow(() -> new  AppObjectNotFoundException("Customer", "Customer Info for customer " +
                        "with Uuid: " + customerUuid + " not found"));
        return mapper.mapToCustomerInfoReadOnlyDTO(customerInfo);

    }

    /**
     * Retrieves customer information by its ID.
     * <p>
     * This method fetches a {@link CustomerInfo} record from the database using the provided ID.
     * If no record is found, an {@link AppObjectNotFoundException} is thrown.
     * The found customer information is mapped to a {@link CustomerInfoReadOnlyDTO} for the response.
     * </p>
     *
     * @param id the ID of the customer information to retrieve.
     * @return a {@link CustomerInfoReadOnlyDTO} containing the details of the customer information.
     * @throws AppObjectNotFoundException if the customer information with the given ID is not found.
     */
    @Override
    public CustomerInfoReadOnlyDTO getCustomerInfoById(Long id) throws AppObjectNotFoundException {

        CustomerInfo customerInfo = customerInfoRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("CustomerInfo", "CustomerInfo with id: "
                        + id + " not found"));

        return mapper.mapToCustomerInfoReadOnlyDTO(customerInfo);
    }

    /**
     * Retrieves payment information for a customer using their UUID.
     * <p>
     * This method fetches the {@link PaymentInfo} associated with a customer identified by their UUID.
     * If the customer or the payment information is not found, an {@link AppObjectNotFoundException} is thrown.
     * The found payment information is mapped to a {@link PaymentInfoReadOnlyDTO} for the response.
     * </p>
     *
     * @param customerUuid the UUID of the customer whose payment information is to be retrieved.
     * @return a {@link PaymentInfoReadOnlyDTO} containing the payment details for the customer.
     * @throws AppObjectNotFoundException if the customer or the payment information is not found.
     */
    @Override
    public PaymentInfoReadOnlyDTO getPaymentInfoByCustomerUuid(String customerUuid) throws AppObjectNotFoundException {
        if (customerRepository.findByUuid(customerUuid).isEmpty()) {
            throw new AppObjectNotFoundException("Customer", "Customer with Uuid: "
                    + customerUuid + " not found");
        }
        PaymentInfo paymentInfo = paymentInfoRepository.findByCustomerUuid(customerUuid)
                .orElseThrow(() -> new  AppObjectNotFoundException("Customer", "Customer Info for customer " +
                        "with Uuid: " + customerUuid + " not found"));
        return mapper.mapToPaymentInfoReadOnlyDTO(paymentInfo);
    }

    /**
     * Retrieves payment information by its ID.
     * <p>
     * This method fetches a {@link PaymentInfo} record from the database using the provided ID
     * and maps it to a {@link PaymentInfoReadOnlyDTO} for the response. If no record is found,
     * an {@link AppObjectNotFoundException} is thrown.
     * </p>
     *
     * @param id the ID of the payment information to retrieve.
     * @return a {@link PaymentInfoReadOnlyDTO} containing the details of the payment information.
     * @throws AppObjectNotFoundException if the payment information with the given ID is not found.
     */
    @Override
    public PaymentInfoReadOnlyDTO getPaymentInfoById(Long id) throws AppObjectNotFoundException {
        PaymentInfo paymentInfo = paymentInfoRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("PaymentInfo", "PaymentInfo with id: "
                        + id + " not found"));

        return mapper.mapToPaymentInfoReadOnlyDTO(paymentInfo);
    }

    /**
     * Saves payment information for a customer.
     *
     * @param dto the {@link PaymentInfoInsertDTO} with payment details.
     * @return a {@link PaymentInfoReadOnlyDTO} with the saved payment details.
     * @throws AppServerException if a server error occurs.
     * @throws AppObjectAlreadyExists if payment information already exists.
     * @throws AppObjectNotFoundException if the customer is not found.
     * @throws AppObjectInvalidArgumentException if input arguments are invalid.
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public PaymentInfoReadOnlyDTO savePaymentInfo(PaymentInfoInsertDTO dto)
            throws AppServerException, AppObjectAlreadyExists, AppObjectNotFoundException, AppObjectInvalidArgumentException{
        if (customerRepository.findByUuid(dto.getCustomerUuid()).isEmpty()) {
            throw new AppObjectNotFoundException("Customer", "Customer with Uuid: "
                    + dto.getCustomerUuid() + " not found");
        }
        if (paymentInfoRepository.findByCustomerUuid(dto.getCustomerUuid()).isPresent()) {
            throw new AppObjectAlreadyExists("PaymentInfo", "PaymentInfo for customer with uuid: "
                    + dto.getCustomerUuid() + " already exists");
        }

        PaymentInfo paymentInfo = mapper.mapToPaymentInfo(dto);

        PaymentInfo savedPaymentInfo = paymentInfoRepository.save(paymentInfo);
        return mapper.mapToPaymentInfoReadOnlyDTO(savedPaymentInfo);

    }

    /**
     * Updates the payment information record with the provided details.
     * <p>
     * This method updates an existing payment information record in the database using
     * the data from the provided {@link PaymentInfoUpdateDTO}. The operation is transactional
     * and will roll back in case of an exception.
     * </p>
     *
     * @param dto the {@link PaymentInfoUpdateDTO} containing the updated payment details.
     * @return a {@link PaymentInfoReadOnlyDTO} containing the details of the updated payment information.
     * @throws AppServerException if a server error occurs during the operation.
     * @throws AppObjectAlreadyExists if a conflicting payment information already exists (not used in the current logic).
     * @throws AppObjectNotFoundException if the payment information with the provided ID is not found.
     * @throws AppObjectInvalidArgumentException if the provided input arguments are invalid (not used in the current logic).
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public PaymentInfoReadOnlyDTO updatePaymentInfo(PaymentInfoUpdateDTO dto)
            throws AppServerException, AppObjectAlreadyExists, AppObjectNotFoundException, AppObjectInvalidArgumentException{
        PaymentInfo paymentInfo = paymentInfoRepository.findById(dto.getId())
                .orElseThrow(() -> new AppObjectNotFoundException("PaymentInfo", "PaymentInfo for customer with id: "
                        + dto.getId() + " not found"));
        paymentInfo.setCardName(dto.getCardName());
        paymentInfo.setCard(dto.getCard());
        paymentInfo.setExpiredDate(dto.getExpiredDate());
        paymentInfo.setCardValidation(dto.getCardValidation());
        PaymentInfo updatedPaymentInfo = paymentInfoRepository.save(paymentInfo);
        return mapper.mapToPaymentInfoReadOnlyDTO(updatedPaymentInfo);

    }


    /**
     * Deletes a payment information record by its ID.
     * <p>
     * This method removes the payment information associated with the provided ID from the database.
     * If the payment information is linked to a customer, the customer's payment information reference
     * is also cleared. The operation is transactional and will roll back in case of an exception.
     * </p>
     *
     * @param id the ID of the payment information to delete.
     * @return a {@link PaymentInfoReadOnlyDTO} containing the details of the deleted payment information.
     * @throws AppServerException if a server error occurs during the operation.
     * @throws AppObjectNotFoundException if the payment information or associated customer is not found.
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public PaymentInfoReadOnlyDTO deletePaymentInfo(Long id)
            throws AppServerException, AppObjectNotFoundException {

        PaymentInfo paymentInfo = paymentInfoRepository.findById(id)
                .orElseThrow(()-> new AppObjectNotFoundException("PaymentInfo", "Payment info with id: " +
                        id + " not found"));
        PaymentInfoReadOnlyDTO dto = mapper.mapToPaymentInfoReadOnlyDTO(paymentInfo);
        Customer customer = customerRepository.findByPaymentInfoId(id)
                        .orElseThrow(()-> new AppObjectNotFoundException("Customer", "Customer not found"));
        customer.setPaymentInfo(null);
        customerRepository.save(customer);
        paymentInfoRepository.delete(paymentInfo);
        return dto;
    }

    /**
     * Deletes payment information by customer UUID.
     *
     * @param customerUuid the UUID of the customer whose payment info will be deleted.
     * @return a {@link PaymentInfoReadOnlyDTO} with the deleted payment details.
     * @throws AppServerException if a server error occurs.
     * @throws AppObjectNotFoundException if the payment info is not found.
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public PaymentInfoReadOnlyDTO deletePaymentInfoByCustomerUuid(String customerUuid)
            throws AppServerException, AppObjectNotFoundException {

        Customer customer = customerRepository.findByUuid(customerUuid)
                .orElseThrow(()-> new AppObjectNotFoundException("Customer","Customer with uuid" + customerUuid +" not found"));

        PaymentInfo paymentInfo = paymentInfoRepository.findByCustomerUuid(customerUuid)
                .orElseThrow(()-> new AppObjectNotFoundException("PaymentInfo", "Payment info for customer with Uuid: " +
                        customerUuid + " not found"));
        PaymentInfoReadOnlyDTO deletedInfo = mapper.mapToPaymentInfoReadOnlyDTO(paymentInfo);
        customer.setPaymentInfo(null);
        customerRepository.save(customer);
        paymentInfoRepository.delete(paymentInfo);
        return deletedInfo;
    }


    /**
     * Constructs a {@link Specification} for querying customers based on the provided filters.
     * <p>
     * This method builds a dynamic query specification by combining various conditions
     * using the {@link Specification} interface. Each condition corresponds to a filter
     * field, and all conditions are combined with logical AND.
     * </p>
     *
     * @param filters the {@link CustomerFilters} object containing the filter criteria.
     * @return a {@link Specification<Customer>} object representing the combined query conditions.
     */
    private Specification<Customer> getSpecsFromFilters(CustomerFilters filters) {
        return Specification
                .where(CustomerSpecification.trStringFieldLike("uuid", filters.getUuid()))
                .and(CustomerSpecification.trStringFieldLike("id",filters.getId()))
                .and(CustomerSpecification.trStringFieldLike("lastname",filters.getLastname()))
                .and(CustomerSpecification.customerUserUsernameIs(filters.getUsername()))
                .and(CustomerSpecification.trUserIsActive(filters.getActive()))
                .and(CustomerSpecification.trCustomerInfoPhoneIs(filters.getPhoneNumber()))
                .and(CustomerSpecification.trCustomerOrderIs(filters.getOrderId()));

    }
}
