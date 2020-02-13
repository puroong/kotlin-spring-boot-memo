package com.urssu.bum.incubating.security.service

import com.urssu.bum.incubating.model.user.Permission
import com.urssu.bum.incubating.model.user.PermissionType
import com.urssu.bum.incubating.repository.memo.MemoRepository
import com.urssu.bum.incubating.security.SecurityProperty
import com.urssu.bum.incubating.security.util.JwtTokenUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class PermissionService @Autowired constructor(
        private val memoRepository: MemoRepository,
        private val jwtUtil: JwtTokenUtil,
        private val securityProperty: SecurityProperty
) {
    private fun isCurrentUser(username: String): Boolean {
        return SecurityContextHolder.getContext().authentication.name == username
    }

    private fun isCurrentUser(authorization: String, memoId: Long): Boolean {
        val token = authorization.substring(securityProperty.HEADER_PREFIX.length)

        val usernameFromToken = jwtUtil.extractUsername(token)
        var memoOwnername = memoRepository.getOne(memoId).owner.username

        return memoOwnername == usernameFromToken
    }

    private fun hasPermission(permissionType: PermissionType): Boolean {
        val userAuthorities = SecurityContextHolder.getContext().authentication.authorities
                .map { authority ->
                    (authority as Permission).name
                }
        return userAuthorities.contains(permissionType.name)
    }

    fun hasWriteMemoPermission(): Boolean {
        return hasPermission(PermissionType.WRITE_MEMO)
    }

    fun hasModifyAnyMemoPermission(): Boolean {
        return hasPermission(PermissionType.MODIFY_ANY_MEMO)
    }

    fun hasModifyMyMemoPermission(authorization: String, memoId: Long): Boolean {
        return hasPermission(PermissionType.MODIFY_MY_MEMO) && isCurrentUser(authorization, memoId)
    }

    fun hasDeleteAnyMemoPermission(): Boolean {
        return hasPermission(PermissionType.DELETE_ANY_MEMO)
    }

    fun hasDeleteMyMemoPermission(authorization: String, memoId: Long): Boolean {
        return hasPermission(PermissionType.DELETE_MY_MEMO) && isCurrentUser(authorization, memoId)
    }

    fun hasReadAnyMemoPermission(): Boolean {
        return hasPermission(PermissionType.READ_ANY_MEMO)
    }

    fun hasReadMyMemoPermission(username: String): Boolean {
        return hasPermission(PermissionType.READ_MY_MEMO) && isCurrentUser(username)
    }

    fun hasReadAnyUserPermission(): Boolean {
        return hasPermission(PermissionType.READ_ANY_USER)
    }

    fun hasReadMyUserPermission(username: String): Boolean {
        return hasPermission(PermissionType.READ_MY_USER) && isCurrentUser(username)
    }

    fun hasDeleteAnyUserPermission(): Boolean {
        return hasPermission(PermissionType.DELETE_ANY_USER)
    }

    fun hasDeleteMyUserPermission(username: String): Boolean {
        return hasPermission(PermissionType.DELETE_MY_USER) && isCurrentUser(username)
    }

    fun hasModifyAnyUser(): Boolean {
        return hasPermission(PermissionType.MODIFY_ANY_USER)
    }
}