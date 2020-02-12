package com.urssu.bum.incubating.model

import com.urssu.bum.incubating.dto.model.memo.MemoDto
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "memos")
class Memo(
        @Id @GeneratedValue val id: Long? = null,
        val title: String? = null,
        @Column(nullable = false) val content: String,
        val createdAt: Date = Date(System.currentTimeMillis()),
        val isPublic: Boolean = true,
        val tag: String? = null,
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