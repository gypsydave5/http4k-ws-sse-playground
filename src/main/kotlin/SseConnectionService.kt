package org.example

import org.http4k.sse.Sse
import org.http4k.sse.SseMessage

class SseConnectionService(private val map: MutableMap<String, Set<Sse>> = mutableMapOf()) {
    fun addConnection(name: String, sse: Sse) {
        sse.onClose { removeConnection(name, sse) }

        val conns = map[name] ?: emptyList()
        map[name] = (conns + sse).toSet()
    }

    fun removeConnection(name: String, sse: Sse) {
        val connections = map[name] ?: return
        val newConnections = connections - sse

        if (newConnections.isNotEmpty()) {
            map[name] = newConnections
        } else {
            map.remove(name)
        }
    }

    fun send(event: SseMessage.Event) {
        map.values.flatten().forEach { it.send(event) }
    }

    override fun toString(): String {
        return map.entries.joinToString ("\n") { (k, v) ->
            k + ": [ " + v.joinToString(",") { it.hashCode().toString() } + " ]"
        }
    }
}

