package org.example.app

import org.example.SseConnectionService
import org.example.SseMessageSender
import org.example.adaptor.sseHandler
import org.example.domain.UserId
import org.http4k.core.Request
import org.http4k.lens.Path
import org.http4k.routing.ResourceLoader.Companion.Directory
import org.http4k.routing.static
import org.http4k.routing.websockets
import org.http4k.server.PolyHandler
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.websocket.Websocket
import org.http4k.websocket.WsMessage
import org.http4k.websocket.WsResponse
import java.util.*
import kotlin.concurrent.thread
import org.http4k.routing.ws.bind as bindWs

val userIdPath = Path.map({ UserId(UUID.fromString(it)) }, { it.value.toString() }).of("id")

fun main() {
    val sseConnectionService = SseConnectionService()
    val messageSender = SseMessageSender(sseConnectionService)


    val ws = websockets(
        "/ws/{name}" bindWs { req: Request ->
            WsResponse { ws: Websocket ->
                val id = userIdPath(req)

                println("WebSocket Connected: $id")

                ws.send(WsMessage("hello $id"))

                ws.onMessage {
                    println("message received: " + it.bodyString())

                    ws.send(WsMessage("$id is responding"))
                }

                ws.onClose { println("$id is closing") }
            }
        }
    )


    val sse = sseHandler(sseConnectionService)

    val http = static(Directory("src/main/resources/web"))


    PolyHandler(http, ws = ws, sse = sse).asServer(Undertow(9000)).start()

    thread { cli(messageSender, sseConnectionService) }
}

