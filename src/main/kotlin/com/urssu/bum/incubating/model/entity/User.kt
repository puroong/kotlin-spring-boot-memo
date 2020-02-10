package com.urssu.bum.incubating.model.entity

import org.springframework.security.core.userdetails.User
import javax.persistence.*

@Entity
@Table(name="users")
class User(
        @Id @GeneratedValue var id: Long? = null,
        @Column(unique=true) var username: String,
        var password: String
) {
    fun toUserDetails(): User {
        return User(username, password, ArrayList())
    }
}