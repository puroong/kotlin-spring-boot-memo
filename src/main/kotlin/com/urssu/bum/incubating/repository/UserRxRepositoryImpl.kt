package com.urssu.bum.incubating.repository

import com.urssu.bum.incubating.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Component
class UserRxRepositoryImpl @Autowired constructor(
        private var userRepository: UserRepository
) : UserRxRepository{
    override fun existsByUsername(username: String): Mono<Boolean> {
        return Mono.fromCallable { userRepository.existsByUsername(username) }
                .subscribeOn(Schedulers.elastic())
    }

    override fun findByUsername(username: String): Mono<User> {
        return Mono.fromCallable { userRepository.findByUsername(username) }
                .subscribeOn(Schedulers.elastic())
    }

    override fun save(user: User): Mono<Unit> {
        return Mono.fromCallable { userRepository.save(user) }
                .subscribeOn(Schedulers.elastic())
    }
}