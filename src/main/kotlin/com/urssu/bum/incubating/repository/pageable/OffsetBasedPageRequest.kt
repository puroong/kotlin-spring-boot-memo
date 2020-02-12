package com.urssu.bum.incubating.repository.pageable

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

abstract class OffsetBasedPageRequest : Pageable {
    abstract protected val _limit: Int
    abstract protected val _offset: Long

    override fun getPageNumber(): Int {
        return _offset as Int / _limit
    }

    override fun hasPrevious(): Boolean {
        return _offset > _limit
    }

    override fun getSort(): Sort {
        return Sort.by("createdAt").descending()
    }

    override fun getPageSize(): Int {
        return _limit
    }

    override fun getOffset(): Long {
        return _offset
    }

    override fun previousOrFirst(): Pageable {
        if (hasPrevious()) return previous()
        else return first()
    }

    abstract fun previous(): Pageable
}