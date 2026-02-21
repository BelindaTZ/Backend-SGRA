package com.CLMTZ.Backend.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CLMTZ.Backend.model.security.ModuleManagement;

public interface IModuleManagementRepository extends JpaRepository<ModuleManagement, Integer> {

}
