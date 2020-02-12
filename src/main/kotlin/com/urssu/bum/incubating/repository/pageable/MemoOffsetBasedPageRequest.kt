package com.urssu.bum.incubating.repository.pageable

import org.springframework.data.domain.Pageable

class MemoOffsetBasedPageRequest(
        override val _limit: Int,
        override val _offset: Long
) : OffsetBasedPageRequest() {
    override fun next(): Pageable {
        return MemoOffsetBasedPageRequest(getPageSize(), getOffset() + getPageSize())
    }

    override fun previous(): Pageable {
        if (hasPrevious()) return MemoOffsetBasedPageRequest(getPageSize(), getOffset() - getPageSize())
        else return this
    }

    override fun first(): Pageable {
        return MemoOffsetBasedPageRequest(getPageSize(), 0)
    }
}