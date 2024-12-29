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

    public List<BrandReadOnlyDTO> getAllBrands() {
        return brandRepository.findAll(Sort.by("brandName"))
                .stream().map(mapper::mapBrandToReadOnlyDTO).toList();

    }
}
