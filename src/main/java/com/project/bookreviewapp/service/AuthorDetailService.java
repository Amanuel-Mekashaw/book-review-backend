package com.project.bookreviewapp.service;

import java.util.Optional;

import com.project.bookreviewapp.entity.AuthorDetail;

public interface AuthorDetailService {
    Optional<AuthorDetail> getAuthorDetail(Long id);

    AuthorDetail createAuthorDetail(AuthorDetail authorDetail);

    void deleteAuthorDetail(Long id);

    boolean isAuthorDetailExist(AuthorDetail authorDetail);
}
