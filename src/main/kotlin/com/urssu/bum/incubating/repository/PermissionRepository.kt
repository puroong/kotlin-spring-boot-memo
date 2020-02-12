package com.urssu.bum.incubating.repository

import com.urssu.bum.incubating.model.Permission
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PermissionRepository : JpaRepository<Permission, Long>
