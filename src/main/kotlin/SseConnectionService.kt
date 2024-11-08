package org.example

import org.example.domain.User
import org.example.domain.UserId
import org.http4k.sse.Sse

class SseConnectionService(private val map: MutableMap<UserId, Set<Sse>> = mutableMapOf()) {
    fun addConnection(userId: UserId, sse: Sse) {
        sse.onClose { removeConnection(userId, sse) }

        val connections = map[userId] ?: emptyList()
        map[userId] = (connections + sse).toSet()
    }

    fun removeConnection(name: UserId, sse: Sse) {
        val connections = map[name] ?: return
        val newConnections = connections - sse

        if (newConnections.isNotEmpty()) {
            map[name] = newConnections
        } else {
            map.remove(name)
        }
    }

    fun connectionsFor(user: User): Set<Sse> {
        return map.getOrDefault(user.id, emptySet())
    }

    fun allConnections(): Set<Sse> {
        return map.values.flatten().toSet()
    }

    override fun toString(): String {
        return map.entries.joinToString ("\n") { (k, v) ->
            k.toString() + ": [ " + v.joinToString(",") { it.hashCode().toString() } + " ]"
        }
    }
}

