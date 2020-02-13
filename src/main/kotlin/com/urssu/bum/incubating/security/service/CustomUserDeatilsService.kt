package com.urssu.bum.incubating.security.service

import com.urssu.bum.incubating.repository.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDeatilsService @Autowired constructor(
        private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val userModel = userRepository.findByUsername(username)
        return userModel.toUserDetails()
    }
}
