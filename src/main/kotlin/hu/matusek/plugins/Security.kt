package hu.matusek.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureSecurity() {
    val username = System.getenv("USERNAME")
    val password = System.getenv("PASSWORD")
    authentication {
        basic(name = "auth-basic") {
            realm = "Whisper"
            validate { credentials ->
                if (credentials.name == username &&
                    credentials.password == password
                ) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
}
