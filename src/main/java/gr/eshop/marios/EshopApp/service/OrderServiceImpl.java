package gr.eshop.marios.EshopApp.service;

import gr.eshop.marios.EshopApp.core.exceptions.AppObjectAlreadyExists;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectInvalidArgumentException;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectNotFoundException;
import gr.eshop.marios.EshopApp.core.exceptions.AppServerException;
import gr.eshop.marios.EshopApp.dto.CustomerReadOnlyDTO;
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

    /**
     * Saves an order for a specific customer.
     * <p>
     * This method creates and saves a new {@link Order} for a customer identified by their UUID. It also updates
     * the stock levels of the products included in the order.
     * </p>
     *
     * @param dto the {@link OrderInsertDTO} containing the order details.
     * @return an {@link OrderReadOnlyDTO} representing the saved order.
     * @throws AppServerException if a server error occurs during the operation.
     * @throws AppObjectNotFoundException if the customer or any product in the order is not found.
     * @throws AppObjectAlreadyExists if a conflict occurs with existing data (not used in the current logic).
     * @throws AppObjectInvalidArgumentException if the order does not contain any items or the stock levels are invalid.
     * @throws IOException if an input/output error occurs.
     */
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

    /**
     * Updates the stock levels of products after a purchase.
     * <p>
     * This method adjusts the stock levels of products based on the quantities purchased in the order.
     * It ensures that stock levels cannot be negative.
     * </p>
     *
     * @param orderItemInsertDTOS a list of {@link OrderItemInsertDTO} containing the purchased product details.
     * @throws AppServerException if a server error occurs during the operation.
     * @throws AppObjectNotFoundException if any product is not found.
     * @throws AppObjectInvalidArgumentException if the resulting stock level for any product is negative.
     * @throws IOException if an input/output error occurs.
     */
    @Transactional
    public void updateProductAfterPurchase (List<OrderItemInsertDTO> orderItemInsertDTOS) throws AppServerException,
            AppObjectNotFoundException ,AppObjectAlreadyExists, AppObjectInvalidArgumentException, IOException {
        for (OrderItemInsertDTO item : orderItemInsertDTOS) {
            Product product = productRepository.findBySku(item.getSku()).
                    orElseThrow(() -> new AppObjectNotFoundException("Product","Product not found with Sku: " + item.getSku()));
            product.setQuantity(product.getQuantity() - item.getQuantity());
            product.setInStock(product.isInStock());
            if (product.getQuantity() < 0) {
                throw new AppObjectInvalidArgumentException("Product", "Product stock cannot be negative for SKU: " + item.getSku());
            }
            productRepository.save(product);

        }
    }

    /**
     * Retrieves all orders associated with a specific customer by their UUID.
     *
     * @param customerUuid the unique identifier of the customer whose orders are to be retrieved
     * @return a list of {@link OrderReadOnlyDTO} objects representing the orders associated with the customer
     * @throws AppServerException if a server-related error occurs during the operation
     * @throws AppObjectNotFoundException if the customer with the specified UUID is not found
     */
    @Override
    public List<OrderReadOnlyDTO> getAllOrders(String customerUuid) throws AppServerException, AppObjectNotFoundException {
        Customer customer = customerRepository.findByUuid(customerUuid)
                .orElseThrow(() -> new AppObjectNotFoundException("Customer", "Customer with Uuid: " + customerUuid + " not found"));
        LOGGER.info("Customer  {} was found", customer);


        CustomerReadOnlyDTO customerDto = mapper.mapToCustomerReadOnlyDTO(customer);
        List<OrderReadOnlyDTO> orderReadOnlyDTOS = customerDto.getOrdersReadOnlyDTOs();
        return orderReadOnlyDTOS;
    }

/**
 * Retrieves an order by its ID.
 * <p>
 * This method fetches an {@link Order} record from the database using its ID and maps it to
 * an {@link OrderReadOnlyDTO} for the response.
 * </p>
 *
 * @param order_id the ID of the order to retrieve.
 * @return an {@link OrderReadOnlyDTO} representing the order.
 * @throws AppServerException if a server error occurs during the operation.
 * @throws AppObjectNotFoundException if the order is not found.
 */
    @Transactional(rollbackOn = Exception.class)
    public OrderReadOnlyDTO getOrderById(Long order_id) throws AppServerException, AppObjectNotFoundException {

        Order order = orderRepository.findById(order_id)
                .orElseThrow(() -> new RuntimeException("Order with ID: " + order_id + " not found"));
        return mapper.mapToReadOnlyOrder(order);
    }

}
