package com.urssu.bum.incubating.service

import com.urssu.bum.incubating.dto.model.user.UserDto
import com.urssu.bum.incubating.exception.UserNotFoundException
import com.urssu.bum.incubating.repository.UserRxRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService @Autowired constructor(
        private val userRxRepository: UserRxRepository
) {
    fun getUser(username: String): Mono<UserDto> {
        return userRxRepository.findByUsernameAndIsActive(username, true)
                .map { it.toUserDto() }
                .switchIfEmpty(Mono.error(UserNotFoundException()))
    }

    // TODO: flatMap vs map
    fun disableUser(username: String): Mono<Unit> {
        return userRxRepository.findByUsernameAndIsActive(username, true)
                .flatMap {
                    // TODO: 깔끔하게 Update 하는 방법
                    it.isActive = false

                    userRxRepository.save(it)
                }
    }
}
