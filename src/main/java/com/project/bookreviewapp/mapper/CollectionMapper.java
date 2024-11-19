package com.project.bookreviewapp.mapper;

import org.springframework.beans.BeanUtils;

import com.project.bookreviewapp.dto.CollectionDTO;
import com.project.bookreviewapp.entity.Collection;
import com.project.bookreviewapp.entity.User;

public class CollectionMapper {

    public static Collection collectionDTOToCollection(CollectionDTO collectionDTO) {
        Collection collection = new Collection();
        BeanUtils.copyProperties(collectionDTO, collection);

        if (collectionDTO.getUserId() != 0) {
            User user = new User();
            user.setId(collectionDTO.getUserId());
            collection.setUser(user);
        }
        return collection;

    }

    public static CollectionDTO collectionToCollectionDTO(Collection collection) {
        CollectionDTO collectionDto = new CollectionDTO();

        BeanUtils.copyProperties(collection, collectionDto);

        if (collection.getUser().getId() != 0) {
            Long userId = collection.getUser().getId();
            collectionDto.setUserId(userId);
        }
        return collectionDto;
    }
}
