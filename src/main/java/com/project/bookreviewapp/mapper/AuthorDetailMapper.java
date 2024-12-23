package com.project.bookreviewapp.mapper;

import org.springframework.beans.BeanUtils;

import com.project.bookreviewapp.dto.AuthorDetailDTO;
import com.project.bookreviewapp.entity.AuthorDetail;
import com.project.bookreviewapp.entity.User;

public class AuthorDetailMapper {

    public static AuthorDetail authorDetailDtoToAuthorDetail(AuthorDetailDTO authorDetailDTO) {
        AuthorDetail authorDetail = new AuthorDetail();

        BeanUtils.copyProperties(authorDetailDTO, authorDetail);

        if (authorDetailDTO.getUserId() != 0) {
            User user = new User();
            user.setId(authorDetailDTO.getUserId());
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
