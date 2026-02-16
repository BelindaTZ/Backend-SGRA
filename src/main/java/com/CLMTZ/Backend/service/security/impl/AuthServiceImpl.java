package com.CLMTZ.Backend.service.security.impl;

import com.CLMTZ.Backend.dto.security.LoginRequestDTO;
import com.CLMTZ.Backend.dto.security.LoginResponseDTO;
import com.CLMTZ.Backend.dto.security.ServerCredentialDTO;
import com.CLMTZ.Backend.dto.security.session.UserContext;
import com.CLMTZ.Backend.model.general.User;
import com.CLMTZ.Backend.model.security.Access;
import com.CLMTZ.Backend.model.security.UsersRoles;
import com.CLMTZ.Backend.repository.security.IAccessRepository;
import com.CLMTZ.Backend.repository.security.IServerCredentialRepository;
import com.CLMTZ.Backend.repository.security.IUsersRolesRepository;
import com.CLMTZ.Backend.service.security.IAuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final String SESSION_CTX_KEY = "CTX";

    private final IAccessRepository accessRepository;
    private final IUsersRolesRepository usersRolesRepository;
    private final IServerCredentialRepository serverCredentialRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${sgra.master-key}")
    private String masterKey;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request, HttpSession session) {
        log.info("Intento de login para usuario: {}", request.getUsername());

        // 1. Buscar acceso por username
        Optional<Access> accessOpt = accessRepository.findByUsername(request.getUsername());
        if (accessOpt.isEmpty()) {
            log.warn("Usuario no encontrado: {}", request.getUsername());
            throw new RuntimeException("Credenciales inválidas");
        }

        Access access = accessOpt.get();

        // 2. Verificar que la cuenta esté activa
        if (!Boolean.TRUE.equals(access.getState())) {
            log.warn("Cuenta desactivada para usuario: {}", request.getUsername());
            throw new RuntimeException("La cuenta está desactivada");
        }

        // 3. Verificar password con BCrypt
        if (!passwordEncoder.matches(request.getPassword(), access.getPassword())) {
            log.warn("Contraseña incorrecta para usuario: {}", request.getUsername());
            throw new RuntimeException("Credenciales inválidas");
        }

        // 4. Obtener datos del usuario
        User user = access.getUser();
        if (user == null) {
            log.error("Usuario asociado no encontrado para acceso: {}", access.getAccessId());
            throw new RuntimeException("Error en la configuración del usuario");
        }

        // 5. Obtener roles del usuario
        List<UsersRoles> userRoles = usersRolesRepository.findActiveRolesByUserId(user.getUserId());
        List<String> roles = userRoles.stream()
                .map(ur -> ur.getRoleId().getRole())
                .collect(Collectors.toList());

        if (roles.isEmpty()) {
            log.warn("Usuario sin roles asignados: {}", request.getUsername());
            throw new RuntimeException("El usuario no tiene roles asignados");
        }

        // 6. Obtener credenciales del servidor (LOGIN SERVER)
        boolean serverSynced = false;
        String dbUser = null;

        if (masterKey != null && !masterKey.isEmpty()) {
            Optional<ServerCredentialDTO> serverCredOpt = serverCredentialRepository.getServerCredential(user.getUserId(), masterKey);
            if (serverCredOpt.isPresent()) {
                ServerCredentialDTO serverCred = serverCredOpt.get();
                serverSynced = true;
                dbUser = serverCred.getDbUser();
                log.info("Credenciales de servidor sincronizadas para usuario: {}", request.getUsername());
            } else {
                log.warn("Credenciales de servidor no sincronizadas para usuario: {}", request.getUsername());
                throw new RuntimeException("Credenciales de servidor no sincronizadas. Contacte al administrador.");
            }
        } else {
            log.warn("Master key no configurada. Las credenciales de servidor no pueden ser verificadas.");
            throw new RuntimeException("Error de configuración del sistema");
        }

        // 7. Crear UserContext y guardar en sesión
        UserContext ctx = new UserContext();
        ctx.setUserId(user.getUserId());
        ctx.setUsername(access.getUsername());
        ctx.setFirstName(user.getFirstName());
        ctx.setLastName(user.getLastName());
        ctx.setEmail(user.getEmail());
        ctx.setRoles(roles);
        ctx.setServerSynced(serverSynced);
        ctx.setDbUser(dbUser);

        session.setAttribute(SESSION_CTX_KEY, ctx);
        log.info("Login exitoso para usuario: {}. Roles: {}", request.getUsername(), roles);

        // 8. Retornar respuesta (sin dbPassword)
        return new LoginResponseDTO(
                user.getUserId(),
                access.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                roles,
                serverSynced
        );
    }

    @Override
    public LoginResponseDTO getCurrentUser(HttpSession session) {
        UserContext ctx = getUserContext(session);
        if (ctx == null) {
            return null;
        }
        return new LoginResponseDTO(
                ctx.getUserId(),
                ctx.getUsername(),
                ctx.getFirstName(),
                ctx.getLastName(),
                ctx.getEmail(),
                ctx.getRoles(),
                ctx.isServerSynced()
        );
    }

    @Override
    public void logout(HttpSession session) {
        UserContext ctx = getUserContext(session);
        if (ctx != null) {
            log.info("Logout para usuario: {}", ctx.getUsername());
        }
        session.invalidate();
    }

    @Override
    public UserContext getUserContext(HttpSession session) {
        Object ctx = session.getAttribute(SESSION_CTX_KEY);
        if (ctx instanceof UserContext) {
            return (UserContext) ctx;
        }
        return null;
    }
}

