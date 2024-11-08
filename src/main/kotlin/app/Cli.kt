package org.example.app

import org.example.SseMessageSender

fun cli(messageSender: SseMessageSender) {
    while (true) {
        print("send sse event: ")
        val message = readln()
        messageSender.send(message)
    }
}