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

/**
 * Retrieves all regions sorted by name.
 * <p>
 * This method fetches all {@link Region} entities from the database and returns them as a sorted list.
 * The regions are sorted in ascending order by their name.
 * </p>
 *
 * @return a {@link List} of {@link Region} entities sorted by name.
 */
    public List<Region> getAllRegions() {
        return regionRepository.findAll(Sort.by("name"))
                .stream().toList();

    }
}
