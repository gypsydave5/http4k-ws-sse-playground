package adaptor

import org.example.SseConnections
import org.example.adaptor.sseHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.sse.SseMessage
import org.http4k.testing.testSseClient
import kotlin.test.Test

class SseHandlerTest {
    @Test
    fun `it does server side event stuff`() {
        val connections = SseConnections()
        val handler = sseHandler(connections)

        val client = handler.testSseClient(Request(Method.GET, "/sse/12345"))

        val event = SseMessage.Event("poop", "da woop")

        connections.send(event)

        client.close()

        val received = client.received().toList()

        assert(received.isNotEmpty())
        assert(received.size == 1)
        assert(received.first() == event)
    }
}