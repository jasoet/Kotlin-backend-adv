package id.jasoet.auth

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

object App {
    val greeting: String
        get() {
            return "Hello world."
        }
}

data class Hello(val from: String, val to: String)

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        routing {
            get("/") {
                call.respondText(App.greeting, ContentType.Text.Plain)
            }

            get("/hello") {
                val name = call.parameters["name"] ?: "Jasoet"
                val respond = Hello(from = name, to = "The World!")
                call.respond(respond)
            }
        }
    }

    server.start(wait = false)
}