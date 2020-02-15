package com.urssu.bum.incubating.repository.memo

import com.urssu.bum.incubating.model.memo.Memo
import com.urssu.bum.incubating.model.user.User
import com.urssu.bum.incubating.model.memo.MemoStatus
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

// TODO: @Repository가 없는데 MemoRxRepositoryImpl에 어떻게 authowire 되는지 모르겠음
interface MemoRepository : JpaRepository<Memo, Long> {
    fun save(memo: Memo)

    fun findAllByOwnerAndStatusOrderByCreatedAtDesc(user: User, status: MemoStatus): List<Memo>
    fun findAllByOwnerAndStatusOrderByCreatedAtDesc(user: User, status: MemoStatus, pageable: Pageable): List<Memo>

    fun findAllByOwnerAndTagAndStatusOrderByCreatedAtDesc(user: User, tag: String, status: MemoStatus): List<Memo>
    fun findAllByOwnerAndTagAndStatusOrderByCreatedAtDesc(user: User, tag: String, status: MemoStatus, pageable: Pageable): List<Memo>

    fun findAllByIsPublicAndStatusOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus): List<Memo>
    fun findAllByIsPublicAndStatusOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus, pageable: Pageable): List<Memo>

    fun findAllByIsPublicAndStatusAndTagOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus, tag: String): List<Memo>
    fun findAllByIsPublicAndStatusAndTagOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus, tag: String, pageable: Pageable): List<Memo>

    @Query("SELECT DISTINCT tag FROM Memo m where m.tag!=null")
    fun findDistinctTags(pageable: Pageable): List<String>
}