package com.urssu.bum.incubating.controller.v1.request

class UpdateMemoRequest(
        val title: String,
        val content: String,
        val isPublic: Boolean,
        val tag: String
)
