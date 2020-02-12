package com.urssu.bum.incubating.config

import com.urssu.bum.incubating.model.Role
import com.urssu.bum.incubating.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class RoleTypeConstants @Autowired constructor(
        private var roleRepository: RoleRepository
) {
    @Bean
    @Qualifier("USER")
    fun USER(): Role {
        return roleRepository.findByName("USER")
    }
}