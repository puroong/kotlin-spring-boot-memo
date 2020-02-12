package com.urssu.bum.incubating.repository

import com.urssu.bum.incubating.model.Memo
import org.springframework.data.jpa.repository.JpaRepository

interface MemoRepository : JpaRepository<Memo, Long> {
    fun save(memo: Memo)
}