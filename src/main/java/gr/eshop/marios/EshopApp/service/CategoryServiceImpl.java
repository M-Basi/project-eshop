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

    public List<CategoryReadOnlyDTO> getAllCategories()  {
        return categoryRepository.findAll(Sort.by("categoryName"))
                .stream().map(mapper::mapCategoryReadOnlyDTO).toList();

    }
}