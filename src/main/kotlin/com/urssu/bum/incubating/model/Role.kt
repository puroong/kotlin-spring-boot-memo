package com.urssu.bum.incubating.model

import com.urssu.bum.incubating.dto.model.user.RoleDto
import org.springframework.security.core.GrantedAuthority
import javax.persistence.*

@Entity
@Table(name="roles")
class Role(
        @Id @GeneratedValue val id: Long? = null,
        @Column(unique = true, nullable = false)val name: String,
        // TODO: Lazy가 안되는 이유
        @OneToMany(fetch = FetchType.EAGER) val permissions: List<Permission> = ArrayList()
) : GrantedAuthority {
    override fun getAuthority(): String {
        return name
    }

    fun getPermissions(): Collection<GrantedAuthority> {
        return permissions
    }

    fun toRoleDto(): RoleDto {
        return RoleDto(
                name = name
        )
    }
}
