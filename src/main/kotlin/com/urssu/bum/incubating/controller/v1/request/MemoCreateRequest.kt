package com.urssu.bum.incubating.controller.v1.request

class MemoCreateRequest(
        val title: String,
        val content: String,
        val isPublic: Boolean,
        val tag: String
)
