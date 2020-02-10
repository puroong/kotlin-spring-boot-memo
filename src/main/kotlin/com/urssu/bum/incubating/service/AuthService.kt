package com.urssu.bum.incubating.service

import com.urssu.bum.incubating.error.UserAlreadyExistError
import com.urssu.bum.incubating.model.dto.auth.SignupUserDTO
import com.urssu.bum.incubating.model.entity.User
import com.urssu.bum.incubating.model.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthService @Autowired constructor(
        private var userRepository: UserRepository
) {
    fun signup(signupUserDTO: SignupUserDTO) {
        val userExists = userRepository.existsByName(signupUserDTO.name)
        if(userExists) throw UserAlreadyExistError()

        val user = User(
                name = signupUserDTO.name,
                password = signupUserDTO.password
        )
        userRepository.save(user)
    }
}