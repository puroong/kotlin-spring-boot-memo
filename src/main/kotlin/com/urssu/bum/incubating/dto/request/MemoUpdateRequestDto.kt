package com.urssu.bum.incubating.dto.request

class MemoUpdateRequestDto(
        val title: String,
        val content: String,
        val isPublic: Boolean,
        val tag: String?
)
