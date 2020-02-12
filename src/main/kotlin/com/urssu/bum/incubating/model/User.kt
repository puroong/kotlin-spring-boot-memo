package com.urssu.bum.incubating.model

import com.urssu.bum.incubating.dto.model.user.UserDto
import org.springframework.security.core.userdetails.User
import javax.persistence.*

@Entity
@Table(name="users")
class User(
        @Id @GeneratedValue var id: Long? = null,
        @Column(unique = true, nullable = false) var username: String,
        @Column(nullable = false) var password: String,
        @ManyToOne val role: Role
        ) {
    fun toUserDetails(): User {
        return User(username, password, role.getPermissions())
    }

    fun toUserDto(): UserDto {
        return UserDto(
                username = username,
                role = role.toRoleDto()
        )
    }
}