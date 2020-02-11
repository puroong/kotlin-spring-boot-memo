package com.urssu.bum.incubating.dto

import com.urssu.bum.incubating.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired

object RoleTypes {
    @Autowired
    private lateinit var roleRepository: RoleRepository

    val USER = roleRepository.findByName("USER")
}