package com.urssu.bum.incubating.model

import com.urssu.bum.incubating.dto.model.memo.MemoDto
import com.urssu.bum.incubating.model.flag.MemoStatus
import java.util.*
import javax.persistence.*

// TODO: 삭제나 업데이트 할 때 cascade 옵션 따져보기
@Entity
@Table(name = "memos")
class Memo(
        @Id @GeneratedValue val id: Long? = null,
        var title: String? = null,
        @Column(nullable = false) var content: String,
        val createdAt: Date = Date(System.currentTimeMillis()),
        var isPublic: Boolean = true,
        var tag: String? = null,
        @ManyToOne val owner: User,
        @Enumerated(EnumType.STRING) var status: MemoStatus = MemoStatus.PUBLISHED
) {
    fun toMemoDto(): MemoDto {
        return MemoDto(
                title = title ?: "",
                content = content,
                createdAt = createdAt,
                isPublic = isPublic,
                tag = tag ?: "",
                owner = owner.toUserDto()
        )
    }
}