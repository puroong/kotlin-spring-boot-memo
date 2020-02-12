package com.urssu.bum.incubating.controller.v1.request

class CreateMemoRequest(
        val title: String,
        val content: String,
        val isPublic: Boolean,
        val tag: String
)
