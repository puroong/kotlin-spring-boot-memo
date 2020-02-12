package com.urssu.bum.incubating.service

import com.urssu.bum.incubating.controller.v1.request.CreateMemoRequest
import com.urssu.bum.incubating.controller.v1.request.UpdateMemoRequest
import com.urssu.bum.incubating.dto.model.memo.MemoDto
import com.urssu.bum.incubating.exception.MemoAlreadyDeletedException
import com.urssu.bum.incubating.exception.MemoNotExistException
import com.urssu.bum.incubating.model.Memo
import com.urssu.bum.incubating.model.flag.MemoStatus
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

    fun getPublicMemos(username: String, tag: String?, limit: Int?, offset: Long?): Flux<MemoDto> {
        /*
        경우의 수는 아래와 같음

        | tag      | limit    | offset   |
        | null     | null     | null     |
        | not null | null     | null     |
        | null     | not null | not null |
        | not null | not null | not null |

        나머지는 전부 badrequestexception
        * */
        if (tag == null && limit == null && offset == null) return getPublicMemosByUsername(username)
        else if (tag != null && limit == null && offset == null) return getPublicMemosByUsernameAndTag(username, tag)
        else if (tag == null && limit != null && offset != null) return getPublicMemosByUsername(username, limit, offset)
        else if (tag != null && limit != null && offset != null) return getPublicMemosByUsernameAndTag(username, tag, limit, offset)
        else throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }

    private fun getPublicMemosByUsernameAndTag(username: String, tag: String): Flux<MemoDto> {
        return userRxRepository.findByUsername(username)
                .flatMapMany { memoRxRepository.findAllByOwnerAndTagAndStatusOrderByCreatedAtDesc(it, tag, MemoStatus.PUBLIC) }
                .map { it.toMemoDto() }
    }

    private fun getPublicMemosByUsernameAndTag(username: String, tag: String, limit: Int, offset: Long): Flux<MemoDto> {
        return userRxRepository.findByUsername(username)
                .flatMapMany { memoRxRepository.findAllByOwnerAndTagAndStatusOrderByCreatedAtDesc(it, tag, MemoStatus.PUBLIC, limit, offset) }
                .map { it.toMemoDto() }
    }

    fun getPublicMemosByUsername(username: String): Flux<MemoDto> {
        return userRxRepository.findByUsername(username)
                .flatMapMany { memoRxRepository.findAllByOwnerAndStatusOrderByCreatedAtDesc(it, MemoStatus.PUBLIC) }
                .map { it.toMemoDto() }
    }

    fun getPublicMemosByUsername(username: String, limit: Int, offset: Long): Flux<MemoDto> {
        return userRxRepository.findByUsername(username)
                .flatMapMany { memoRxRepository.findAllByOwnerAndStatusOrderByCreatedAtDesc(it, MemoStatus.PUBLIC, limit, offset) }
                .map { it.toMemoDto() }
    }

    fun updateMemo(memoId: Long, updateMemoRequest: UpdateMemoRequest): Mono<Unit> {
        return memoRxRepository.getOne(memoId)
                .flatMap {
                    // TODO: 깔끔하게 update하는 방법 찾기
                    it.title = updateMemoRequest.title
                    it.content = updateMemoRequest.content
                    it.isPublic = updateMemoRequest.isPublic
                    it.tag = updateMemoRequest.tag

                    memoRxRepository.save(it)
                }
                .switchIfEmpty(Mono.error(MemoNotExistException()))
    }

    fun deleteMemo(memoId: Long): Mono<Unit> {
        return memoRxRepository.getOne(memoId)
                .flatMap {
                    if(it.status == MemoStatus.DELETED) throw MemoAlreadyDeletedException()

                    // TODO: 깔끔하게 update하는 방법 찾기
                    it.status = MemoStatus.DELETED

                    memoRxRepository.save(it)
                }
                .switchIfEmpty(Mono.error(MemoNotExistException()))
    }
}