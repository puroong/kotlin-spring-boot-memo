package com.urssu.bum.incubating.service

import com.urssu.bum.incubating.controller.v1.request.UserSigninRequest
import com.urssu.bum.incubating.controller.v1.request.UserSignupRequest
import com.urssu.bum.incubating.exception.InvalidUserDataException
import com.urssu.bum.incubating.exception.UserAlreadyExistException
import com.urssu.bum.incubating.dto.model.security.TokenDto
import com.urssu.bum.incubating.model.Role
import com.urssu.bum.incubating.model.User
import com.urssu.bum.incubating.repository.UserRxRepository
import com.urssu.bum.incubating.security.util.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
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
        private var userRxRepository: UserRxRepository,
        private var passwordEncoder: PasswordEncoder,
        private var authenticationManager: AuthenticationManager,
        private var userDetailsService: UserDetailsService,
        private var jwtTokenUtil: JwtUtil,
        @Qualifier("USER") private var ROLE_USER: Role
) {
    fun signupNewUser(userSignupRequest: UserSignupRequest): Mono<Void> {
        return userRxRepository.existsByUsername(userSignupRequest.username)
                .flatMap  { userExist ->
                    if(userExist) throw UserAlreadyExistException()
                    else userRxRepository.save(
                            User(
                                    username = userSignupRequest.username,
                                    password = passwordEncoder.encode(userSignupRequest.password),
                                    role = ROLE_USER
                            )
                    )
                }
                .then(Mono.empty())
    }

    fun authenticateAndCreateToken(userSigninRequest: UserSigninRequest): Mono<TokenDto> {
        try {
            // TODO: 인증을 어떻게 하는지 잘 모르겠음. 잘못된 정보 전달 시 내가 예외처리 할 수 있는 방법
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                            userSigninRequest.username,
                            userSigninRequest.password
                    )
            )
        } catch (e: InternalAuthenticationServiceException) {
            throw InvalidUserDataException()
        } catch(e: BadCredentialsException) {
            throw InvalidUserDataException()
        }

        val userDetails = userDetailsService
                .loadUserByUsername(userSigninRequest.username)
        val jwt = jwtTokenUtil.generateToken(userDetails)

        return Mono.just(TokenDto(jwt))
    }
}