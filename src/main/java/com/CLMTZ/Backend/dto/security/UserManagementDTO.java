package com.CLMTZ.Backend.dto.security;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserManagementDTO {
    private Integer userGId;
    private String user;
    private String password;
    private Boolean state;
}
