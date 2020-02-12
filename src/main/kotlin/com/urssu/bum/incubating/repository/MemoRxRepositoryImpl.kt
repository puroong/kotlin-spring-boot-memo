package com.urssu.bum.incubating.repository

import com.urssu.bum.incubating.model.Memo
import com.urssu.bum.incubating.model.User
import com.urssu.bum.incubating.repository.pageable.MemoOffsetBasedPageRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Repository
class MemoRxRepositoryImpl @Autowired constructor(
        private var memoRepository: MemoRepository
) : MemoRxRepository {
    override fun save(memo: Memo): Mono<Unit> {
        return Mono.fromCallable { memoRepository.save(memo) }
                .subscribeOn(Schedulers.elastic())
    }

    override fun getOne(memoId: Long): Mono<Memo> {
        return Mono.fromCallable { memoRepository.getOne(memoId) }
                .subscribeOn(Schedulers.elastic())
    }

    override fun findAllByOwnerOrderByCreatedAtDesc(user: User): Flux<Memo> {
        return Mono.fromCallable { memoRepository.findAllByOwnerOrderByCreatedAtDesc(user) }
                .flatMapMany { Flux.fromIterable(it) }
    }

    override fun findAllByOwnerOrderByCreatedAtDesc(user: User, limit: Int, offset: Long): Flux<Memo> {
        val pageable = MemoOffsetBasedPageRequest(limit, offset)
        return Mono.fromCallable { memoRepository.findAllByOwnerOrderByCreatedAtDesc(user, pageable) }
                .flatMapMany { Flux.fromIterable(it) }
    }

    override fun findAllByOwnerAndTagOrderByCreatedAtDesc(user: User, tag: String): Flux<Memo> {
        return Mono.fromCallable { memoRepository.findAllByOwnerAndTagOrderByCreatedAtDesc(user, tag) }
                .flatMapMany { Flux.fromIterable(it) }
    }

    override fun findAllByOwnerAndTagOrderByCreatedAtDesc(user: User, tag: String, limit: Int, offset: Long): Flux<Memo> {
        val pageable = MemoOffsetBasedPageRequest(limit, offset)
        return Mono.fromCallable { memoRepository.findAllByOwnerAndTagOrderByCreatedAtDesc(user, tag, pageable) }
                .flatMapMany { Flux.fromIterable(it) }
    }
}