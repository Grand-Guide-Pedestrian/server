package ggp.server.server.routes

import io.ktor.http.content.defaultResource
import io.ktor.http.content.files
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.http.content.staticBasePackage
import io.ktor.routing.Route

fun Route.game() {
    static("game") {
        resources("game")
        defaultResource("index.html")
    }
    static("static") {
        resources("game/static")
    }
}
