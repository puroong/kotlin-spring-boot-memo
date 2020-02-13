package com.urssu.bum.incubating.model.memo

import com.urssu.bum.incubating.model.user.User
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "memos")
class Memo(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
        @Column(name = "title", nullable = false) var title: String = "",
        @Column(name = "content", nullable = false) var content: String,
        @Column(name = "created_at", nullable = false) val createdAt: Date = Date(System.currentTimeMillis()),
        @Column(name = "is_public", nullable = false) var isPublic: Boolean = true,
        @Column(name = "tag", nullable = false) var tag: String = "",
        @ManyToOne val owner: User,
        @Column(name = "status", nullable = false) @Enumerated(EnumType.STRING) var status: MemoStatus = MemoStatus.PUBLISHED
) {
    fun update(
            title: String = this.title,
            content: String = this.content,
            isPublic: Boolean = this.isPublic,
            tag: String = this.tag,
            status: MemoStatus = this.status
    ) {
        this.title = title
        this.content = content
        this.isPublic = isPublic
        this.tag = tag
        this.status = status
    }
}