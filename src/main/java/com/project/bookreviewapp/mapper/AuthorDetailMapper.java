package com.project.bookreviewapp.mapper;

import org.springframework.beans.BeanUtils;

import com.project.bookreviewapp.dto.AuthorDetailDTO;
import com.project.bookreviewapp.entity.AuthorDetail;
import com.project.bookreviewapp.entity.User;
import com.project.bookreviewapp.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

public class AuthorDetailMapper {

    // public static AuthorDetail authorDetailDtoToAuthorDetail(AuthorDetailDTO
    // authorDetailDTO) {
    // if (authorDetailDTO == null) {
    // throw new IllegalArgumentException("AuthorDetailDTO cannot be null");
    // }
    // AuthorDetail authorDetail = new AuthorDetail();

    // BeanUtils.copyProperties(authorDetailDTO, authorDetail);

    // // Set the user if the ID is valid
    // Long userId = authorDetailDTO.getUserId();
    // if (userId != null && userId > 0) {
    // User user = new User();
    // user.setId(userId);
    // authorDetail.setUser(user);
    // }

    // return authorDetail;
    // }

    public static AuthorDetail authorDetailDtoToAuthorDetail(AuthorDetailDTO authorDetailDTO,
            UserRepository userRepository) {

        if (authorDetailDTO == null) {
            throw new IllegalArgumentException("AuthorDetailDTO cannot be null");
        }

        AuthorDetail authorDetail = new AuthorDetail();
        BeanUtils.copyProperties(authorDetailDTO, authorDetail);

        // Fetch the User object from the repository if the ID is valid
        if (authorDetailDTO.getUserId() != null && authorDetailDTO.getUserId() != 0) {
            User user = userRepository.findById(authorDetailDTO.getUserId()).orElseThrow(
                    () -> new EntityNotFoundException("User not found with ID: " + authorDetailDTO.getUserId()));
            authorDetail.setUser(user);
        }

        return authorDetail;
    }

    public static AuthorDetailDTO authorDetailToAuthorDetailDto(AuthorDetail authorDetail) {
        AuthorDetailDTO authorDetailDTO = new AuthorDetailDTO();
        BeanUtils.copyProperties(authorDetail, authorDetailDTO);

        if (authorDetail.getUser().getId() != 0) {
            Long userId = authorDetail.getUser().getId();
            authorDetailDTO.setUserId(userId);
        }

        return authorDetailDTO;
    }

}
