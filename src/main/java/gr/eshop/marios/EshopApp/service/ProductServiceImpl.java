package gr.eshop.marios.EshopApp.service;

import gr.eshop.marios.EshopApp.core.exceptions.AppObjectAlreadyExists;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectInvalidArgumentException;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectNotFoundException;
import gr.eshop.marios.EshopApp.core.exceptions.AppServerException;
import gr.eshop.marios.EshopApp.core.filters.Paginated;
import gr.eshop.marios.EshopApp.core.filters.ProductFilters;
import gr.eshop.marios.EshopApp.core.specifications.ProductSpecification;
import gr.eshop.marios.EshopApp.dto.ProductInsertDTO;
import gr.eshop.marios.EshopApp.dto.ProductReadOnlyDTO;
import gr.eshop.marios.EshopApp.dto.ProductUpdateDTO;
import gr.eshop.marios.EshopApp.mapper.Mapper;
import gr.eshop.marios.EshopApp.model.AttachmentPhoto;
import gr.eshop.marios.EshopApp.model.Product;
import gr.eshop.marios.EshopApp.repository.AttachmentPhotoRepository;
import gr.eshop.marios.EshopApp.repository.ProductRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final ProductRepository productRepository;
    private final Mapper mapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private final AttachmentPhotoRepository attachmentPhotoRepository;

/**
 * Saves a new product with its image.
 * <p>
 * This method creates a new {@link Product} in the database and associates it with an image.
 * If the SKU already exists, an {@link AppObjectAlreadyExists} exception is thrown.
 * </p>
 *
 * @param dto          the {@link ProductInsertDTO} containing the product details.
 * @param productImage the {@link MultipartFile} containing the product image.
 * @return a {@link ProductReadOnlyDTO} representing the saved product.
 * @throws AppServerException if a server error occurs.
 * @throws AppObjectAlreadyExists if a product with the same SKU already exists.
 * @throws AppObjectInvalidArgumentException if the input arguments are invalid.
 * @throws IOException if an error occurs while saving the product image.
 */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public ProductReadOnlyDTO saveProduct(ProductInsertDTO dto, MultipartFile productImage) throws AppServerException, AppObjectAlreadyExists, AppObjectInvalidArgumentException, IOException {
        if (productRepository.findBySku(dto.getSku()).isPresent()) {
            throw new AppObjectAlreadyExists("Product", "Product with sku: "
                    + dto.getSku() + " already exists");
        }
        LOGGER.info("Saving photo " + productImage.getName());
        LOGGER.info("Received product: {}", dto);

        Product product = mapper.mapToProduct(dto);
        LOGGER.info("Saving product: {}", product);

        AttachmentPhoto attachmentPhoto = saveProductImage(product,productImage);
        LOGGER.info("Saved product: {}", product.getAttachmentPhoto());

        Product savedProduct = productRepository.save(product);
        LOGGER.info("Saved product: {}", savedProduct);
        return mapper.mapToProductReadOnlyDTO(savedProduct);
    }

/**
 * Saves the image for a product.
 * <p>
 * This method handles the file storage for the product's image and creates an {@link AttachmentPhoto}
 * entity to associate with the product.
 * </p>
 *
 * @param product      the {@link Product} to associate the image with.
 * @param photoProduct the {@link MultipartFile} containing the product image.
 * @return the saved {@link AttachmentPhoto}.
 * @throws IOException if an error occurs while saving the image file.
 */
    @Transactional(rollbackOn = Exception.class)
    public AttachmentPhoto saveProductImage(Product product, MultipartFile photoProduct) throws IOException {

        AttachmentPhoto attachment = new AttachmentPhoto();
        if (photoProduct != null && !photoProduct.isEmpty()) {

            String originalFileName = photoProduct.getOriginalFilename();
            String savedName = UUID.randomUUID() + getFileExtension(originalFileName);
            String uploadDir = "uploads/";
            Path filepath = Paths.get(uploadDir + savedName);
            Files.createDirectories(filepath.getParent());
            Files.write(filepath, photoProduct.getBytes());


            attachment.setFilename(originalFileName);
            attachment.setSavedName(savedName);
            attachment.setFilePath(filepath.toString());
            attachment.setContentType(photoProduct.getContentType());
            attachment.setExtension(getFileExtension(originalFileName));


            product.setAttachmentPhoto(attachment);

        }
        return attachment;
    }

    public String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }

/**
 * Updates an existing product with new details and/or an image.
 * <p>
 * This method updates the product's details in the database. If a new image is provided,
 * it replaces the old image.
 * </p>
 *
 * @param dto          the {@link ProductUpdateDTO} containing updated product details.
 * @param productImage the {@link MultipartFile} containing the new product image (optional).
 * @return a {@link ProductReadOnlyDTO} representing the updated product.
 * @throws AppServerException if a server error occurs.
 * @throws AppObjectNotFoundException if the product with the given ID or SKU is not found.
 * @throws IOException if an error occurs while saving the product image.
 */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public ProductReadOnlyDTO updateProduct(ProductUpdateDTO dto, MultipartFile productImage) throws AppServerException,
            AppObjectNotFoundException, IOException {
        LOGGER.info("Received product: {}", dto);
        if (productRepository.findBySku(dto.getSku()).isEmpty()) {
            throw new AppObjectNotFoundException("Product", "Product with sku: "
                    + dto.getSku() + " not found");
        }
        if (productRepository.findById(dto.getId()).isEmpty()) {
            throw new AppObjectNotFoundException("Product", "Product with id: "
                    + dto.getId() + " not found");
        }


        Product product = mapper.mapToUpdateProduct(dto);
        saveProductImage(product,productImage);
        Product updatedProduct = productRepository.save(product);
        return mapper.mapToProductReadOnlyDTO(updatedProduct);
    }

