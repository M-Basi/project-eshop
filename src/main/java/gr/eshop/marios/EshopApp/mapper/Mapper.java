package gr.eshop.marios.EshopApp.mapper;

import gr.eshop.marios.EshopApp.core.enums.Role;
import gr.eshop.marios.EshopApp.dto.*;
import gr.eshop.marios.EshopApp.model.*;
import gr.eshop.marios.EshopApp.model.static_data.Region;
import gr.eshop.marios.EshopApp.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class Mapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Mapper.class);
    private final PasswordEncoder passwordEncoder;


    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final RegionRepository regionRepository;
    private final ProductRepository productRepository;


    /**
     * Maps a UserInsertDTO to a User entity.
     *
     * @param userInsertDTO the UserInsertDTO containing the user details
     * @return the mapped User entity
     */
    public User mapToUser(UserInsertDTO userInsertDTO) {
        User user = new User();
        user.setUsername(userInsertDTO.getUsername());
        String password = passwordEncoder.encode(userInsertDTO.getPassword());
        user.setPassword(password);
        Role role = (userInsertDTO.getRole() != null) ? userInsertDTO.getRole() : Role.CUSTOMER_USER;
        user.setRole(role);
        Boolean active = (userInsertDTO.getIsActive() != null) ? userInsertDTO.getIsActive() : true;
        user.setIsActive(active);

        return user;
    }


    /**
     * Maps a User entity to a UserReadOnlyDTO.
     *
     * @param user the User entity to map
     * @return the mapped UserReadOnlyDTO
     */
    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {

        return new UserReadOnlyDTO(user.getUuid(),user.getUsername(),user.getRole(),
                user.getIsActive());
    }


    /**
     * Maps a CustomerInsertDTO to a Customer entity.
     *
     * @param customerInsertDTO the CustomerInsertDTO containing customer details
     * @return the mapped Customer entity
     */
    public Customer mapToCustomer(CustomerInsertDTO customerInsertDTO) {
        Customer customer = new Customer();
        customer.setFirstname(customerInsertDTO.getFirstname());
        customer.setLastname(customerInsertDTO.getLastname());

        User user = userRepository.findByUuid(customerInsertDTO.getUserUuid())
                .orElseThrow(() -> new RuntimeException("User with Uuid: " + customerInsertDTO.getUserUuid() + " not found"));

        customer.setUser(user);
        return customer;
    }


    /**
     * Maps a CustomerInfoInsertDTO to a CustomerInfo entity.
     *
     * @param customerInfoInsertDTO the CustomerInfoInsertDTO containing customer info details
     * @return the mapped CustomerInfo entity
     */
    public CustomerInfo mapToCustomerInfo(CustomerInfoInsertDTO customerInfoInsertDTO) {

        LOGGER.info("Mapping CustomerInfo for DTO: {}", customerInfoInsertDTO);
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setPhoneNumber(customerInfoInsertDTO.getPhoneNumber());
        customerInfo.setCity(customerInfoInsertDTO.getCity());
        customerInfo.setCountry(customerInfoInsertDTO.getCountry());

        Region region= regionRepository.findByName(customerInfoInsertDTO.getRegion())
                .orElseThrow(() -> new RuntimeException("Region with name: "
                        + customerInfoInsertDTO.getRegion() + " not found"));
        customerInfo.setRegion(region);

        customerInfo.setZipCode(customerInfoInsertDTO.getZipCode());
        customerInfo.setStreetNumber(customerInfoInsertDTO.getStreetNumber());
        customerInfo.setStreet(customerInfoInsertDTO.getStreet());

        Customer customer = customerRepository.findByUuid(customerInfoInsertDTO.getCustomerUuid())
                .orElseThrow(() -> new RuntimeException("Customer with Uuid: " + customerInfoInsertDTO.getCustomerUuid() + " not found"));

        customerInfo.setCustomer(customer);
        return customerInfo;
    }

    /**
     * Maps a CustomerInfo entity to a CustomerInfoReadOnlyDTO.
     *
     * @param customerInfo the CustomerInfo entity to map
     * @return the mapped CustomerInfoReadOnlyDTO
     */
    public CustomerInfoReadOnlyDTO mapToCustomerInfoReadOnlyDTO(CustomerInfo customerInfo) {
        CustomerInfoReadOnlyDTO customerInfoReadOnlyDTO = new CustomerInfoReadOnlyDTO();
        customerInfoReadOnlyDTO.setId(customerInfo.getId());
        customerInfoReadOnlyDTO.setPhoneNumber(customerInfo.getPhoneNumber());
        customerInfoReadOnlyDTO.setCity(customerInfo.getCity());
        customerInfoReadOnlyDTO.setCountry(customerInfo.getCountry());
        customerInfoReadOnlyDTO.setRegion(customerInfo.getRegion());
        customerInfoReadOnlyDTO.setZipCode(customerInfo.getZipCode());
        customerInfoReadOnlyDTO.setStreetNumber(customerInfo.getStreetNumber());
        customerInfoReadOnlyDTO.setStreet(customerInfo.getStreet());

        return customerInfoReadOnlyDTO;
    }

    /**
     * Maps a PaymentInfoInsertDTO to a PaymentInfo entity.
     *
     * @param paymentInfoInsertDTO the PaymentInfoInsertDTO containing payment info details
     * @return the mapped PaymentInfo entity
     */
    public PaymentInfo mapToPaymentInfo(PaymentInfoInsertDTO paymentInfoInsertDTO) {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCard(paymentInfoInsertDTO.getCard());
        paymentInfo.setExpiredDate(paymentInfoInsertDTO.getExpiredDate());
        paymentInfo.setCardName(paymentInfoInsertDTO.getCardName());
        paymentInfo.setCardValidation(paymentInfoInsertDTO.getCardValidation());

        Customer customer = customerRepository.findByUuid(paymentInfoInsertDTO.getCustomerUuid())
                .orElseThrow(() -> {
                    String errorMessage = "Customer with Uuid: " + paymentInfoInsertDTO.getCustomerUuid() + " not found";
                    LOGGER.info(errorMessage);
                    return new RuntimeException(errorMessage);
                });
        paymentInfo.setCustomer(customer);

        return paymentInfo;
    }

    /**
     * Maps a PaymentInfo entity to a PaymentInfoReadOnlyDTO.
     *
     * @param paymentInfo the PaymentInfo entity to map
     * @return the mapped PaymentInfoReadOnlyDTO
     */
    public PaymentInfoReadOnlyDTO mapToPaymentInfoReadOnlyDTO(PaymentInfo paymentInfo) {
        var dto = new PaymentInfoReadOnlyDTO();
        dto.setId(paymentInfo.getId());
        dto.setCard(paymentInfo.getCard());
        dto.setExpiredDate(paymentInfo.getExpiredDate());
        dto.setCardName(paymentInfo.getCardName());
        dto.setCardValidation(paymentInfo.getCardValidation());
        return dto;
    }

    /**
     * Maps a Customer entity to a CustomerReadOnlyDTO.
     *
     * @param customer the Customer entity to map
     * @return the mapped CustomerReadOnlyDTO
     */
    public CustomerReadOnlyDTO mapToCustomerReadOnlyDTO(Customer customer) {
        CustomerReadOnlyDTO customerReadOnlyDTO = new CustomerReadOnlyDTO();
        customerReadOnlyDTO.setUuid(customer.getUuid());
        customerReadOnlyDTO.setFirstname(customer.getFirstname());
        customerReadOnlyDTO.setLastname(customer.getLastname());
        customerReadOnlyDTO.setUserReadOnlyDTO(mapToUserReadOnlyDTO(customer.getUser()));
        customerReadOnlyDTO.setCustomerInfoReadOnlyDTO(
                customer.getCustomerInfo() != null ? mapToCustomerInfoReadOnlyDTO(customer.getCustomerInfo()) : null
        );

        customerReadOnlyDTO.setPaymentInfoReadOnlyDTO(
                customer.getPaymentInfo() != null ? mapToPaymentInfoReadOnlyDTO(customer.getPaymentInfo()) : null
        );

        Set<OrderReadOnlyDTO> orders = mapOrdersToReadOnlyDTO(customer.getOrders());
        List<OrderReadOnlyDTO> ordersReadOnlyDTOs= new ArrayList<>(orders);
        ordersReadOnlyDTOs.sort(Comparator.comparing(OrderReadOnlyDTO::getId).reversed());
        customerReadOnlyDTO.setOrdersReadOnlyDTOs(ordersReadOnlyDTOs);
        return customerReadOnlyDTO;
    }


    /**
     * Maps a set of Order entities to a set of OrderReadOnlyDTOs.
     *
     * @param orders the set of Order entities to map
     * @return the mapped set of OrderReadOnlyDTOs
     */
    public Set<OrderReadOnlyDTO> mapOrdersToReadOnlyDTO(Set<Order> orders) {
        return orders.stream()
                .map(this::mapToReadOnlyOrder)
                .collect(Collectors.toSet());
    }

    /**
     * Maps an Order entity to an OrderReadOnlyDTO.
     *
     * @param order the Order entity to map
     * @return the mapped OrderReadOnlyDTO
     */
    public OrderReadOnlyDTO mapToReadOnlyOrder(Order order) {
        var dto = new OrderReadOnlyDTO();
        dto.setId(order.getId());
        dto.setUuid(order.getUuid());
        dto.setStatus(order.getStatus());
        dto.setCreatedDate(order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dto.setOrderTrackingNumber(order.getOrderTrackingNumber());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setOrderItems(mapOrderItemsToReadOnlyDTO(order.getOrderItems()));

        return dto;

    }

    /**
     * Maps a set of OrderItem entities to a set of OrderItemReadOnlyDTOs.
     *
     * @param orderItems the set of OrderItem entities to map
     * @return the mapped set of OrderItemReadOnlyDTOs
     */
    public Set<OrderItemReadOnlyDTO> mapOrderItemsToReadOnlyDTO(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::mapToReadOnlyOrderItem)
                .collect(Collectors.toSet());
    }

    /**
     * Maps an OrderItem entity to an OrderItemReadOnlyDTO.
     *
     * @param orderItem the OrderItem entity to map
     * @return the mapped OrderItemReadOnlyDTO
     */
    public OrderItemReadOnlyDTO mapToReadOnlyOrderItem(OrderItem orderItem) {
        var dto = new OrderItemReadOnlyDTO();
        dto.setId(orderItem.getId());
        dto.setName(orderItem.getName());
        dto.setSku(orderItem.getSku());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getUnitPrice());
        dto.setBrand(orderItem.getBrand().getBrandName());
        dto.setCategory(orderItem.getCategory().getCategoryName());
        var image = mapAttachmentToReadOnlyDTO(orderItem.getAttachmentPhoto());
        dto.setImage(image);
        dto.setTotalPrice(orderItem.getTotalPrice());


        return dto;
    }



    /**
     * Maps an OrderInsertDTO to an Order entity.
     *
     * @param orderInsertDTO the OrderInsertDTO containing order details
     * @return the mapped Order entity
     */
    public Order mapToOrder(OrderInsertDTO orderInsertDTO) {
        OrderReadOnlyDTO orderReadOnlyDTO = new OrderReadOnlyDTO();
        var order = new Order();
        Customer customer = customerRepository.findByUuid(orderInsertDTO.getCustomerUuid())
                .orElseThrow(() -> {
                    String errorMessage = "Customer with Uuid: " + orderInsertDTO.getCustomerUuid() + " not found";
                    LOGGER.info(errorMessage);
                    return new RuntimeException(errorMessage);
                });
        LOGGER.info("Customer: " + customer.getUuid());

        order.setStatus((orderInsertDTO.getStatus()) != null ? orderInsertDTO.getStatus() : "CONFIRMED");
        order.setOrderItems(mapOrderItemsTo(orderInsertDTO.getOrderItems()));

        order.setOrderTrackingNumber(order.getOrderTrackingNumber());
        order.setTotalPrice(order.totalPrice());
        order.setCustomer(customer);


        return order;
    }

    /**
     * Maps an OrderItemInsertDTO to an OrderItem entity.
     *
     * @param orderItemInsertDTO the OrderItemInsertDTO containing order item details
     * @return the mapped OrderItem entity
     */
    public OrderItem mapToOrderItem(OrderItemInsertDTO orderItemInsertDTO) {
        OrderItem orderItem = new OrderItem();

        Product product = productRepository.findBySku(orderItemInsertDTO.getSku())
                .orElseThrow(() -> new RuntimeException("Product with Sku: " + orderItemInsertDTO.getSku() + " not found"));
        orderItem.setName(product.getName());
        orderItem.setUnitPrice(product.getPrice());
        orderItem.setQuantity(orderItemInsertDTO.getQuantity());
        orderItem.setSku(orderItemInsertDTO.getSku());
        orderItem.setCategory(product.getCategory());
        LOGGER.info("Category: {}",product.getCategory());
        orderItem.setBrand(product.getBrand());
        LOGGER.info("Brand: {}",product.getBrand());

        orderItem.setAttachmentPhoto(product.getAttachmentPhoto());
        LOGGER.info("Attachment: {}", product.getAttachmentPhoto());
        LOGGER.info("Ordered item ok: {}", orderItem.getName());
        orderItem.setTotalPrice(orderItem.totalItemOrderPrice() );

        return orderItem;

    }

    /**
     * Maps a list of OrderItemInsertDTOs to a set of OrderItem entities.
     *
     * @param orderItemInsertDTOs the list of OrderItemInsertDTOs
     * @return the mapped set of OrderItem entities
     */
    public Set<OrderItem> mapOrderItemsTo(List<OrderItemInsertDTO> orderItemInsertDTOs) {
        return orderItemInsertDTOs.stream()
                .map(this::mapToOrderItem)
                .collect(Collectors.toSet());
    }

    /**
     * Maps an AttachmentPhoto to an AttachmentReadOnlyDTO.
     *
     * @param attachmentPhoto the AttachmentPhoto to map
     * @return the mapped AttachmentReadOnlyDTO
     */
    public AttachmentReadOnlyDTO mapAttachmentToReadOnlyDTO(AttachmentPhoto attachmentPhoto) {
        var attachment = new AttachmentReadOnlyDTO();
        attachment.setFilename(attachmentPhoto.getFilename());
        attachment.setExtension(attachmentPhoto.getExtension());
        attachment.setSavedName(attachmentPhoto.getSavedName());
        attachment.setFilePath(attachmentPhoto.getFilePath());
        attachment.setContentType(attachmentPhoto.getContentType());

        return attachment;
    }

    /**
     * Maps an AdminUserInsertDTO to an AdminUser entity.
     *
     * @param adminUserInsertDTO the AdminUserInsertDTO containing admin user details
     * @return the mapped AdminUser entity
     */
    public AdminUser mapToAdmin(AdminUserInsertDTO adminUserInsertDTO) {
        AdminUser admin = new AdminUser();
        admin.setFirstname(adminUserInsertDTO.getFirstname());
        admin.setLastname(adminUserInsertDTO.getLastname());
        User user = userRepository.findByUuid(adminUserInsertDTO.getUserUuid())
                .orElseThrow(() -> new RuntimeException("User with Uuid: "
                        + adminUserInsertDTO.getUserUuid() + " not found"));
        admin.setUser(user);

        return admin;
    }

    /**
     * Maps an AdminUserUpdateDTO to an AdminUser entity for updating purposes.
     *
     * @param adminUserUpdateDTO the AdminUserUpdateDTO containing admin user details
     * @return the updated AdminUser entity
     */
    public AdminUser mapToUpdateAdmin(AdminUserUpdateDTO adminUserUpdateDTO) {
        AdminUser admin = new AdminUser();
        admin.setId(adminUserUpdateDTO.getId());
        admin.setUuid(adminUserUpdateDTO.getUuid());
        admin.setFirstname(adminUserUpdateDTO.getFirstname());
        admin.setLastname(adminUserUpdateDTO.getLastname());
        User user = userRepository.findByUuid(adminUserUpdateDTO.getUserUuid())
                .orElseThrow(() -> new RuntimeException("User with Uuid: " + adminUserUpdateDTO.getUserUuid() + " not found"));
        admin.setUser(user);

        return admin;
    }

    /**
     * Maps an AdminUser entity to an AdminUserReadOnlyDTO.
     *
     * @param admin the AdminUser entity to map
     * @return the mapped AdminUserReadOnlyDTO
     */
    public AdminUserReadOnlyDTO mapAdminToReadOnlyDTO(AdminUser admin) {
        return new AdminUserReadOnlyDTO(admin.getId(),admin.getFirstname(), admin.getFirstname(), admin.getUuid());
    }

    /**
     * Maps a ProductInsertDTO to a Product entity.
     *
     * @param productInsertDTO the ProductInsertDTO containing product details
     * @return the mapped Product entity
     */
    public Product mapToProduct(ProductInsertDTO productInsertDTO) {
        Product product = new Product();
        LOGGER.info("Product: " + productInsertDTO);

        product.setName(productInsertDTO.getName());
        product.setIsActive((productInsertDTO.getIsActive() != null) ? productInsertDTO.getIsActive() : true);
        product.setDescription(productInsertDTO.getDescription());
        product.setPrice(productInsertDTO.getPrice());
        product.setQuantity(productInsertDTO.getQuantity());
        product.setInStock(product.isInStock());

        Category category = categoryRepository.findByCategoryName(productInsertDTO.getCategoryName())
                .orElseThrow(() -> new RuntimeException("Category with name : " + productInsertDTO.getCategoryName() + " not found"));
        LOGGER.info("Category: {}",category);
        Brand brand = brandRepository.findByBrandName(productInsertDTO.getBrandName())
                .orElseThrow(() -> new RuntimeException("Brand with name: " + productInsertDTO.getBrandName() + " not found"));
        product.setCategory(category);
        product.setBrand(brand);
        product.setSku(productInsertDTO.getSku());


        return product;

    }

    /**
     * Maps a ProductUpdateDTO to a Product entity for updating purposes.
     *
     * @param productUpdateDTO the ProductUpdateDTO containing product details
     * @return the updated Product entity
     */
    public Product mapToUpdateProduct(ProductUpdateDTO productUpdateDTO) {
        Product product = new Product();
        LOGGER.info("{}", productUpdateDTO);
        product.setId(productUpdateDTO.getId());
        product.setUuid(productUpdateDTO.getUuid());
        product.setName(productUpdateDTO.getName());
        Category category = new Category();
        category = categoryRepository.findByCategoryName(productUpdateDTO.getCategoryName())
                .orElseThrow(() -> new RuntimeException("Category with name: " + productUpdateDTO.getCategoryName() + " not found"));
        Brand brand = new Brand();
        brand = brandRepository.findByBrandName(productUpdateDTO.getBrandName())
                .orElseThrow(() -> new RuntimeException("Brand with name : " + productUpdateDTO.getBrandName() + " not found"));
        product.setBrand(brand);
        product.setCategory(category);
        product.setInStock(product.getInStock());
        product.setDescription(productUpdateDTO.getDescription());
        product.setPrice(productUpdateDTO.getPrice());
        product.setQuantity(productUpdateDTO.getQuantity());
        product.setSku(productUpdateDTO.getSku());
        product.setIsActive((productUpdateDTO.getIsActive() != null ? productUpdateDTO.getIsActive() : true ));

        return product;

    }

    /**
     * Maps a Product entity to a ProductReadOnlyDTO.
     *
     * @param product the Product entity to map
     * @return the mapped ProductReadOnlyDTO
     */
    public ProductReadOnlyDTO mapToProductReadOnlyDTO(Product product) {
        ProductReadOnlyDTO productReadOnlyDTO = new ProductReadOnlyDTO();
        productReadOnlyDTO.setId(product.getId());
        productReadOnlyDTO.setUuid(product.getUuid());
        productReadOnlyDTO.setName(product.getName());
        productReadOnlyDTO.setDescription(product.getDescription());
        productReadOnlyDTO.setPrice(product.getPrice());
        productReadOnlyDTO.setQuantity(product.getQuantity());
        productReadOnlyDTO.setCategory(mapCategoryReadOnlyDTO(product.getCategory()));
        productReadOnlyDTO.setBrand(mapBrandToReadOnlyDTO(product.getBrand()));
        productReadOnlyDTO.setSku(product.getSku());
        productReadOnlyDTO.setInStock(product.getInStock());
        productReadOnlyDTO.setIsActive(product.getIsActive());

        var image = mapAttachmentToReadOnlyDTO(product.getAttachmentPhoto());
        productReadOnlyDTO.setImage(mapAttachmentToReadOnlyDTO(product.getAttachmentPhoto()));

        return productReadOnlyDTO;

    }

    /**
     * Maps a Brand entity to a BrandReadOnlyDTO.
     *
     * @param brand the Brand entity to map
     * @return the mapped BrandReadOnlyDTO
     */
    public BrandReadOnlyDTO mapBrandToReadOnlyDTO(Brand brand) {
        return new BrandReadOnlyDTO(brand.getId(), brand.getBrandName());
    }

    /**
     * Maps a Category entity to a CategoryReadOnlyDTO.
     *
     * @param category the Category entity to map
     * @return the mapped CategoryReadOnlyDTO
     */
    public CategoryReadOnlyDTO mapCategoryReadOnlyDTO(Category category) {
        return new CategoryReadOnlyDTO(category.getId(), category.getCategoryName());
    }


}

