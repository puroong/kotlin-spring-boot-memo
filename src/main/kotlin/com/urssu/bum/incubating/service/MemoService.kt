package com.urssu.bum.incubating.service

import com.urssu.bum.incubating.controller.v1.request.MemoCreateRequest
import com.urssu.bum.incubating.controller.v1.request.MemoUpdateRequest
import com.urssu.bum.incubating.dto.model.memo.MemoDto
import com.urssu.bum.incubating.exception.MemoAlreadyDeletedException
import com.urssu.bum.incubating.exception.MemoNotFoundException
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
    fun createMemo(createMemoRequest: MemoCreateRequest): Mono<Unit> {
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

    fun getPublicMemos(tag: String?, limit: Int?, offset: Long?): Flux<MemoDto> {
        /*
        경우의 수는 아래와 같음

        | tag      | limit    | offset   |
        | null     | null     | null     |
        | not null | null     | null     |
        | null     | not null | not null |
        | not null | not null | not null |

        나머지는 전부 badrequestexception
        * */
        if (tag == null && limit == null && offset == null) return getPublicMemos()
        else if (tag != null && limit == null && offset == null) return getPublicMemos(tag)
        else if (tag == null && limit != null && offset != null) return getPublicMemos(limit, offset)
        else if (tag != null && limit != null && offset != null) return getPublicMemos(tag, limit, offset)
        else throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }

    private fun getPublicMemos(): Flux<MemoDto> {
        return memoRxRepository.findAllByIsPublicAndStatusOrderByCreatedAtDesc(true, MemoStatus.PUBLISHED)
                .map { it.toMemoDto() }
    }

    private fun getPublicMemos(limit: Int, offset: Long): Flux<MemoDto> {
        return memoRxRepository.findAllByIsPublicAndStatusOrderByCreatedAtDesc(true, MemoStatus.PUBLISHED, limit, offset)
                .map { it.toMemoDto() }
    }

    private fun getPublicMemos(tag: String): Flux<MemoDto> {
        return memoRxRepository.findAllByIsPublicAndStatusAndTagOrderByCreatedAtDesc(true, MemoStatus.PUBLISHED, tag)
                .map { it.toMemoDto() }
    }

    private fun getPublicMemos(tag: String, limit: Int, offset: Long): Flux<MemoDto> {
        return memoRxRepository.findAllByIsPublicAndStatusAndTagOrderByCreatedAtDesc(true, MemoStatus.PUBLISHED, tag, limit, offset)
                .map { it.toMemoDto() }
    }

    fun getPublishedMemos(username: String, tag: String?, limit: Int?, offset: Long?): Flux<MemoDto> {
        /*
        경우의 수는 아래와 같음

        | tag      | limit    | offset   |
        | null     | null     | null     |
        | not null | null     | null     |
        | null     | not null | not null |
        | not null | not null | not null |

        나머지는 전부 badrequestexception
        * */
        if (tag == null && limit == null && offset == null) return getPublishedMemosByUsername(username)
        else if (tag != null && limit == null && offset == null) return getPublishedMemosByUsernameAndTag(username, tag)
        else if (tag == null && limit != null && offset != null) return getPublishedMemosByUsername(username, limit, offset)
        else if (tag != null && limit != null && offset != null) return getPublishedMemosByUsernameAndTag(username, tag, limit, offset)
        else throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }

    private fun getPublishedMemosByUsernameAndTag(username: String, tag: String): Flux<MemoDto> {
        return userRxRepository.findByUsername(username)
                .flatMapMany { memoRxRepository.findAllByOwnerAndTagAndStatusOrderByCreatedAtDesc(it, tag, MemoStatus.PUBLISHED) }
                .map { it.toMemoDto() }
    }

    private fun getPublishedMemosByUsernameAndTag(username: String, tag: String, limit: Int, offset: Long): Flux<MemoDto> {
        return userRxRepository.findByUsername(username)
                .flatMapMany { memoRxRepository.findAllByOwnerAndTagAndStatusOrderByCreatedAtDesc(it, tag, MemoStatus.PUBLISHED, limit, offset) }
                .map { it.toMemoDto() }
    }

    private fun getPublishedMemosByUsername(username: String): Flux<MemoDto> {
        return userRxRepository.findByUsername(username)
                .flatMapMany { memoRxRepository.findAllByOwnerAndStatusOrderByCreatedAtDesc(it, MemoStatus.PUBLISHED) }
                .map { it.toMemoDto() }
    }

    private fun getPublishedMemosByUsername(username: String, limit: Int, offset: Long): Flux<MemoDto> {
        return userRxRepository.findByUsername(username)
                .flatMapMany { memoRxRepository.findAllByOwnerAndStatusOrderByCreatedAtDesc(it, MemoStatus.PUBLISHED, limit, offset) }
                .map { it.toMemoDto() }
    }

    fun updateMemo(memoId: Long, updateMemoRequest: MemoUpdateRequest): Mono<Unit> {
        return memoRxRepository.getOne(memoId)
                .flatMap {
                    // TODO: 깔끔하게 update하는 방법 찾기
                    it.title = updateMemoRequest.title
                    it.content = updateMemoRequest.content
                    it.isPublic = updateMemoRequest.isPublic
                    it.tag = updateMemoRequest.tag

                    memoRxRepository.save(it)
                }
                .switchIfEmpty(Mono.error(MemoNotFoundException()))
    }

    fun deleteMemo(memoId: Long): Mono<Unit> {
        return memoRxRepository.getOne(memoId)
                .flatMap {
                    if(it.status == MemoStatus.DELETED) throw MemoAlreadyDeletedException()

                    // TODO: 깔끔하게 update하는 방법 찾기
                    it.status = MemoStatus.DELETED

                    memoRxRepository.save(it)
                }
                .switchIfEmpty(Mono.error(MemoNotFoundException()))
    }
}