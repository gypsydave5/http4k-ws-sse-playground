package adaptor

import org.example.SseConnectionService
import org.example.SseMessageSender
import org.example.adaptor.sseHandler
import org.example.domain.User
import org.example.domain.UserId
import org.example.domain.UserName
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.sse.SseMessage
import org.http4k.testing.testSseClient
import java.util.UUID
import kotlin.test.Test

class SseHandlerTest {
    @Test
    fun `it does server side event stuff`() {
        val connections = SseConnectionService()
        val messageSender = SseMessageSender(connections)
        val handler = sseHandler(connections)

        val client = handler.testSseClient(Request(Method.GET, "/sse/12345"))

        messageSender.sendToAll("this is a message")

        client.close()

        val received = client.received().toList()
        val expectedMessage = SseMessage.Event("message", "this is a message")
        assert(received.first() == expectedMessage)
    }

    @Test
    fun `can handle multiple connections`() {
        val connections = SseConnectionService()
        val messageSender = SseMessageSender(connections)
        val handler = sseHandler(connections)

        val firstClient = handler.testSseClient(Request(Method.GET, "/sse/12345"))
        val secondClient = handler.testSseClient(Request(Method.GET, "/sse/12345"))

        messageSender.sendToAll("this is a message")

        firstClient.close()
        secondClient.close()

        val expectedMessage = SseMessage.Event("message", "this is a message")
        val firstClientRecieved = firstClient.received().toList()
        assert(firstClientRecieved.first() == expectedMessage)

        val secondClientRecieved = secondClient.received().toList()
        assert(secondClientRecieved.first() == expectedMessage)
    }

    @Test
    fun `can send only to targeted users`() {
        val connections = SseConnectionService()
        val messageSender = SseMessageSender(connections)
        val firstUser = User.random()
        val secondUser = User.random()
        val handler = sseHandler(connections)

        val firstUserClient = handler.testSseClient(Request(Method.GET, "/sse/${firstUser.id.value}"))
        val secondUserClient = handler.testSseClient(Request(Method.GET, "/sse/${secondUser.id.value}"))

        messageSender.sendTo(firstUser, "this is a message")

        firstUserClient.close()
        secondUserClient.close()

        val expectedMessage = SseMessage.Event("message", "this is a message")
        val firstClientRecieved = firstUserClient.received().toList()
        assert(firstClientRecieved.first() == expectedMessage)

        assert(secondUserClient.received().none())
    }
}

fun User.Companion.random(): User {
    return User(UserId(UUID.randomUUID()), UserName(UUID.randomUUID().toString()))
}