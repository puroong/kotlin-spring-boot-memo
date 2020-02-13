package com.urssu.bum.incubating.repository.role

import com.urssu.bum.incubating.exception.RoleNotFoundException
import com.urssu.bum.incubating.model.user.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Repository
class RoleRxRepositoryImpl @Autowired constructor(
        private val roleRepository: RoleRepository
): RoleRxRepository{
    override fun existsByName(name: String): Mono<Boolean> {
        return Mono.fromCallable { roleRepository.existsByName(name) }
                .subscribeOn(Schedulers.elastic())
    }

    override fun findByName(name: String): Mono<Role> {
        return Mono.fromCallable { roleRepository.findByName(name) }
                .subscribeOn(Schedulers.elastic())
    }

    override fun findByNameIfExist(name: String): Mono<Role> {
        return existsByName(name)
                .flatMap { roleExist ->
                    if(!roleExist) throw RoleNotFoundException()
                    findByName(name)
                }
    }
}