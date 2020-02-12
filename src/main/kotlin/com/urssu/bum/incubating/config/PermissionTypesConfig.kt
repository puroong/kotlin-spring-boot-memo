package com.urssu.bum.incubating.config

import com.urssu.bum.incubating.repository.PermissionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PermissionTypeConstants @Autowired constructor(
        private var permissionRepository: PermissionRepository
)