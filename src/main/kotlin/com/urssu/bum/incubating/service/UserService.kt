package com.urssu.bum.incubating.service

import com.urssu.bum.incubating.dto.model.user.UserDto
import com.urssu.bum.incubating.repository.UserRxRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService @Autowired constructor(
        private val userRxRepository: UserRxRepository
) {
    fun getUser(username: String): Mono<UserDto> {
        return userRxRepository.findByUsername(username)
                .map { it.toUserDto() }
    }
}
