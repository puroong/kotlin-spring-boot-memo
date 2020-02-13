package com.urssu.bum.incubating.repository

import com.urssu.bum.incubating.exception.UserNotFoundException
import com.urssu.bum.incubating.model.user.User
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

    override fun existsByUsernameAndIsActive(username: String, isActive: Boolean): Mono<Boolean> {
        return Mono.fromCallable { userRepository.existsByUsernameAndIsActive(username, isActive) }
                .subscribeOn(Schedulers.elastic())
    }
    override fun findByUsername(username: String): Mono<User> {
        return existsByUsername(username)
                .map { userExists ->
                    if(userExists) userRepository.findByUsername(username)
                    else throw UserNotFoundException()
                }
    }

    override fun findByUsernameAndIsActive(username: String, isActive: Boolean): Mono<User> {
        return existsByUsernameAndIsActive(username, isActive)
                .map { userExists ->
                    if(userExists) userRepository.findByUsernameAndIsActive(username, isActive)
                    else throw UserNotFoundException()
                }
    }

    override fun save(user: User): Mono<Unit> {
        return Mono.fromCallable { userRepository.save(user) }
                .subscribeOn(Schedulers.elastic())
    }
}