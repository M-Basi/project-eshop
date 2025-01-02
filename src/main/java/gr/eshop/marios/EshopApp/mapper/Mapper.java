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
    private final OrderItemRepository orderItemRepository;
    private final AttachmentPhotoRepository attachmentPhotoRepository;

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



    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {

        return new UserReadOnlyDTO(user.getUuid(),user.getUsername(),user.getRole(),
                user.getIsActive());
    }



    public Customer mapToCustomer(CustomerInsertDTO customerInsertDTO) {
        Customer customer = new Customer();
        customer.setFirstname(customerInsertDTO.getFirstname());
        customer.setLastname(customerInsertDTO.getLastname());

        User user = userRepository.findByUuid(customerInsertDTO.getUserUuid())
                .orElseThrow(() -> new RuntimeException("User with Uuid: " + customerInsertDTO.getUserUuid() + " not found"));

        customer.setUser(user);
        return customer;
    }



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

//    public CustomerInfo mapToUpdateCustomerInfo(CustomerInfoUpdateDTO customerInfoUpdateDTO) {
//        CustomerInfo customerInfo = new CustomerInfo();
//        customerInfo.setId(customerInfoUpdateDTO.getId());
//        customerInfo.setPhoneNumber(customerInfoUpdateDTO.getPhoneNumber());
//        customerInfo.setCity(customerInfoUpdateDTO.getCity());
//        customerInfo.setCountry(customerInfoUpdateDTO.getCountry());
//        Region region= regionRepository.findByName(customerInfoUpdateDTO.getRegion())
//                .orElseThrow(() -> new RuntimeException("Region with name: "
//                        + customerInfoUpdateDTO.getRegion() + " not found"));
//        customerInfo.setRegion(region);
//        customerInfo.setZipCode(customerInfoUpdateDTO.getZipCode());
//        customerInfo.setStreetNumber(customerInfoUpdateDTO.getStreetNumber());
//        customerInfo.setStreet(customerInfoUpdateDTO.getStreet());
//
//        Customer customer = customerRepository.findByUuid(customerInfoUpdateDTO.getCustomerUuid())
//                .orElseThrow(() -> new RuntimeException("User with Uuid: " + customerInfoUpdateDTO.getCustomerUuid() + " not found"));
//
//        customerInfo.setCustomer(customer);
//        return customerInfo;
//    }

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

//    public PaymentInfo mapToUpdatePaymentInfo(PaymentInfoUpdateDTO paymentInfoUpdateDTO) {
//        PaymentInfo paymentInfo = new PaymentInfo();
//        paymentInfo.setId(paymentInfoUpdateDTO.getId());
//        paymentInfo.setCard(paymentInfoUpdateDTO.getCard());
//        paymentInfo.setExpiredDate(paymentInfoUpdateDTO.getExpiredDate());
//        paymentInfo.setCardName(paymentInfoUpdateDTO.getCardName());
//
//        Customer customer = customerRepository.findByUuid(paymentInfoUpdateDTO.getCustomerUuid())
//                .orElseThrow(() -> new RuntimeException("User with Uuid: " +
//                        paymentInfoUpdateDTO.getCustomerUuid() + " not found"));
//        paymentInfo.setCustomer(customer);
//
//        return paymentInfo;
//    }

    public PaymentInfoReadOnlyDTO mapToPaymentInfoReadOnlyDTO(PaymentInfo paymentInfo) {
        var dto = new PaymentInfoReadOnlyDTO();
        dto.setId(paymentInfo.getId());
        dto.setCard(paymentInfo.getCard());
        dto.setExpiredDate(paymentInfo.getExpiredDate());
        dto.setCardName(paymentInfo.getCardName());
        dto.setCardValidation(paymentInfo.getCardValidation());
        return dto;
    }

    public CustomerReadOnlyDTO mapToCustomerReadOnlyDTO(Customer customer) {
        CustomerReadOnlyDTO customerReadOnlyDTO = new CustomerReadOnlyDTO();
//        customerReadOnlyDTO.setId(customer.getId());
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

    

    public Set<OrderReadOnlyDTO> mapOrdersToReadOnlyDTO(Set<Order> orders) {
        return orders.stream()
                .map(this::mapToReadOnlyOrder)
                .collect(Collectors.toSet());
    }

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


    public Set<OrderItemReadOnlyDTO> mapOrderItemsToReadOnlyDTO(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::mapToReadOnlyOrderItem)
                .collect(Collectors.toSet());
    }

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
//        for (OrderItem item : orderInsertDTO.getOrderItems()) {
//            OrderItem orderItem = new OrderItem();
//            orderItem.setSku(item.getSku());
//            orderItem.setName(item.getName());
//            Category category = categoryRepository.findById(item.productInsertDTO.getCategoryNumber())
//                    .orElseThrow(() -> new RuntimeException("Category with id : " + productInsertDTO.getCategoryNumber() + " not found"));
//            LOGGER.info("Category: {}",category);
//            Brand brand = brandRepository.findById(productInsertDTO.getBrandNumber())
//                    .orElseThrow(() -> new RuntimeException("Brand with id: " + productInsertDTO.getBrandNumber() + " not found"));
//            orderItem.setBrand(item.getBrand());
//            orderItem.setCategory(item.getCategory());
//            orderItem.setQuantity(item.getQuantity());
//            orderItem.setUnitPrice(item.getUnitPrice());
//            AttachmentPhoto attachmentPhoto = attachmentPhotoRepository.findById(item.getAttachmentPhoto().getId()).orElse(null);
//            orderItem.setAttachmentPhoto(attachmentPhoto);
//            orderItem.setTotalPrice(item.getTotalPrice());
//            orderItem.setOrder(order);
//            order.addOrderedItem(item);
//
//        }


    }

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

    public Set<OrderItem> mapOrderItemsTo(List<OrderItemInsertDTO> orderItemInsertDTOs) {
        return orderItemInsertDTOs.stream()
                .map(this::mapToOrderItem)
                .collect(Collectors.toSet());
    }



    public AttachmentReadOnlyDTO mapAttachmentToReadOnlyDTO(AttachmentPhoto attachmentPhoto) {
        var attachment = new AttachmentReadOnlyDTO();
        attachment.setFilename(attachmentPhoto.getFilename());
        attachment.setExtension(attachmentPhoto.getExtension());
        attachment.setSavedName(attachmentPhoto.getSavedName());
        attachment.setFilePath(attachmentPhoto.getFilePath());
        attachment.setContentType(attachmentPhoto.getContentType());

        return attachment;
    }






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

    public AdminUserReadOnlyDTO mapAdminToReadOnlyDTO(AdminUser admin) {
        return new AdminUserReadOnlyDTO(admin.getId(),admin.getFirstname(), admin.getFirstname(), admin.getUuid());
    }


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

    public BrandReadOnlyDTO mapBrandToReadOnlyDTO(Brand brand) {
        return new BrandReadOnlyDTO(brand.getId(), brand.getBrandName());
    }

    public CategoryReadOnlyDTO mapCategoryReadOnlyDTO(Category category) {
        return new CategoryReadOnlyDTO(category.getId(), category.getCategoryName());
    }

}

