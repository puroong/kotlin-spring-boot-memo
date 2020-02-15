package com.urssu.bum.incubating.security.service

import com.urssu.bum.incubating.exception.MemoNotFoundException
import com.urssu.bum.incubating.exception.UnauthorizedUserException
import com.urssu.bum.incubating.model.user.Permission
import com.urssu.bum.incubating.model.user.PermissionType
import com.urssu.bum.incubating.repository.memo.MemoRepository
import com.urssu.bum.incubating.security.SecurityProperty
import com.urssu.bum.incubating.security.util.JwtTokenUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class PermissionService @Autowired constructor(
        private val memoRepository: MemoRepository,
        private val jwtUtil: JwtTokenUtil,
        private val securityProperty: SecurityProperty
) {
    private fun invalidAuthorization(authorization: String): Boolean {
        return authorization.length <= securityProperty.HEADER_PREFIX.length
    }

    private fun isAnonymousUser(): Boolean {
        return SecurityContextHolder.getContext().authentication is AnonymousAuthenticationToken
    }

    private fun isCurrentUser(username: String): Boolean {
        if(isAnonymousUser()) throw UnauthorizedUserException()
        return SecurityContextHolder.getContext().authentication.name == username
    }

    private fun isCurrentUser(authorization: String, memoId: Long): Boolean {
        if(invalidAuthorization(authorization)) throw UnauthorizedUserException()

        val token = authorization.substring(securityProperty.HEADER_PREFIX.length)

        val usernameFromToken = jwtUtil.extractUsername(token)
        var memoOwnername: String? = null

        if (memoRepository.existsById(memoId)) memoOwnername = memoRepository.getOne(memoId).owner.username
        else throw MemoNotFoundException()

        return memoOwnername == usernameFromToken
    }

    private fun hasPermission(permissionType: PermissionType): Boolean {
        if(isAnonymousUser()) throw UnauthorizedUserException()

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