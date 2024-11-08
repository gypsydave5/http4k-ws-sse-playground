package org.example.app

import org.example.SseConnectionService
import org.example.SseMessageSender
import org.example.adaptor.sseHandler
import org.http4k.core.Request
import org.http4k.lens.Path
import org.http4k.routing.*
import org.http4k.routing.ResourceLoader.Companion.Directory
import org.http4k.routing.ws.bind as bindWs
import org.http4k.server.PolyHandler
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.websocket.Websocket
import org.http4k.websocket.WsMessage
import org.http4k.websocket.WsResponse
import kotlin.concurrent.thread

val namePath = Path.of("name")

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val sseConnectionService = SseConnectionService()
    val messageSender = SseMessageSender(sseConnectionService)


    val ws = websockets(
        "/ws/{name}" bindWs { req: Request ->
            WsResponse { ws: Websocket ->
                val name = namePath(req)

                println("WebSocket Connected: $name")

                ws.send(WsMessage("hello $name"))

                ws.onMessage {
                    println("message received: " + it.bodyString())

                    ws.send(WsMessage("$name is responding"))
                }

                ws.onClose { println("$name is closing") }
            }
        }
    )


    val sse = sseHandler(sseConnectionService)

    val http = static(Directory("src/main/resources/web"))


    PolyHandler(http, ws = ws, sse = sse).asServer(Undertow(9000)).start()

    thread { cli(messageSender, sseConnectionService) }
}

