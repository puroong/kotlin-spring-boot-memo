package com.urssu.bum.incubating.repository

import com.urssu.bum.incubating.model.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun existsByUsername(username: String): Boolean
    fun existsByUsernameAndIsActive(username: String, isActive: Boolean): Boolean
    fun findByUsername(username: String): User
    fun findByUsernameAndIsActive(username: String, isActive: Boolean): User
    fun save(user: User)
}