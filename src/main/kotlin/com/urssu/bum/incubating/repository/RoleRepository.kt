package com.urssu.bum.incubating.repository

import com.urssu.bum.incubating.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, String> {
    fun findByName(name: String): Role
}