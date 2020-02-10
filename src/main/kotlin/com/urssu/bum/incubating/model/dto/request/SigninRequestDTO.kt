package com.urssu.bum.incubating.model.dto.request

import com.urssu.bum.incubating.model.dto.auth.SigninUserDTO.SigninUserDTO
import com.urssu.bum.incubating.model.dto.auth.SignupUserDTO

class SigninRequestDTO(
        val username: String,
        val password: String
) {
    fun toSigninUserDTO(): SigninUserDTO {
        return SigninUserDTO(username, password)
    }
}