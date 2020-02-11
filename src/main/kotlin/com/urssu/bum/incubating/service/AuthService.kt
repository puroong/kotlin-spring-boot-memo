package com.urssu.bum.incubating.service

import com.urssu.bum.incubating.error.InvalidUserDataError
import com.urssu.bum.incubating.error.UserAlreadyExistError
import com.urssu.bum.incubating.model.dto.Roles
import com.urssu.bum.incubating.model.dto.auth.SigninUserDTO.SigninUserDTO
import com.urssu.bum.incubating.model.dto.auth.SignupUserDTO
import com.urssu.bum.incubating.model.entity.User
import com.urssu.bum.incubating.model.repository.UserRepository
import com.urssu.bum.incubating.util.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService @Autowired constructor(
        private var userRepository: UserRepository,
        private var passwordEncoder: PasswordEncoder,
        private var authenticationManager: AuthenticationManager,
        private var userDetailsService: UserDetailsService,
        private var jwtTokenUtil: JwtUtil
) {
    fun signup(signupUserDTO: SignupUserDTO) {
        val userExists = userRepository.existsByUsername(signupUserDTO.username)
        if(userExists) throw UserAlreadyExistError()

        val newUser = User(
                username = signupUserDTO.username,
                password = passwordEncoder.encode(signupUserDTO.password),
                role = Roles.USER
        )
        userRepository.save(newUser)
    }

    fun signinAndCreateJwt(signinUserDTO: SigninUserDTO): String {
        try {
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                            signinUserDTO.username,
                            signinUserDTO.password
                    )
            )
        } catch(e: BadCredentialsException) {
            throw InvalidUserDataError()
        }

        val userDetails = userDetailsService
                .loadUserByUsername(signinUserDTO.username)
        val jwt = jwtTokenUtil.generateToken(userDetails)

        return jwt
    }
}