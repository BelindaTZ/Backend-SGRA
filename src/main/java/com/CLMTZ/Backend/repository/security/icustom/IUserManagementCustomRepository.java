package com.CLMTZ.Backend.repository.security.icustom;

import java.time.LocalDate;
import java.util.List;

import com.CLMTZ.Backend.dto.security.Response.UserListManagementResponseDTO;

public interface IUserManagementCustomRepository {
        List<UserListManagementResponseDTO> listUsersManagement(String filterUser, LocalDate date, Boolean state);
}
