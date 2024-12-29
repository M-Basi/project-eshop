package gr.eshop.marios.EshopApp.service;

import gr.eshop.marios.EshopApp.core.exceptions.AppObjectAlreadyExists;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectInvalidArgumentException;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectNotFoundException;
import gr.eshop.marios.EshopApp.core.exceptions.AppServerException;
import gr.eshop.marios.EshopApp.dto.OrderInsertDTO;
import gr.eshop.marios.EshopApp.dto.OrderReadOnlyDTO;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IOrderService {
    OrderReadOnlyDTO saveOrderToCustomer(OrderInsertDTO dto ) throws AppObjectNotFoundException, AppServerException, AppObjectAlreadyExists, AppObjectInvalidArgumentException, IOException;

    Set<OrderReadOnlyDTO> getAllOrders(String customerUuid) throws AppServerException, AppObjectNotFoundException;
}
