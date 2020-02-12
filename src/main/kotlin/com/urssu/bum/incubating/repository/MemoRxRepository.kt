package com.urssu.bum.incubating.repository

import com.urssu.bum.incubating.model.Memo
import com.urssu.bum.incubating.model.User
import com.urssu.bum.incubating.model.flag.MemoStatus
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

interface MemoRxRepository {
    fun save(memo: Memo): Mono<Unit>

    fun getOne(memoId: Long): Mono<Memo>

    fun findAllByOwnerAndStatusOrderByCreatedAtDesc(user: User, status: MemoStatus): Flux<Memo>
    fun findAllByOwnerAndStatusOrderByCreatedAtDesc(user: User, status: MemoStatus, limit: Int, offset: Long): Flux<Memo>
    fun findAllByOwnerAndTagAndStatusOrderByCreatedAtDesc(user: User, tag: String, status: MemoStatus): Flux<Memo>
    fun findAllByOwnerAndTagAndStatusOrderByCreatedAtDesc(user: User, tag: String, status: MemoStatus, limit: Int, offset: Long): Flux<Memo>
    fun findAllByIsPublicAndStatusOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus): Flux<Memo>
    fun findAllByIsPublicAndStatusOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus, limit: Int, offset: Long): Flux<Memo>
    fun findAllByIsPublicAndStatusAndTagOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus, tag: String): Flux<Memo>
    fun findAllByIsPublicAndStatusAndTagOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus, tag: String, limit: Int, offset: Long): Flux<Memo>
}