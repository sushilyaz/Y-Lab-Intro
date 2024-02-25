package org.example.repository;

import org.example.dto.UserInfoDTO;

import java.util.List;

public interface AdminRepository {
    void addNewType(String newKey);
    List<UserInfoDTO> findUsersAndCR();
}
