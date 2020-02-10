package com.urssu.bum.incubating.model.repository

import com.urssu.bum.incubating.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun existsByName(name: String): Boolean
}