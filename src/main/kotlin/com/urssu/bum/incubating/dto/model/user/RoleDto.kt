package com.urssu.bum.incubating.dto.model.user

// TODO: 지금 Entity랑 DTO랑 섞어쓰고 있는데 DTO만 사용하는쪽으로 리팩토링하기
class RoleDto(
        val name: String,
        val permissions: List<PermissionDto>
)
