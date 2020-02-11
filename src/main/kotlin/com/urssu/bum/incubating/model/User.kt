package com.urssu.bum.incubating.model

import org.springframework.security.core.userdetails.User
import javax.persistence.*

@Entity
@Table(name="users")
class User(
        @Id @GeneratedValue var id: Long? = null,
        @Column(unique = true, nullable = false) var username: String,
        @Column(nullable = false) var password: String,
        @OneToOne val role: Role
        ) {
    fun toUserDetails(): User {
        return User(username, password, role.getPermissions())
    }
}