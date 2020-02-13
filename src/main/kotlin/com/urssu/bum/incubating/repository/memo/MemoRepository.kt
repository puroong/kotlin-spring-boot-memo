package com.urssu.bum.incubating.repository

import com.urssu.bum.incubating.model.memo.Memo
import com.urssu.bum.incubating.model.user.User
import com.urssu.bum.incubating.model.memo.MemoStatus
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface MemoRepository : JpaRepository<Memo, Long> {
    fun findAllByOwnerAndStatusOrderByCreatedAtDesc(user: User, status: MemoStatus): List<Memo>
    fun findAllByOwnerAndStatusOrderByCreatedAtDesc(user: User, status: MemoStatus, pageable: Pageable): List<Memo>
    fun findAllByOwnerAndTagAndStatusOrderByCreatedAtDesc(user: User, tag: String, status: MemoStatus): List<Memo>
    fun findAllByOwnerAndTagAndStatusOrderByCreatedAtDesc(user: User, tag: String, status: MemoStatus, pageable: Pageable): List<Memo>
    fun findAllByIsPublicAndStatusOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus): List<Memo>
    fun findAllByIsPublicAndStatusOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus, pageable: Pageable): List<Memo>
    fun findAllByIsPublicAndStatusAndTagOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus, tag: String): List<Memo>
    fun findAllByIsPublicAndStatusAndTagOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus, tag: String, pageable: Pageable): List<Memo>
    fun save(memo: Memo)
}