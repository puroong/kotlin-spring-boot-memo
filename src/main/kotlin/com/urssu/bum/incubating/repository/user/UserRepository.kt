package com.urssu.bum.incubating.repository.user

import com.urssu.bum.incubating.model.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// TODO: @Repository가 없는데 UserRxRepositoryImpl에 어떻게 authowire 되는지 모르겠음
interface UserRepository : JpaRepository<User, Long> {
    fun save(user: User)

    fun existsByUsername(username: String): Boolean
    fun existsByUsernameAndIsActive(username: String, isActive: Boolean): Boolean

    fun findByUsername(username: String): User
    fun findByUsernameAndIsActive(username: String, isActive: Boolean): User
}