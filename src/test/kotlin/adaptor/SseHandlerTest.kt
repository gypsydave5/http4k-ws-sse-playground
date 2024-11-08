package adaptor

import org.example.SseConnectionService
import org.example.SseMessageSender
import org.example.adaptor.sseHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.sse.SseMessage
import org.http4k.testing.testSseClient
import kotlin.test.Test

class SseHandlerTest {
    @Test
    fun `it does server side event stuff`() {
        val connections = SseConnectionService()
        val messageSender = SseMessageSender(connections)
        val handler = sseHandler(connections)

        val client = handler.testSseClient(Request(Method.GET, "/sse/12345"))

        messageSender.send("this is a message")

        client.close()

        val received = client.received().toList()
        val expectedMessage = SseMessage.Event("message", "this is a message")
        assert(received.first() == expectedMessage)
    }
}