package org.example.domain

import java.util.UUID

@JvmInline
value class UserId(val value: UUID)

@JvmInline
value class UserName(val value: String)

data class User(val id: UserId, val name: UserName) {
    companion object
}

