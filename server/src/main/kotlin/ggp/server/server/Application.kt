package ggp.server.server

import ggp.server.server.config.gamesModule
import ggp.server.server.routes.hero
import ggp.server.server.routes.player
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.http.cio.websocket.pingPeriod
import io.ktor.http.cio.websocket.timeout
import io.ktor.routing.routing
import io.ktor.websocket.WebSockets
import org.koin.ktor.ext.Koin
import java.time.Duration

fun Application.main() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(1)
        timeout = Duration.ofSeconds(2)
    }
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
