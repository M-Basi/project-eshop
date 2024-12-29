package gr.eshop.marios.EshopApp.service;

import gr.eshop.marios.EshopApp.core.exceptions.AppObjectAlreadyExists;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectInvalidArgumentException;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectNotFoundException;
import gr.eshop.marios.EshopApp.core.exceptions.AppServerException;
import gr.eshop.marios.EshopApp.dto.OrderInsertDTO;
import gr.eshop.marios.EshopApp.dto.OrderItemInsertDTO;
import gr.eshop.marios.EshopApp.dto.OrderReadOnlyDTO;
import gr.eshop.marios.EshopApp.mapper.Mapper;
import gr.eshop.marios.EshopApp.model.Customer;
import gr.eshop.marios.EshopApp.model.Order;
import gr.eshop.marios.EshopApp.model.OrderItem;
import gr.eshop.marios.EshopApp.model.Product;
import gr.eshop.marios.EshopApp.repository.CustomerRepository;
import gr.eshop.marios.EshopApp.repository.OrderItemRepository;
import gr.eshop.marios.EshopApp.repository.OrderRepository;
import gr.eshop.marios.EshopApp.repository.ProductRepository;
import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;
    private final Mapper mapper;
    private final ProductRepository productRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public OrderReadOnlyDTO saveOrderToCustomer(OrderInsertDTO dto)
            throws AppServerException, AppObjectNotFoundException ,AppObjectAlreadyExists, AppObjectInvalidArgumentException, IOException {
        if (dto.getOrderItems() == null || dto.getOrderItems().isEmpty()) {
            throw new AppObjectInvalidArgumentException("Order","Order must have at least one item");
        }
        Customer customer = customerRepository.findByUuid(dto.getCustomerUuid())
                .orElseThrow(() -> new AppObjectNotFoundException("Customer","Customer not found with Uuid: "
                        + dto.getCustomerUuid() + " not found"));
        Order order = mapper.mapToOrder(dto);

        for (OrderItem orderItem : order.getAllOrderItems()) {
            if(productRepository.findBySku(orderItem.getSku()).isEmpty()) {
                throw new AppObjectNotFoundException("Product","Product with sku: " + orderItem.getSku() + " not found");
            }
            orderItem.setOrder(order); // Σύνδεση με το Order
        }
        Order savedOrder = orderRepository.save(order);
        updateProductAfterPurchase(dto.getOrderItems());

        return mapper.mapToReadOnlyOrder(savedOrder);
    }

    @Transactional
    public void updateProductAfterPurchase (List<OrderItemInsertDTO> orderItemInsertDTOS) throws AppServerException,
            AppObjectNotFoundException ,AppObjectAlreadyExists, AppObjectInvalidArgumentException, IOException {
        for (OrderItemInsertDTO item : orderItemInsertDTOS) {
            Product product = productRepository.findBySku(item.getSku()).
                    orElseThrow(() -> new AppObjectNotFoundException("Product","Product not found with Sku: " + item.getSku()));
            product.setQuantity(product.getQuantity() - item.getQuantity());
            if (product.getQuantity() < 0) {
                throw new AppObjectInvalidArgumentException("Product", "Product stock cannot be negative for SKU: " + item.getSku());
            }
            productRepository.save(product);

        }
    }

    @Override
    public Set<OrderReadOnlyDTO> getAllOrders(String customerUuid) throws AppServerException, AppObjectNotFoundException {
        Customer customer = customerRepository.findByUuid(customerUuid)
                .orElseThrow(() -> new AppObjectNotFoundException("Customer", "Customer with Uuid: " + customerUuid + " not found"));
        LOGGER.info("Customer  {} was found", customer);

//        Set<Order> orders = customer.getOrders();
//        Set<Order> sortedOrders = new TreeSet<>(Comparator.comparing(Order::getCreatedAt));
//        sortedOrders.addAll(orders);
//        return mapper.mapOrdersToReadOnlyDTO(sortedOrders);

        Set<Order> orders = customer.getOrders();


        List<Order> sortedOrders = new ArrayList<>(orders);


        sortedOrders.sort(Comparator.comparing(Order::getCreatedAt));


        return sortedOrders.stream()
                .map(order -> {
                    OrderReadOnlyDTO dto = mapper.mapToReadOnlyOrder(order);
                    dto.setCreatedDate(order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    return dto;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));

    }


    @Transactional(rollbackOn = Exception.class)
    public OrderReadOnlyDTO getOrderById(Long order_id) throws AppServerException, AppObjectNotFoundException {
//        if (orderRepository.findById(order_id).isEmpty()) {
//            LOGGER.error("Order with id {} not found", order_id);
//            throw new AppObjectNotFoundException("Customer", "Customer with uuid: "
//                    + order_id + " not found");
//        }
        Order order = orderRepository.findById(order_id)
                .orElseThrow(() -> new RuntimeException("Order with ID: " + order_id + " not found"));
        return mapper.mapToReadOnlyOrder(order);
    }





}
