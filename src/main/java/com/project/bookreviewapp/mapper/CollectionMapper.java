package com.project.bookreviewapp.mapper;

import com.project.bookreviewapp.dto.CollectionDTO;
import com.project.bookreviewapp.entity.Collection;

public class CollectionMapper {

    public static Collection collectionDTOToCollection(CollectionDTO collectionDTO) {
        return Collection.builder().id(collectionDTO.getId()).name(collectionDTO.getName())
                .description(collectionDTO.getDescription()).createdAt(collectionDTO.getCreatedAt()).user(null)
                .books(null).updatedAt(collectionDTO.getUpdatedAt()).build();
    }

    public static CollectionDTO collectionToCollectionDTO(Collection collection) {
        return CollectionDTO.builder().id(collection.getId()).name(collection.getName())
                .description(collection.getDescription()).createdAt(collection.getCreatedAt())
                .updatedAt(collection.getUpdatedAt()).build();
    }
}
