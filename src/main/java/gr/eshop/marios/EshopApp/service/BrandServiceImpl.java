package gr.eshop.marios.EshopApp.service;


import gr.eshop.marios.EshopApp.dto.BrandReadOnlyDTO;
import gr.eshop.marios.EshopApp.mapper.Mapper;
import gr.eshop.marios.EshopApp.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@RequiredArgsConstructor
public class BrandServiceImpl {

    private final BrandRepository brandRepository;
    private final Mapper mapper;

    /**
     * Retrieves all brands sorted by their name.
     * <p>
     * This method fetches all brand entities from the database, sorts them
     * by the "brandName" field, and maps them to read-only DTOs using the
     * {@link Mapper}.
     * </p>
     *
     * @return a list of {@link BrandReadOnlyDTO} representing all brands sorted by name.
     */
    public List<BrandReadOnlyDTO> getAllBrands() {
        return brandRepository.findAll(Sort.by("brandName"))
                .stream().map(mapper::mapBrandToReadOnlyDTO).toList();

    }
}
