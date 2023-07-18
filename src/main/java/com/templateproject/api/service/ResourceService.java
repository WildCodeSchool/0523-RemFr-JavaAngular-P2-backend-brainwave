package com.templateproject.api.service;


import com.templateproject.api.DTO.ResourceDTO;
import com.templateproject.api.DtoMapper.ResourceDTOMapper;
import com.templateproject.api.entity.Resource;
import com.templateproject.api.repository.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResourceService {
    private static ResourceRepository resourceRepository;
    private static ResourceDTOMapper resourceDTOMapper;

    public ResourceService(ResourceRepository resourceRepository,
                           ResourceDTOMapper resourceDTOMapper) {
        this.resourceRepository = resourceRepository;
        this.resourceDTOMapper = resourceDTOMapper;
    }

    public static Optional<ResourceDTO> findById(UUID id) {
        return resourceRepository.findById(id)
                .map(resourceDTOMapper::convertToDTO);

    }

    public List<ResourceDTO> findAllResources() {
        List<Resource> resources = resourceRepository.findAll();

        return resources.stream()
                .map(resourceDTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public static Optional<ResourceDTO> findResourceById(UUID id) {
        return resourceRepository.findById(id)
                .map(resourceDTOMapper::convertToDTO);

    }

    public List<ResourceDTO> findByPromotionId(UUID promoId) {
        List<Resource> resources = resourceRepository.findByPromotionId(promoId);
        return resources.stream()
                .map(resourceDTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ResourceDTO> findByAuthor(UUID userId) {
        List<Resource> resources = resourceRepository.findByAuthorId(userId);
        return resources.stream()
                .map(resourceDTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public void setResourceRepository(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public void setResourceDTOMapper(ResourceDTOMapper resourceDTOMapper) {
        this.resourceDTOMapper = resourceDTOMapper;
    }

}
