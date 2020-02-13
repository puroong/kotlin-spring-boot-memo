package com.urssu.bum.incubating.repository.user

import com.urssu.bum.incubating.exception.UserNotFoundException
import com.urssu.bum.incubating.model.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Repository
class UserRxRepositoryImpl @Autowired constructor(
        private val userRepository: UserRepository
) : UserRxRepository {
    override fun existsByUsername(username: String): Mono<Boolean> {
        return Mono.fromCallable { userRepository.existsByUsername(username) }
                .subscribeOn(Schedulers.elastic())
    }

    override fun existsByUsernameAndIsActive(username: String, isActive: Boolean): Mono<Boolean> {
        return Mono.fromCallable { userRepository.existsByUsernameAndIsActive(username, isActive) }
                .subscribeOn(Schedulers.elastic())
    }
    override fun findByUsername(username: String): Mono<User> {
        return Mono.fromCallable { userRepository.findByUsername(username) }
                .subscribeOn(Schedulers.elastic())
    }

    override fun findByUsernameAndIsActive(username: String, isActive: Boolean): Mono<User> {
        return Mono.fromCallable { userRepository.findByUsernameAndIsActive(username, isActive) }
                .subscribeOn(Schedulers.elastic())
    }

    override fun findByUsernameIfExist(username: String): Mono<User> {
        return existsByUsername(username)
                .map { userExist ->
                    if(!userExist) throw UserNotFoundException()
                }
                .flatMap {
                    findByUsername(username)
                }
    }

    override fun findByUsernameAndIsActiveIfExist(username: String, isActive: Boolean): Mono<User> {
        return existsByUsernameAndIsActive(username, isActive)
                .map {userExist ->
                    if(!userExist) throw UserNotFoundException()
                }
                .flatMap {
                    findByUsernameAndIsActive(username, isActive)
                }
    }

    override fun save(user: User): Mono<Unit> {
        return Mono.fromCallable { userRepository.save(user) }
                .subscribeOn(Schedulers.elastic())
    }
}