package com.urssu.bum.incubating.repository

import com.urssu.bum.incubating.model.User
import reactor.core.publisher.Mono

interface UserRxRepository {
    fun existsByUsername(username: String): Mono<Boolean>
    fun findByUsername(username: String): Mono<User>
    fun save(user: User): Mono<Unit>
}