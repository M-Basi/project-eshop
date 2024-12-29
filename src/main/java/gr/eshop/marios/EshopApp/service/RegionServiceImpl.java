package gr.eshop.marios.EshopApp.service;


import gr.eshop.marios.EshopApp.model.static_data.Region;
import gr.eshop.marios.EshopApp.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl {

    private final RegionRepository regionRepository;


    public List<Region> getAllRegions() {
        return regionRepository.findAll(Sort.by("name"))
                .stream().toList();

    }
}
