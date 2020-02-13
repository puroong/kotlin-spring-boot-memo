package com.urssu.bum.incubating.dto.model.user

import com.urssu.bum.incubating.model.user.User

class UserDto (
        val username: String,
        val isActive: Boolean,
        val role: RoleDto
) {
    constructor(user: User): this(
            username = user.username,
            isActive = user.isActive,
            role = RoleDto(user.role)
    )
}