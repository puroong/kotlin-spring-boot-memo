package com.urssu.bum.incubating.repository.permission

import com.urssu.bum.incubating.model.user.Permission
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PermissionRepository : JpaRepository<Permission, Long> {
    fun findByName(name: String): Permission
}
