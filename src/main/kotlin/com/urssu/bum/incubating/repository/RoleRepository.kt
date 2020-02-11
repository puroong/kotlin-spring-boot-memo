package com.urssu.bum.incubating.repository

import com.urssu.bum.incubating.model.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, String> {
    fun findByName(name: String): Role
}