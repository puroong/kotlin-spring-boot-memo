package com.urssu.bum.incubating.dto

import com.urssu.bum.incubating.repository.PermissionRepository
import org.springframework.beans.factory.annotation.Autowired

object PermissionTypes {
    @Autowired
    private lateinit var permissionRepository: PermissionRepository
}