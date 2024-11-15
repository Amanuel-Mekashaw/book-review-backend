package com.project.bookreviewapp.mapper;

import com.project.bookreviewapp.dto.AuthorDetailDTO;
import com.project.bookreviewapp.entity.AuthorDetail;

public class AuthorDetailMapper {

    public static AuthorDetail authorDetailDtoToAuthorDetail(AuthorDetailDTO authorDetailDTO) {
        return AuthorDetail.builder()
                .id(authorDetailDTO.getId())
                .biography(authorDetailDTO.getBiography())
                .profilePicture(authorDetailDTO.getProfilePicture())
                .socialLinks(authorDetailDTO.getSocialLinks())
                .user(null)
                .build();
    }

    public static AuthorDetailDTO authorDetailToAuthorDetailDto(AuthorDetail authorDetail) {
        return AuthorDetailDTO.builder()
                .id(authorDetail.getId())
                .biography(authorDetail.getBiography())
                .profilePicture(authorDetail.getProfilePicture())
                .socialLinks(authorDetail.getSocialLinks())
                .userId(null)
                .build();
    }

}
