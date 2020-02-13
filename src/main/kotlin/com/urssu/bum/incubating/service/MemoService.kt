package com.urssu.bum.incubating.service

import com.urssu.bum.incubating.dto.request.MemoCreateRequestDto
import com.urssu.bum.incubating.dto.request.MemoUpdateRequestDto
import com.urssu.bum.incubating.exception.MemoAlreadyDeletedException
import com.urssu.bum.incubating.model.memo.Memo
import com.urssu.bum.incubating.model.memo.MemoStatus
import com.urssu.bum.incubating.repository.memo.MemoRxRepository
import com.urssu.bum.incubating.repository.user.UserRxRepository
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
        private val memoRxRepository: MemoRxRepository,
        private val userRxRepository: UserRxRepository
){
    fun createMemo(createMemoRequest: MemoCreateRequestDto): Mono<Unit> {
        val userDetails = SecurityContextHolder.getContext().authentication.principal as UserDetails

        return userRxRepository.findByUsernameIfExist(userDetails.username)
                .map {
                    Memo(
                            title = createMemoRequest.title,
                            content = createMemoRequest.content,
                            isPublic = createMemoRequest.isPublic,
                            tag = createMemoRequest.tag,
                            owner = it
                    )
                }
                .flatMap(memoRxRepository::save)
    }

    fun getPublicMemos(tag: String?, limit: Int?, offset: Long?): Flux<Memo> {
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

    private fun getPublicMemos(): Flux<Memo> {
        return memoRxRepository.findAllByIsPublicAndStatusOrderByCreatedAtDesc(true, MemoStatus.PUBLISHED)
    }

    private fun getPublicMemos(limit: Int, offset: Long): Flux<Memo> {
        return memoRxRepository.findAllByIsPublicAndStatusOrderByCreatedAtDesc(true, MemoStatus.PUBLISHED, limit, offset)
    }

    private fun getPublicMemos(tag: String): Flux<Memo> {
        return memoRxRepository.findAllByIsPublicAndStatusAndTagOrderByCreatedAtDesc(true, MemoStatus.PUBLISHED, tag)
    }

    private fun getPublicMemos(tag: String, limit: Int, offset: Long): Flux<Memo> {
        return memoRxRepository.findAllByIsPublicAndStatusAndTagOrderByCreatedAtDesc(true, MemoStatus.PUBLISHED, tag, limit, offset)
    }

    fun getPublishedMemos(username: String, tag: String?, limit: Int?, offset: Long?): Flux<Memo> {
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

    private fun getPublishedMemosByUsernameAndTag(username: String, tag: String): Flux<Memo> {
        return userRxRepository.findByUsername(username)
                .flatMapMany { memoRxRepository.findAllByOwnerAndTagAndStatusOrderByCreatedAtDesc(it, tag, MemoStatus.PUBLISHED) }
    }

    private fun getPublishedMemosByUsernameAndTag(username: String, tag: String, limit: Int, offset: Long): Flux<Memo> {
        return userRxRepository.findByUsernameIfExist(username)
                .flatMapMany { memoRxRepository.findAllByOwnerAndTagAndStatusOrderByCreatedAtDesc(it, tag, MemoStatus.PUBLISHED, limit, offset) }
    }

    private fun getPublishedMemosByUsername(username: String): Flux<Memo> {
        return userRxRepository.findByUsernameIfExist(username)
                .flatMapMany { memoRxRepository.findAllByOwnerAndStatusOrderByCreatedAtDesc(it, MemoStatus.PUBLISHED) }
    }

    private fun getPublishedMemosByUsername(username: String, limit: Int, offset: Long): Flux<Memo> {
        return userRxRepository.findByUsernameIfExist(username)
                .flatMapMany { memoRxRepository.findAllByOwnerAndStatusOrderByCreatedAtDesc(it, MemoStatus.PUBLISHED, limit, offset) }
    }

    fun updateMemo(memoId: Long, updateMemoRequest: MemoUpdateRequestDto): Mono<Unit> {
        return memoRxRepository.getOneIfExist(memoId)
                .map {
                    it.update(
                            title = updateMemoRequest.title,
                            content = updateMemoRequest.content,
                            isPublic = updateMemoRequest.isPublic,
                            tag = updateMemoRequest.tag
                    )
                    it
                }
                .flatMap(memoRxRepository::save)
    }

    fun deleteMemo(memoId: Long): Mono<Unit> {
        return memoRxRepository.getOneIfExist(memoId)
                .map {
                    if (it.status == MemoStatus.DELETED) throw MemoAlreadyDeletedException()
                    it.update(
                            status = MemoStatus.DELETED
                    )
                    it
                }
                .flatMap(memoRxRepository::save)
    }
}