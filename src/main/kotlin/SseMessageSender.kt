package org.example

import org.http4k.sse.SseMessage

class SseMessageSender(private val connections: SseConnectionService) : MessageSender {
    fun send(message: String) {
        connections.send(SseMessage.Event("message", message))
    }
}
