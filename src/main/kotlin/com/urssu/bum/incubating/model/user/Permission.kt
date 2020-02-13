package com.urssu.bum.incubating.model.user

import com.urssu.bum.incubating.dto.model.user.PermissionDto
import org.springframework.security.core.GrantedAuthority
import javax.persistence.*

@Entity
@Table(name="permissions")
class Permission(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long,
        @Column(name = "name", unique = true, nullable = false) val name: String
) : GrantedAuthority {
    override fun getAuthority(): String {
        return name
    }
}