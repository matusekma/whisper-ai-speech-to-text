package hu.matusek

import hu.matusek.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureSerialization()
    configureSecurity()
    configureMonitoring()
    configureRouting()
}
