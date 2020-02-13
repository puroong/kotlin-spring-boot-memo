package com.urssu.bum.incubating.service

import com.urssu.bum.incubating.repository.memo.MemoRxRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class TagService @Autowired constructor(
        private val memoRxRepository: MemoRxRepository
){
    fun getTags(limit: Int, offset: Int?): Flux<String> {
        return memoRxRepository.findDistinctTags(limit, offset ?: 0)
    }
}