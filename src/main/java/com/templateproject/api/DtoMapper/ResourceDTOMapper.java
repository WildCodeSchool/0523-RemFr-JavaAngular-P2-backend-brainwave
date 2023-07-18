package com.templateproject.api.DtoMapper;

import com.templateproject.api.DTO.ResourceDTO;
import com.templateproject.api.entity.Resource;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ResourceDTOMapper implements Function<Resource, ResourceDTO> {
    @Override
    public ResourceDTO apply(Resource resource) {
        return new ResourceDTO(
                resource.getId(),
                resource.getTitle(),
                resource.getContent(),
                resource.getLink(),
                resource.getCreationDate(),
                resource.getPromotion().getId(),
                resource.getAuthor() == null ? null : resource.getAuthor().getId()

        );
    }


    public ResourceDTO convertToDTO(Resource resource) {
        return apply(resource);
    }
}
