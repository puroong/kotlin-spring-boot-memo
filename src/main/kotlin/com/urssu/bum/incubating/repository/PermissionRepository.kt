package com.urssu.bum.incubating.repository

import com.urssu.bum.incubating.model.Permission
import org.springframework.data.jpa.repository.JpaRepository

interface PermissionRepository : JpaRepository<Permission, Long>
