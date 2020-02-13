package com.urssu.bum.incubating.dto.model.user

import com.urssu.bum.incubating.model.user.Role

class RoleDto(
        val name: String
) {
    constructor(role: Role): this(
            name = role.name
    )
}
