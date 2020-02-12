package com.urssu.bum.incubating.service

import com.urssu.bum.incubating.controller.v1.request.CreateMemoRequest
import com.urssu.bum.incubating.model.Memo
import com.urssu.bum.incubating.repository.MemoRxRepository
import com.urssu.bum.incubating.repository.UserRxRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class MemoService @Autowired constructor(
        private var memoRxRepository: MemoRxRepository,
        private var userRxRepository: UserRxRepository
){
    @PreAuthorize("hasAuthority('WRITE_MY_MEMO')")
    fun creatememo(createMemoRequest: CreateMemoRequest): Mono<Unit> {
        val userDetails = SecurityContextHolder.getContext().authentication.principal as UserDetails

        return userRxRepository.findByUsername(userDetails.username)
                .flatMap { memoRxRepository.save(
                        Memo(
                                title = createMemoRequest.title,
                                content = createMemoRequest.content,
                                isPublic = createMemoRequest.isPublic,
                                tag = createMemoRequest.tag,
                                owner = it
                        )
                ) }
    }
}