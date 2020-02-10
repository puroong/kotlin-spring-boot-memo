package com.urssu.bum.incubating.service

import com.urssu.bum.incubating.model.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class MyUserDeatilsService @Autowired constructor(
        private var userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        // TODO: user(entity)랑 user(userDetails) 분리하기
        val user = userRepository.findByUsername(username)
        return user.toUserDetails()
    }
}
