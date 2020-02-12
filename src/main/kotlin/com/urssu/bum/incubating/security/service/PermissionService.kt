package com.urssu.bum.incubating.security.service

import com.urssu.bum.incubating.exception.MemoNotExistException
import com.urssu.bum.incubating.repository.MemoRepository
import com.urssu.bum.incubating.security.SecurityConstants
import com.urssu.bum.incubating.security.util.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class PermissionService @Autowired constructor(
        private val memoRepository: MemoRepository,
        private val jwtUtil: JwtUtil
) {
    fun isMemoOwner(username: String): Boolean {
        return SecurityContextHolder.getContext().authentication.name == username
    }

    fun isMemoOwner(authorization: String, memoId: Long): Boolean {
        val token = authorization.substring(SecurityConstants.TOKEN_PREFIX.length)

        val usernameFromToken = jwtUtil.extractUsername(token)
        var memoOwnername: String? = null
        // TODO: Memo 존재 여부를 어디서 확인할지 고민
        try{
            memoOwnername = memoRepository.getOne(memoId).owner.username
        } catch (e: JpaObjectRetrievalFailureException) {
            throw MemoNotExistException()
        }

        return memoOwnername == usernameFromToken
    }

    // TOOD: 이름 좀 괜찮은걸로 바꾸기 + isMemoOwner랑 코드가 겹치는데 재사용할 방법 찾기
    fun isMe(username: String): Boolean {
        return SecurityContextHolder.getContext().authentication.name == username
    }
}