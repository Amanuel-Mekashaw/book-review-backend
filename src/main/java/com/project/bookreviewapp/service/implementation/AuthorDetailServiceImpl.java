package com.project.bookreviewapp.service.implementation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bookreviewapp.dto.AuthorDetailDTO;
import com.project.bookreviewapp.entity.AuthorDetail;
import com.project.bookreviewapp.entity.User;
import com.project.bookreviewapp.mapper.AuthorDetailMapper;
import com.project.bookreviewapp.repository.AuthorDetailRepository;
import com.project.bookreviewapp.repository.UserRepository;
import com.project.bookreviewapp.service.AuthorDetailService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthorDetailServiceImpl implements AuthorDetailService {

    private final AuthorDetailRepository authorDetailRepository;
    private final UserRepository userRepository;

    private static final String STORAGE_DIRECTORY = "D:\\test\\profile_images";

    private static final long MAX_FILE_SIZE_MB = 4 * 1024 * 1024; // 4MB

    public AuthorDetailServiceImpl(AuthorDetailRepository authorDetailRepository, UserRepository userRepository) {
        this.authorDetailRepository = authorDetailRepository;
        this.userRepository = userRepository;
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
        return authorDetailRepository.findUserDetailByAuthorId(id);
    }

    @Override
    public void addNewUserDetail(AuthorDetailDTO authorDetailDTO, MultipartFile profilePhoto) throws RuntimeException {
        try {
            AuthorDetail authorDetail = AuthorDetailMapper.authorDetailDtoToAuthorDetail(authorDetailDTO,
                    userRepository);
            saveProfilePhoto(authorDetail, profilePhoto);
            authorDetailRepository.save(authorDetail);
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public void saveProfilePhoto(AuthorDetail authorDetail, MultipartFile profilePhoto) throws IOException {

        // check if photo is null
        if (profilePhoto == null || profilePhoto.isEmpty()) {
            throw new IllegalArgumentException("Profile Photo is missing or empty");
        }

        // check file size
        if (profilePhoto.getSize() > MAX_FILE_SIZE_MB) {
            throw new IllegalArgumentException("File size exceeds the 4MB limit");
        }

        String originalFilename = profilePhoto.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("Profile Photo filename is missing");
        }

        String extension = FilenameUtils.getExtension(originalFilename);
        System.out.println("\n\n\nExension" + extension);

        String filename = UUID.randomUUID().toString() + "." + extension;

        Path filePath = Paths.get(STORAGE_DIRECTORY, filename);
        Files.createDirectories(filePath.getParent());
        Files.copy(profilePhoto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("\n\n\nprofile photo " + filename + "\n\n\n");
        authorDetail.setProfilePicture(filename);

    }

}
