package ggp.server.server.routes

import ggp.server.server.game.Game
import ggp.server.server.game.Hero
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.close
import io.ktor.routing.Route
import io.ktor.websocket.webSocket
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import org.apache.logging.log4j.LogManager
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject
import java.util.concurrent.ConcurrentHashMap

fun Route.hero() {
    val logger = LogManager.getLogger("ggp.server.server.routes.Hero")
    val games: ConcurrentHashMap<String, Game> by inject(named("games"))

    webSocket("/game/{gameId}/hero") {
        val gameId = call.parameters["gameId"]?.takeIf { it.isNotBlank() }

        if (gameId.isNullOrBlank()) {
            this.close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Empty game ID!"))

            return@webSocket
        }

        logger.debug("New hero has connected to the game #{}", gameId)

        val game = games.getOrPut(gameId) { Game(gameId) }

        game.hero.set(Hero(this))

        try {
            closeReason.await()

            logger.info("The hero has disconnected from the game #{}", gameId)
        } catch (e: ClosedReceiveChannelException) {
            logger.info("ClosedReceiveChannelException (reason: {})", closeReason.await())
        } catch (e: Throwable) {
            logger.error("Reason: {}", closeReason.await(), e)
        } finally {
            game.hero.set(null)
        }
    }
}
