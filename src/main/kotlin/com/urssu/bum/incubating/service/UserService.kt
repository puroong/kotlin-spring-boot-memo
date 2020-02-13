package com.urssu.bum.incubating.service

import com.urssu.bum.incubating.controller.v1.request.UserRoleUpdateRequest
import com.urssu.bum.incubating.dto.model.user.UserDto
import com.urssu.bum.incubating.dto.response.SigninResponseDto
import com.urssu.bum.incubating.exception.RoleNotFoundException
import com.urssu.bum.incubating.exception.UserNotFoundException
import com.urssu.bum.incubating.model.Role
import com.urssu.bum.incubating.repository.RoleRepository
import com.urssu.bum.incubating.repository.UserRxRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService @Autowired constructor(
        private val userRxRepository: UserRxRepository,
        private val roleRepository: RoleRepository
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

    fun updateUserRole(username: String, userRoleUpdateRequest: UserRoleUpdateRequest): Mono<Unit> {
        return userRxRepository.findByUsernameAndIsActive(username, true)
                .flatMap {
                    // ToDO: role 없을 때 어떤 exception 나는지 모르겠음
                    // TODO: switchIfEmpty 어떨 때 되는지
                    val role = userRoleUpdateRequest.toRole()
                    it.role = role

                    userRxRepository.save(it)
                }
                .switchIfEmpty(Mono.error(RoleNotFoundException()))
    }

    fun UserRoleUpdateRequest.toRole(): Role {
        return roleRepository.findByName(roleName)
    }
}
