package com.urssu.bum.incubating.dto.model.memo

import com.urssu.bum.incubating.dto.model.user.UserDto
import com.urssu.bum.incubating.model.memo.Memo
import java.util.*

class MemoDto(
        val title: String,
        val content: String,
        val createdAt: Date,
        val isPublic: Boolean,
        val tag: String?,
        val owner: UserDto
) {
    constructor(memo: Memo): this(
            title = memo.title,
            content = memo.content,
            createdAt = memo.createdAt,
            isPublic = memo.isPublic,
            tag = memo.tag,
            owner = UserDto(memo.owner)
    )
}
