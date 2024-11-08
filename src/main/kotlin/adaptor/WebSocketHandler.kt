package org.example.adaptor

import org.example.app.userIdPath
import org.http4k.core.Request
import org.http4k.routing.websockets
import org.http4k.routing.ws.bind
import org.http4k.websocket.Websocket
import org.http4k.websocket.WsMessage
import org.http4k.websocket.WsResponse

fun webSocketHandler() = websockets(
    "/ws/{name}" bind { req: Request ->
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