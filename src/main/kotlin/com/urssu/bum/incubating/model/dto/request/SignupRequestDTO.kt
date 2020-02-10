package com.urssu.bum.incubating.model.dto.request

import com.urssu.bum.incubating.model.dto.auth.SignupUserDTO

class SignupRequestDTO(
        val username: String,
        val password: String
) {
    fun toSignupUserDTO(): SignupUserDTO {
        return SignupUserDTO(username, password)
    }
}