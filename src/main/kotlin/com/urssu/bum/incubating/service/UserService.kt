package com.urssu.bum.incubating.service

import com.urssu.bum.incubating.dto.request.UserRoleUpdateRequestDto
import com.urssu.bum.incubating.model.user.Role
import com.urssu.bum.incubating.model.user.User
import com.urssu.bum.incubating.repository.role.RoleRxRepository
import com.urssu.bum.incubating.repository.user.UserRxRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService @Autowired constructor(
        private val userRxRepository: UserRxRepository,
        private val roleRxRepository: RoleRxRepository
) {
    fun getUser(username: String): Mono<User> {
        return userRxRepository.findByUsernameAndIsActiveIfExist(username, true)
    }

    fun disableUser(username: String): Mono<Unit> {
        return userRxRepository.findByUsernameAndIsActiveIfExist(username, true)
                .map {
                    it.update(isActive = true)
                    it
                }
                .flatMap(userRxRepository::save)
    }

    fun updateUserRole(username: String, userRoleUpdateRequest: UserRoleUpdateRequestDto): Mono<Unit> {
        return userRxRepository.findByUsernameAndIsActiveIfExist(username, true)
                .flatMap { user ->
                    userRoleUpdateRequest.toRole()
                            .map {
                                user.update(
                                        role = it
                                )
                                user
                            }
                            .flatMap(userRxRepository::save)
                }
    }

    fun UserRoleUpdateRequestDto.toRole(): Mono<Role> {
        return roleRxRepository.findByNameIfExist(roleName)
    }
}
