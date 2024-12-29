package gr.eshop.marios.EshopApp.service;

import gr.eshop.marios.EshopApp.core.exceptions.AppObjectAlreadyExists;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectInvalidArgumentException;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectNotFoundException;
import gr.eshop.marios.EshopApp.core.exceptions.AppServerException;
import gr.eshop.marios.EshopApp.core.filters.Paginated;
import gr.eshop.marios.EshopApp.core.filters.ProductFilters;
import gr.eshop.marios.EshopApp.dto.ProductInsertDTO;
import gr.eshop.marios.EshopApp.dto.ProductReadOnlyDTO;
import gr.eshop.marios.EshopApp.dto.ProductUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {

    ProductReadOnlyDTO saveProduct(ProductInsertDTO dto, MultipartFile productImage) throws AppServerException, AppObjectAlreadyExists, AppObjectInvalidArgumentException, IOException;
    ProductReadOnlyDTO updateProduct(ProductUpdateDTO dto, MultipartFile productImage) throws AppServerException, AppObjectNotFoundException, IOException;
    ProductReadOnlyDTO deleteProduct(Long id) throws AppServerException, AppObjectNotFoundException;
    Paginated<ProductReadOnlyDTO> getProductFilteredPaginated(ProductFilters filters);
    Page<ProductReadOnlyDTO> getPaginatedProducts(int page, int size) ;
    Page<ProductReadOnlyDTO> getPaginatedProducts(int page, int size, String sortBy, String sortDirection);
    List<ProductReadOnlyDTO> getAllProductsList() throws AppObjectNotFoundException;


    ProductReadOnlyDTO getProductById(Long id) throws AppObjectNotFoundException;
}
