package com.urssu.bum.incubating.repository

import com.urssu.bum.incubating.model.Memo
import com.urssu.bum.incubating.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface MemoRepository : JpaRepository<Memo, Long> {
    fun findAllByOwnerOrderByCreatedAtDesc(user: User): List<Memo>
    fun findAllByOwnerOrderByCreatedAtDesc(user: User, pageable: Pageable): List<Memo>
    fun findAllByOwnerAndTagOrderByCreatedAtDesc(user: User, tag: String): List<Memo>
    fun findAllByOwnerAndTagOrderByCreatedAtDesc(user: User, tag: String, pageable: Pageable): List<Memo>
    fun save(memo: Memo)
}