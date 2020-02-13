package com.urssu.bum.incubating.repository.role

import com.urssu.bum.incubating.model.user.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, String> {
    fun existsByName(name: String): Boolean
    fun findByName(name: String): Role
}