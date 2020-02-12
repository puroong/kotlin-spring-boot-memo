package com.urssu.bum.incubating.dto.model.memo

import com.urssu.bum.incubating.dto.model.user.UserDto
import java.util.*

class MemoDto(
        val title: String,
        val content: String,
        val createdAt: Date,
        val isPublic: Boolean,
        val tag: String,
        val owner: UserDto
)
