package ggp.server.server.game

import io.ktor.http.cio.websocket.WebSocketSession

class Player(
    val session: WebSocketSession
) {
    override fun toString(): String {
        return "Player"
    }
}
