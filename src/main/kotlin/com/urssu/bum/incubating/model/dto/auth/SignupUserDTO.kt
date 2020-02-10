package com.urssu.bum.incubating.model.dto.auth

import com.urssu.bum.incubating.model.dto.request.SignupRequestDTO

class SignupUserDTO(
        var name: String,
        var password: String
) {
    companion object {
        fun fromSignupRequest(signupRequestDTO: SignupRequestDTO): SignupUserDTO {
            val name = signupRequestDTO.name
            val password = signupRequestDTO.password

            return SignupUserDTO(name, password)
        }
    }
}