package com.urssu.bum.incubating.repository.memo

import com.urssu.bum.incubating.exception.MemoNotFoundException
import com.urssu.bum.incubating.model.memo.Memo
import com.urssu.bum.incubating.model.user.User
import com.urssu.bum.incubating.model.memo.MemoStatus
import com.urssu.bum.incubating.repository.pageable.MemoOffsetBasedPageRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Repository
class MemoRxRepositoryImpl @Autowired constructor(
        private val memoRepository: MemoRepository
) : MemoRxRepository {
    override fun save(memo: Memo): Mono<Unit> {
        return Mono.fromCallable { memoRepository.save(memo) }
                .subscribeOn(Schedulers.elastic())
    }

    override fun existsById(id: Long): Mono<Boolean> {
        return Mono.fromCallable { memoRepository.existsById(id) }
                .subscribeOn(Schedulers.elastic())
    }

    override fun getOne(memoId: Long): Mono<Memo> {
        return Mono.fromCallable { memoRepository.getOne(memoId) }
                .subscribeOn(Schedulers.elastic())
    }

    override fun getOneIfExist(memoId: Long): Mono<Memo> {
        return existsById(memoId)
                .flatMap { memoExist ->
                    if(!memoExist) throw MemoNotFoundException()
                    getOne(memoId)
                }
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

    override fun findAllByIsPublicAndStatusOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus): Flux<Memo> {
        return Mono.fromCallable { memoRepository.findAllByIsPublicAndStatusOrderByCreatedAtDesc(isPublic, status) }
                .subscribeOn(Schedulers.elastic())
                .flatMapMany { Flux.fromIterable(it) }
    }

    override fun findAllByIsPublicAndStatusOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus, limit: Int, offset: Long): Flux<Memo> {
        val pageable = MemoOffsetBasedPageRequest(limit, offset)
        return Mono.fromCallable { memoRepository.findAllByIsPublicAndStatusOrderByCreatedAtDesc(true, status, pageable) }
                .subscribeOn(Schedulers.elastic())
                .flatMapMany { Flux.fromIterable(it) }
    }

    override fun findAllByIsPublicAndStatusAndTagOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus, tag: String): Flux<Memo> {
        return Mono.fromCallable { memoRepository.findAllByIsPublicAndStatusAndTagOrderByCreatedAtDesc(true, status, tag) }
                .subscribeOn(Schedulers.elastic())
                .flatMapMany { Flux.fromIterable(it) }
    }

    override fun findAllByIsPublicAndStatusAndTagOrderByCreatedAtDesc(isPublic: Boolean, status: MemoStatus, tag: String, limit: Int, offset: Long): Flux<Memo> {
        val pageable = MemoOffsetBasedPageRequest(limit, offset)
        return Mono.fromCallable { memoRepository.findAllByIsPublicAndStatusAndTagOrderByCreatedAtDesc(true, status, tag, pageable) }
                .subscribeOn(Schedulers.elastic())
                .flatMapMany { Flux.fromIterable(it) }
    }

    override fun findDistinctTags(limit: Int, offset: Int): Flux<String> {
        val pageable = PageRequest.of(offset/limit, limit)
        return Mono.fromCallable { memoRepository.findDistinctTags(pageable) }
                .subscribeOn(Schedulers.elastic())
                .flatMapMany { Flux.fromIterable(it) }
    }
}