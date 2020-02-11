package com.urssu.bum.incubating.service

import com.urssu.bum.incubating.exception.InvalidUserDataException
import com.urssu.bum.incubating.exception.UserAlreadyExistException
import com.urssu.bum.incubating.dto.RoleTypes
import com.urssu.bum.incubating.dto.model.user.UserCredentialDto
import com.urssu.bum.incubating.model.User
import com.urssu.bum.incubating.repository.UserRepository
import com.urssu.bum.incubating.security.util.JwtUtil
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
    fun signupNewUser(userCredentialDto: UserCredentialDto) {
        val userExists = userRepository.existsByUsername(userCredentialDto.username)
        if(userExists) throw UserAlreadyExistException()

        val newUserModel = User(
                username = userCredentialDto.username,
                password = passwordEncoder.encode(userCredentialDto.password),
                role = RoleTypes.USER
        )
        userRepository.save(newUserModel)
    }

    fun authenticateAndCreateJwt(userCredentialDto: UserCredentialDto): String {
        try {
            // TODO: 인증을 어떻게 하는지 잘 모르겠음. 잘못된 정보 전달 시 내가 예외처리 할 수 있는 방법
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                            userCredentialDto.username,
                            userCredentialDto.password
                    )
            )
        } catch(e: BadCredentialsException) {
            throw InvalidUserDataException()
        }

        val userDetails = userDetailsService
                .loadUserByUsername(userCredentialDto.username)
        val jwt = jwtTokenUtil.generateToken(userDetails)

        return jwt
    }
}