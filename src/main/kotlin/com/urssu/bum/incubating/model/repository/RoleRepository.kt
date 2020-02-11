package com.urssu.bum.incubating.model.repository

import com.urssu.bum.incubating.model.entity.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, String> {
    fun findByName(name: String): Role
}