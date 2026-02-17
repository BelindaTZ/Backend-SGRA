package com.CLMTZ.Backend.controller.security;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.CLMTZ.Backend.dto.security.SpResponseDTO;
import com.CLMTZ.Backend.dto.security.UserManagementDTO;
import com.CLMTZ.Backend.service.security.IUserManagementService;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/security/user-managements")
@CrossOrigin(origins = "*")
public class UserManagementController {

    private final IUserManagementService userManagementser;

    @GetMapping
    public ResponseEntity<List<UserManagementDTO>> findAll() { return ResponseEntity.ok(userManagementser.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<UserManagementDTO> findById(@PathVariable Integer id) { return ResponseEntity.ok(userManagementser.findById(id)); }

    @PostMapping
    public ResponseEntity<UserManagementDTO> save(@RequestBody UserManagementDTO dto) { return new ResponseEntity<>(userManagementser.save(dto), HttpStatus.CREATED); }

    @PutMapping("/{id}")
    public ResponseEntity<UserManagementDTO> update(@PathVariable Integer id, @RequestBody UserManagementDTO dto) { return ResponseEntity.ok(userManagementser.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) { userManagementser.deleteById(id); return ResponseEntity.noContent().build(); }

    @PostMapping("/create-user")
    public ResponseEntity<SpResponseDTO> createUser(@RequestBody UserManagementDTO requestUser) {    
        
        SpResponseDTO request = userManagementser.createGUser(requestUser);

        return ResponseEntity.ok(request);
    }
    
    @PutMapping("/update-user")
    public ResponseEntity<SpResponseDTO> updateGUser(@RequestBody UserManagementDTO requestUser) {    
        
        SpResponseDTO request = userManagementser.updateGUser(requestUser);

        return ResponseEntity.ok(request);
    }
}
