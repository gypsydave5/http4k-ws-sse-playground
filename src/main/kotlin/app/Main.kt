package org.example.app

import org.example.SseConnectionService
import org.example.SseMessageSender
import org.example.adaptor.sseHandler
import org.example.adaptor.webSocketHandler
import org.example.domain.UserId
import org.http4k.lens.Path
import org.http4k.routing.ResourceLoader.Companion.Directory
import org.http4k.routing.static
import org.http4k.server.PolyHandler
import org.http4k.server.Undertow
import org.http4k.server.asServer
import java.util.*
import kotlin.concurrent.thread

val userIdPath = Path.map({ UserId(UUID.fromString(it)) }, { it.value.toString() }).of("id")

fun main() {
    val sseConnectionService = SseConnectionService()
    val messageSender = SseMessageSender(sseConnectionService)


    val ws = webSocketHandler()


    val sse = sseHandler(sseConnectionService)

    val http = static(Directory("src/main/resources/web"))


    PolyHandler(http, ws = ws, sse = sse).asServer(Undertow(9000)).start()

    thread { cli(messageSender, sseConnectionService) }
}

