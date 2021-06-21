package ggp.server.server.game

import java.util.concurrent.atomic.AtomicReference

data class Game(
    val id: String,
    var hero: AtomicReference<Hero> = AtomicReference(),
    var player: AtomicReference<Player> = AtomicReference(),
)
