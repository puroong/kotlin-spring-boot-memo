package com.urssu.bum.incubating.repository.role

import com.urssu.bum.incubating.model.user.Role
import reactor.core.publisher.Mono

interface RoleRxRepository {
    fun existsByName(name: String): Mono<Boolean>

    fun findByName(name: String): Mono<Role>
    fun findByNameIfExist(name: String): Mono<Role>
}