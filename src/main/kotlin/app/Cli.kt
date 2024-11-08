package org.example.app

import org.example.SseConnectionService
import org.example.SseMessageSender

fun cli(messageSender: SseMessageSender, connectionService: SseConnectionService) {
    while (true) {
        println("--current connections--")
        println(connectionService.toString())
        println()
        print("send sse event: ")
        val message = readln()
        messageSender.sendToAll(message)
        println()
    }
}