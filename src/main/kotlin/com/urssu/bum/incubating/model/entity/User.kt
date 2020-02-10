package com.urssu.bum.incubating.model.entity

import javax.persistence.*

@Entity
@Table(name="users")
class User(
        @Id @GeneratedValue var id: Long? = null,
        @Column(unique=true) var name: String,
        var password: String
)