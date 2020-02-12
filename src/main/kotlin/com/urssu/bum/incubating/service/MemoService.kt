package com.urssu.bum.incubating.service

import com.urssu.bum.incubating.controller.v1.request.CreateMemoRequest
import com.urssu.bum.incubating.dto.model.memo.MemoDto
import com.urssu.bum.incubating.model.Memo
import com.urssu.bum.incubating.repository.MemoRxRepository
import com.urssu.bum.incubating.repository.UserRxRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class MemoService @Autowired constructor(
        private var memoRxRepository: MemoRxRepository,
        private var userRxRepository: UserRxRepository
){
    fun createMemo(createMemoRequest: CreateMemoRequest): Mono<Unit> {
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

    fun getMemos(username: String, tag: String?, limit: Int?, offset: Long?): Flux<MemoDto> {
        /*
        경우의 수는 아래와 같음

        | tag      | limit    | offset   |
        | null     | null     | null     |
        | not null | null     | null     |
        | null     | not null | not null |
        | not null | not null | not null |

        나머지는 전부 badrequestexception
        * */
        if (tag == null && limit == null && offset == null) return getMemosByUsername(username)
        else if (tag != null && limit == null && offset == null) return getMemosByUsernameAndTag(username, tag)
        else if (tag == null && limit != null && offset != null) return getMemosByUsername(username, limit, offset)
        else if (tag != null && limit != null && offset != null) return getMemosByUsernameAndTag(username, tag, limit, offset)
        else throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }

    private fun getMemosByUsernameAndTag(username: String, tag: String): Flux<MemoDto> {
        return userRxRepository.findByUsername(username)
                .flatMapMany { memoRxRepository.findAllByOwnerAndTagOrderByCreatedAtDesc(it, tag) }
                .map { it.toMemoDto() }
    }

    private fun getMemosByUsernameAndTag(username: String, tag: String, limit: Int, offset: Long): Flux<MemoDto> {
        return userRxRepository.findByUsername(username)
                .flatMapMany { memoRxRepository.findAllByOwnerAndTagOrderByCreatedAtDesc(it, tag, limit, offset) }
                .map { it.toMemoDto() }
    }

    fun getMemosByUsername(username: String): Flux<MemoDto> {
        return userRxRepository.findByUsername(username)
                .flatMapMany { memoRxRepository.findAllByOwnerOrderByCreatedAtDesc(it) }
                .map { it.toMemoDto() }
    }

    fun getMemosByUsername(username: String, limit: Int, offset: Long): Flux<MemoDto> {
        return userRxRepository.findByUsername(username)
                .flatMapMany { memoRxRepository.findAllByOwnerOrderByCreatedAtDesc(it, limit, offset) }
                .map { it.toMemoDto() }
    }
}