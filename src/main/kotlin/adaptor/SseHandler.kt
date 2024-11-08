package org.example.adaptor

import org.example.SseConnections
import org.example.namePath
import org.http4k.routing.sse
import org.http4k.routing.sse.bind
import org.http4k.sse.Sse
import org.http4k.sse.SseResponse

fun sseHandler(sseConnections: SseConnections) = sse(
    "/sse/{name}" bind { req ->
        SseResponse(headers = listOf("Access-Control-Allow-Origin" to "*")) { sse: Sse ->

            val name = namePath(req)

            sseConnections.addConnection(namePath(req), sse)

            println("connection $name has been added\n")

            sse.onClose {
                println("\n\n!!! $name is closing !!!\n\n")
                sseConnections.removeConnection(name, sse)
            }
        }
    }
)