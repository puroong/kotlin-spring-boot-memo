package com.urssu.bum.incubating.repository.memo

import com.urssu.bum.incubating.model.memo.Memo
import com.urssu.bum.incubating.model.user.User
import com.urssu.bum.incubating.model.memo.MemoStatus
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface MemoRxRepository {
    fun save(memo: Memo): Mono<Unit>

    fun existsById(id: Long): Mono<Boolean>

    fun getOne(memoId: Long): Mono<Memo>
    fun getOneIfExist(memoId: Long): Mono<Memo>

    fun findAllByOwnerAndStatusOrderByCreatedAtDesc(user: User, status: MemoStatus): Flux<Memo>
    fun findAllByOwnerAndStatusOrderByCreatedAtDesc(user: User, status: MemoStatus, limit: Int, offset: Int): Flux<Memo>

    fun findAllByOwnerAndTagAndStatusOrderByCreatedAtDesc(user: User, tag: String, status: MemoStatus): Flux<Memo>
    fun findAllByOwnerAndTagAndStatusOrderByCreatedAtDesc(user: User, tag: String, status: MemoStatus, limit: Int, offset: Int): Flux<Memo>

    fun findAllByIsPublicAndStatusOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus): Flux<Memo>
    fun findAllByIsPublicAndStatusOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus, limit: Int, offset: Int): Flux<Memo>

    fun findAllByIsPublicAndStatusAndTagOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus, tag: String): Flux<Memo>
    fun findAllByIsPublicAndStatusAndTagOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus, tag: String, limit: Int, offset: Int): Flux<Memo>

    fun findDistinctTags(limit: Int, offset: Int): Flux<String>
}