package com.urssu.bum.incubating.model.user

import com.urssu.bum.incubating.dto.model.user.RoleDto
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import org.springframework.security.core.GrantedAuthority
import javax.persistence.*

@Entity
@Table(name="roles")
class Role(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
        @Column(name = "name", unique = true, nullable = false)val name: String,
        @ManyToMany(fetch = FetchType.EAGER) @Cascade(CascadeType.SAVE_UPDATE) val permissions: List<Permission> = ArrayList()
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
