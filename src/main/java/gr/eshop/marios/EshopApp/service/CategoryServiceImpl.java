package gr.eshop.marios.EshopApp.service;


import gr.eshop.marios.EshopApp.dto.CategoryReadOnlyDTO;
import gr.eshop.marios.EshopApp.mapper.Mapper;
import gr.eshop.marios.EshopApp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl {

    private final CategoryRepository categoryRepository;
    private final Mapper mapper;

    /**
     * Retrieves all categories sorted by their name.
     * <p>
     * This method fetches all category entities from the database, sorts them
     * by the "categoryName" field, and maps them to read-only DTOs using the
     * {@link Mapper}.
     * </p>
     *
     * @return a list of {@link CategoryReadOnlyDTO} representing all categories sorted by name.
     */
    public List<CategoryReadOnlyDTO> getAllCategories()  {
        return categoryRepository.findAll(Sort.by("categoryName"))
                .stream().map(mapper::mapCategoryReadOnlyDTO).toList();

    }
}