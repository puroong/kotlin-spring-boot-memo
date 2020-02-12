package com.urssu.bum.incubating.model

import com.urssu.bum.incubating.dto.model.memo.MemoDto
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "memos")
class Memo(
        @Id @GeneratedValue val id: Long? = null,
        var title: String? = null,
        @Column(nullable = false) var content: String,
        val createdAt: Date = Date(System.currentTimeMillis()),
        var isPublic: Boolean = true,
        var tag: String? = null,
        @ManyToOne val owner: User
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