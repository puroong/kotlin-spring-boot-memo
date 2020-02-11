package com.urssu.bum.incubating.security

import com.urssu.bum.incubating.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDeatilsService @Autowired constructor(
        private var userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        // TODO: user(entity)랑 user(userDetails) 분리하기
        val userModel = userRepository.findByUsername(username)
        return userModel.toUserDetails()
    }
}
