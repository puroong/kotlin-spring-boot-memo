package com.urssu.bum.incubating.repository.user

import com.urssu.bum.incubating.model.user.User
import reactor.core.publisher.Mono

interface UserRxRepository {
    fun save(user: User): Mono<Unit>

    fun existsByUsernameAndIsActive(username: String, isActive: Boolean): Mono<Boolean>
    fun existsByUsername(username: String): Mono<Boolean>

    fun findByUsername(username: String): Mono<User>
    fun findByUsernameAndIsActive(username: String, isActive: Boolean): Mono<User>

    fun findByUsernameIfExist(username: String): Mono<User>
    fun findByUsernameAndIsActiveIfExist(username: String, isActive: Boolean): Mono<User>
}