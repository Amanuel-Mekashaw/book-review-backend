package com.project.bookreviewapp.service.implementation;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.project.bookreviewapp.entity.AuthorDetail;
import com.project.bookreviewapp.entity.User;
import com.project.bookreviewapp.repository.AuthorDetailRepository;
import com.project.bookreviewapp.service.AuthorDetailService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthorDetailServiceImpl implements AuthorDetailService {

    private final AuthorDetailRepository authorDetailRepository;

    public AuthorDetailServiceImpl(AuthorDetailRepository authorDetailRepository) {
        this.authorDetailRepository = authorDetailRepository;
    }

    @Override
    public Optional<AuthorDetail> getAuthorDetail(Long id) {
        return authorDetailRepository.findById(id);
    }

    @Override
    public AuthorDetail createAuthorDetail(AuthorDetail authorDetail) {
        return authorDetailRepository.save(authorDetail);
    }

    @Override
    public void deleteAuthorDetail(Long id) {
        try {
            authorDetailRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            log.debug("No author detail by this id " + id, ex.getMessage());
        }
    }

    @Override
    public boolean isAuthorDetailExist(AuthorDetail authorDetail) {
        return authorDetailRepository.existsById(authorDetail.getId());
    }

    @Override
    public Optional<User> getUserByAuthorId(Long authorId) {
        return authorDetailRepository.findUserByAuthorId(authorId);
    }

    public Optional<AuthorDetail> getUserDetailByAuthorId(Long authorId) {
        return authorDetailRepository.findUserDetailByAuthorId(authorId);
    }

    @Override
    public Optional<AuthorDetail> findUserWithAuthorDetailsById(Long id) {
        return authorDetailRepository.findUserWithAuthorDetailsById(id);
    }

}
