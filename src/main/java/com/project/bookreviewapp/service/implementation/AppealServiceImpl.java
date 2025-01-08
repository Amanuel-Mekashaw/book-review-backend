package com.project.bookreviewapp.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.bookreviewapp.entity.Appeal;
import com.project.bookreviewapp.entity.User;
import com.project.bookreviewapp.entity.Appeal.AppealState;
import com.project.bookreviewapp.entity.User.Role;
import com.project.bookreviewapp.entity.User.Status;
import com.project.bookreviewapp.repository.AppealRepository;
import com.project.bookreviewapp.repository.UserRepository;
import com.project.bookreviewapp.service.AppealService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppealServiceImpl implements AppealService {

    private final AppealRepository appealRepository;
    private final UserRepository userRepository;

    // process appeal
    @Override
    public Appeal processAppeal(Long appealId, Appeal.AppealState newState, Long reviewerId, String reviewerMessage) {
        // Fetch the admin user
        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));

        if (reviewer.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only admins can process appeals");
        }

        // Fetch the appeal
        Appeal appeal = appealRepository.findById(appealId)
                .orElseThrow(() -> new RuntimeException("Appeal not found"));

        if (newState == Appeal.AppealState.APPROVED) {
            // Fetch the user associated with the appeal
            User user = userRepository.findByEmail(appeal.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (user.getStatus() == Status.INACTIVE) {
                user.setStatus(Status.ACTIVE);
                userRepository.save(user);
                appeal.setAppealState(Appeal.AppealState.APPROVED);
                appeal.setReviewerMessage(reviewerMessage);

            } else {
                throw new RuntimeException("User is not inactive");
            }
        } else if (newState == Appeal.AppealState.REJECTED) {
            appeal.setAppealState(Appeal.AppealState.REJECTED);
            appeal.setReviewerMessage(reviewerMessage);
        }

        appeal.setReviewerId(reviewer.getId());
        appealRepository.save(appeal);
        return appeal;
    }

    @Override
    public Appeal createAppeal(String email, String appealMessage) {

        Appeal appeal = Appeal.builder()
                .email(email)
                .appealMessage(appealMessage)
                .appealState(AppealState.PENDING)
                .build();
        appealRepository.save(appeal);
        return appeal;
    }

    // Get appeals based on state
    @Override
    public List<Appeal> getAppealsByState(Appeal.AppealState state) {
        return appealRepository.findByAppealState(state);
    }

    // Get an appeal by email and state (for a user to track the status)
    @Override
    public Optional<Appeal> getAppealByEmail(String email) {
        return appealRepository.findByEmail(email);
    }

}
