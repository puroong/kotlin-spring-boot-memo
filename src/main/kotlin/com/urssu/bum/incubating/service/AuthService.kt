package com.urssu.bum.incubating.service

import com.urssu.bum.incubating.dto.request.UserSigninRequestDto
import com.urssu.bum.incubating.dto.request.UserSignupRequestDto
import com.urssu.bum.incubating.exception.InvalidUserDataException
import com.urssu.bum.incubating.exception.UserAlreadyExistException
import com.urssu.bum.incubating.model.user.RoleType
import com.urssu.bum.incubating.model.user.User
import com.urssu.bum.incubating.repository.role.RoleRxRepository
import com.urssu.bum.incubating.repository.user.UserRxRepository
import com.urssu.bum.incubating.security.util.JwtTokenUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthService @Autowired constructor(
        private val userRxRepository: UserRxRepository,
        private val roleRxRepository: RoleRxRepository,
        private val passwordEncoder: PasswordEncoder,
        private val authenticationManager: AuthenticationManager,
        private val userDetailsService: UserDetailsService,
        private val jwtTokenUtil: JwtTokenUtil
) {
    fun signupNewUser(userSignupRequest: UserSignupRequestDto): Mono<Unit> {
        return userRxRepository.existsByUsername(userSignupRequest.username)
                .map {
                    if (it) throw UserAlreadyExistException()
                    RoleType.USER.name
                }
                .flatMap(roleRxRepository::findByName)
                .map {
                    User(
                            username = userSignupRequest.username,
                            password = passwordEncoder.encode(userSignupRequest.password),
                            isActive = true,
                            role = it
                    )
                }
                .flatMap(userRxRepository::save)
    }

    fun authenticateAndCreateToken(userSigninRequest: UserSigninRequestDto): Mono<String> {
        try {
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                            userSigninRequest.username,
                            userSigninRequest.password
                    )
            )
        } catch (e: Exception) {
            when(e) {
                is InternalAuthenticationServiceException,
                is BadCredentialsException -> throw InvalidUserDataException()
            }
        }

        val userDetails = userDetailsService
                .loadUserByUsername(userSigninRequest.username)
        val token = jwtTokenUtil.generateToken(userDetails)

        return Mono.just(token)
    }
}