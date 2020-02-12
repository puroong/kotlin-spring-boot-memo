package com.urssu.bum.incubating.dto.response

import com.urssu.bum.incubating.dto.model.security.TokenDto

class SigninResponseDto(
        val token: String
) {
    constructor(tokenDto: TokenDto) : this(tokenDto.token)
}