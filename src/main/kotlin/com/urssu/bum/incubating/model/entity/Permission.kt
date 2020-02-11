package com.urssu.bum.incubating.model.entity

import org.springframework.security.core.GrantedAuthority
import javax.persistence.*

@Entity
@Table(name="permissions")
class Permission(
        @Id @GeneratedValue val id: Long,
        @Column(unique = true, nullable = false) val name: String
) : GrantedAuthority {
    override fun getAuthority(): String {
        return name
    }
}
