package org.example.adaptor

import org.example.SseConnectionService
import org.example.app.namePath
import org.http4k.routing.sse
import org.http4k.routing.sse.bind
import org.http4k.sse.Sse
import org.http4k.sse.SseResponse

fun sseHandler(sseConnectionService: SseConnectionService) = sse(
    "/sse/{name}" bind { req ->
        SseResponse(headers = listOf("Access-Control-Allow-Origin" to "*")) { sse: Sse ->

            val name = namePath(req)

            sseConnectionService.addConnection(namePath(req), sse)

            println("connection $name has been added\n")
        }
    }
)