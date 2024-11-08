package org.example.adaptor

import org.example.SseConnectionService
import org.example.app.userIdPath
import org.http4k.routing.sse
import org.http4k.routing.sse.bind
import org.http4k.sse.Sse
import org.http4k.sse.SseResponse

fun sseHandler(sseConnectionService: SseConnectionService) = sse(
    "/sse/{id}" bind { req ->
        SseResponse(headers = listOf("Access-Control-Allow-Origin" to "*")) { sse: Sse ->

            val userId = userIdPath(req)

            sseConnectionService.addConnection(userIdPath(req), sse)

            println("connection $userId has been added\n")
        }
    }
)