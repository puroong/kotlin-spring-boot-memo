package com.urssu.bum.incubating.model

import javax.persistence.*

@Entity
@Table(name = "memos")
class Memo(
        @Id @GeneratedValue val id: Long? = null,
        val title: String? = null,
        @Column(nullable = false) val content: String,
        val isPublic: Boolean = true,
        val tag: String? = null,
        @ManyToOne val owner: User
)