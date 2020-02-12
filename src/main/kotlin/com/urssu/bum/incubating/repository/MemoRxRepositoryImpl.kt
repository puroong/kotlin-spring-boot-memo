package com.urssu.bum.incubating.repository

import com.urssu.bum.incubating.model.Memo
import com.urssu.bum.incubating.model.User
import com.urssu.bum.incubating.model.flag.MemoStatus
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

    override fun findAllByOwnerAndStatusOrderByCreatedAtDesc(user: User, status: MemoStatus): Flux<Memo> {
        return Mono.fromCallable { memoRepository.findAllByOwnerAndStatusOrderByCreatedAtDesc(user, status) }
                .subscribeOn(Schedulers.elastic())
                .flatMapMany { Flux.fromIterable(it) }
    }

    override fun findAllByOwnerAndStatusOrderByCreatedAtDesc(user: User, status: MemoStatus, limit: Int, offset: Long): Flux<Memo> {
        val pageable = MemoOffsetBasedPageRequest(limit, offset)
        return Mono.fromCallable { memoRepository.findAllByOwnerAndStatusOrderByCreatedAtDesc(user, status, pageable) }
                .subscribeOn(Schedulers.elastic())
                .flatMapMany { Flux.fromIterable(it) }
    }

    override fun findAllByOwnerAndTagAndStatusOrderByCreatedAtDesc(user: User, tag: String, status: MemoStatus): Flux<Memo> {
        return Mono.fromCallable { memoRepository.findAllByOwnerAndTagAndStatusOrderByCreatedAtDesc(user, tag, status) }
                .subscribeOn(Schedulers.elastic())
                .flatMapMany { Flux.fromIterable(it) }
    }

    override fun findAllByOwnerAndTagAndStatusOrderByCreatedAtDesc(user: User, tag: String, status: MemoStatus, limit: Int, offset: Long): Flux<Memo> {
        val pageable = MemoOffsetBasedPageRequest(limit, offset)
        return Mono.fromCallable { memoRepository.findAllByOwnerAndTagAndStatusOrderByCreatedAtDesc(user, tag, status, pageable) }
                .subscribeOn(Schedulers.elastic())
                .flatMapMany { Flux.fromIterable(it) }
    }
}