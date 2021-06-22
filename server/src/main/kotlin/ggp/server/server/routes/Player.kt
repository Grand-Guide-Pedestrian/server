package ggp.server.server.routes

import ggp.server.server.game.Game
import ggp.server.server.game.Player
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.close
import io.ktor.routing.Route
import io.ktor.websocket.webSocket
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import org.apache.logging.log4j.LogManager
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject
import java.util.concurrent.ConcurrentHashMap

fun Route.player() {
    val logger = LogManager.getLogger("ggp.server.server.routes.Player")
    val games: ConcurrentHashMap<String, Game> by inject(named("games"))

    webSocket("/game/{gameId}/player") {
        val gameId = call.parameters["gameId"]?.takeIf { it.isNotBlank() }

        if (gameId.isNullOrBlank()) {
            this.close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Empty game ID!"))

            return@webSocket
        }

        logger.debug("New player has connected to the game #{}", gameId)

        val game = games.getOrPut(gameId) { Game(gameId) }

        if (!game.player.compareAndSet(null, Player(this))) {
            logger.warn("Kicking the player off the game")

            this.close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "No more players allowed!"))

            return@webSocket
        }

        try {
            for (frame in incoming) {
                logger.debug("Game #{}, Frame: {}", gameId, frame)

                try {
                    game.hero.get()?.session?.send(frame)
                } catch (e: Throwable) {
                    logger.error("Failed to send the frame to the hero", e)
                }
            }

            logger.info("The player has disconnected from the game #{}", gameId)
        } catch (e: ClosedReceiveChannelException) {
            logger.info("ClosedReceiveChannelException (reason: {})", closeReason.await())
        } catch (e: Throwable) {
            logger.error("Reason: {}", closeReason.await(), e)
        } finally {
            game.player.set(null)
        }
    }
}