/**
 * Deletes a product by its ID.
 * <p>
 * This method removes the product from the database and clears its associations
 * with related entities such as image, brand, and category.
 * </p>
 *
 * @param id the ID of the product to delete.
 * @return a {@link ProductReadOnlyDTO} representing the deleted product.
 * @throws AppServerException if a server error occurs.
 * @throws AppObjectNotFoundException if the product with the given ID is not found.
 */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public ProductReadOnlyDTO deleteProduct(Long id) throws AppServerException, AppObjectNotFoundException {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("Product", "Product with id: " + id + " not found"));
        ProductReadOnlyDTO dto = mapper.mapToProductReadOnlyDTO(product);
        product.setAttachmentPhoto(null);
        product.setBrand(null);
        product.setCategory(null);
        productRepository.delete(product);
        return dto;
    }

/**
 * Retrieves paginated and filtered products.
 * <p>
 * This method applies filters to the product query and returns a paginated list of products.
 * </p>
 *
 * @param filters the {@link ProductFilters} containing the criteria for filtering.
 * @return a {@link Paginated} object containing the filtered and paginated products as {@link ProductReadOnlyDTO}.
 */
    @Override
    public Paginated<ProductReadOnlyDTO> getProductFilteredPaginated(ProductFilters filters) {
        var filtered = productRepository.findAll(getSpecsFromFilters(filters),filters.getPageable());
        LOGGER.info("Filtered products: {}", filtered);
        return new Paginated<>(filtered.map(mapper::mapToProductReadOnlyDTO));
    }

/**
 * Retrieves paginated products with default sorting.
 * <p>
 * This method returns a paginated list of products sorted by the default field ("id") in ascending order.
 * </p>
 *
 * @param page the page number to retrieve.
 * @param size the number of products per page.
 * @return a {@link Page} of {@link ProductReadOnlyDTO}.
 */
    @Override
    public Page<ProductReadOnlyDTO> getPaginatedProducts(int page, int size) {
        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());
        return productRepository.findAll(pageable).map(mapper::mapToProductReadOnlyDTO);
    }




/**
 * Retrieves paginated products with custom sorting.
 * <p>
 * This method returns a paginated list of products sorted by the specified field and direction.
 * </p>
 *
 * @param page the page number to retrieve.
 * @param size the number of products per page.
 * @param sortBy the field by which to sort.
 * @param sortDirection the direction of sorting ("ASC" or "DESC").
 * @return a {@link Page} of {@link ProductReadOnlyDTO}.
 */
    @Override
    public Page<ProductReadOnlyDTO> getPaginatedProducts(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAll(pageable).map(mapper::mapToProductReadOnlyDTO);
    }


/**
 * Retrieves filtered products.
 * <p>
 * This method applies filters to the product query and returns a list of products.
 * </p>
 *
 * @param filters the {@link ProductFilters} containing the criteria for filtering.
 * @return a {@link List} of {@link ProductReadOnlyDTO}.
 */
    @org.springframework.transaction.annotation.Transactional
    public List<ProductReadOnlyDTO> getProductFiltered(ProductFilters filters) {
        return productRepository.findAll(getSpecsFromFilters(filters))
                .stream().map(mapper::mapToProductReadOnlyDTO).toList();
    }




/**
 * Retrieves a product by its ID.
 * <p>
 * This method fetches a product using its unique identifier and maps it to a {@link ProductReadOnlyDTO}.
 * </p>
 *
 * @param id the ID of the product.
 * @return a {@link ProductReadOnlyDTO} representing the product.
 * @throws AppObjectNotFoundException if the product with the given ID is not found.
 */
    @Override
    public ProductReadOnlyDTO getProductById(Long id) throws AppObjectNotFoundException {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Product with ID: " + id + " not found"));

        return mapper.mapToProductReadOnlyDTO(product);
    }

/**
 * Retrieves a list of all products.
 * <p>
 * This method fetches all products from the database and maps them to {@link ProductReadOnlyDTO}.
 * </p>
 *
 * @return a {@link List} of {@link ProductReadOnlyDTO}.
 * @throws AppObjectNotFoundException if no products are found.
 */
    @Override
    public List<ProductReadOnlyDTO> getAllProductsList() throws AppObjectNotFoundException {
        LOGGER.info("Getting all products");
        List<Product> products = productRepository.findAll();
        List<ProductReadOnlyDTO> productsDTO = new ArrayList<>();
        productsDTO = productRepository.findAll().stream().map(mapper::mapToProductReadOnlyDTO).toList();
        return productsDTO;
    }


/**
 * Builds a dynamic query specification based on provided filters.
 * <p>
 * This method constructs a {@link Specification} for querying products using the criteria provided
 * in the {@link ProductFilters}.
 * </p>
 *
 * @param filters the {@link ProductFilters} containing the filtering criteria.
 * @return a {@link Specification} for querying products.
 */
    private Specification<Product> getSpecsFromFilters(ProductFilters filters) {
        return Specification
                .where(ProductSpecification.trStringFieldLike("uuid", filters.getUuid()))
                .and(ProductSpecification.trStringFieldLike("id",filters.getId()))
                .and(ProductSpecification.trStringFieldLike("sku",filters.getSku()))
                .and(ProductSpecification.trProductByBrand(filters.getBrand()))
                .and(ProductSpecification.trProductByCategory(filters.getCategory()))
                .and(ProductSpecification.trProductIsActive(filters.getIsActive()))
                .and(ProductSpecification.trProductIsInStock(filters.getIsInStock()));

    }
}
