package org.example.domain

interface MessageSender {
    fun sendToAll(message: String)
    fun sendTo(user: User, message: String)
}