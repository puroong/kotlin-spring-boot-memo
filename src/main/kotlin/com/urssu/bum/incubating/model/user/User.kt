package com.urssu.bum.incubating.model.user

import com.urssu.bum.incubating.dto.model.user.UserDto
import com.urssu.bum.incubating.model.user.Role
import org.springframework.security.core.userdetails.User
import javax.persistence.*

@Entity
@Table(name="users")
class User(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
        @Column(name = "username", unique = true, nullable = false) var username: String,
        @Column(name = "password", nullable = false) var password: String,
        @Column(name = "is_active", nullable = false) var isActive: Boolean,
        @ManyToOne var role: Role
        ) {
    fun toUserDetails(): User {
        return User(username, password, role.permissions)
    }

    fun update(
            password: String = this.password,
            isActive: Boolean = this.isActive,
            role: Role = this.role
    ) {
        this.password = password
        this.isActive = isActive
        this.role = role
    }
}