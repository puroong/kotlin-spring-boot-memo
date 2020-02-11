package com.urssu.bum.incubating.model.repository

import com.urssu.bum.incubating.model.entity.Permission
import org.springframework.data.jpa.repository.JpaRepository

interface PermissionRepository : JpaRepository<Permission, Long>
