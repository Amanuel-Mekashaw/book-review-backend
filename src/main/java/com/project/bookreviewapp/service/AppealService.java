package com.project.bookreviewapp.service;

import java.util.List;
import java.util.Optional;

import com.project.bookreviewapp.entity.Appeal;

public interface AppealService {
    public Appeal processAppeal(Long appealId, Appeal.AppealState newState, Long reviewerId, String reviewerMessage)
            throws Exception;

    // Submit a new appeal
    public Appeal createAppeal(String email, String appealMessage);

    public List<Appeal> getAppealsByState(Appeal.AppealState state);

    public Optional<Appeal> getAppealByEmail(String email);
}
