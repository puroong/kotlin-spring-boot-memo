package com.urssu.bum.incubating.model.dto

import com.urssu.bum.incubating.model.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired

object Roles {
    @Autowired
    private lateinit var roleRepository: RoleRepository

    val USER = roleRepository.findByName("USER")
}