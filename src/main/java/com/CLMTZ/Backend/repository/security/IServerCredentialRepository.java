package com.CLMTZ.Backend.repository.security;

import com.CLMTZ.Backend.dto.security.ServerCredentialDTO;
import java.util.Optional;

public interface IServerCredentialRepository {
    Optional<ServerCredentialDTO> getServerCredential(Integer userId, String masterKey);
}
