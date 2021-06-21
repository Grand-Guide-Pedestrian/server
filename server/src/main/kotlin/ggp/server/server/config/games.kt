package ggp.server.server.config

import ggp.server.server.game.Game
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.ConcurrentHashMap

val gamesModule = module {
    single(named("games")) {
        ConcurrentHashMap<String, Game>()
    }
}
