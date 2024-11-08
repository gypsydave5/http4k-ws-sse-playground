package org.example

import org.example.domain.MessageSender
import org.example.domain.User
import org.http4k.sse.SseMessage

class SseMessageSender(private val connections: SseConnectionService) : MessageSender {
    override fun sendToAll(message: String) {
        connections.allConnections().forEach {
            it.send(SseMessage.Event("message", message))
        }
    }

    override fun sendTo(user: User, message: String) {
        connections.connectionsFor(user).forEach {
            it.send(SseMessage.Event("message", message))
        }
    }
}
