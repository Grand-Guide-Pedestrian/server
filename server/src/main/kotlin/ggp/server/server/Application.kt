package ggp.server.server

import ggp.server.server.config.gamesModule
import ggp.server.server.routes.hero
import ggp.server.server.routes.player
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.routing.routing
import io.ktor.websocket.WebSockets
import org.koin.ktor.ext.Koin

fun Application.main() {
    install(WebSockets)
    install(Koin) {
        modules(
            gamesModule
        )
    }

    routing {
        player()
        hero()
    }
}
