package hu.matusek.plugins

import com.aallam.openai.client.OpenAI
import hu.matusek.service.WhisperServiceImpl
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.async
import kotlinx.html.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Instant

fun Application.configureRouting() {
    val whisperServiceImpl = WhisperServiceImpl(OpenAI(System.getenv("OPENAI_API_KEY")))
    Files.createDirectories(Paths.get("./uploads"))
    routing {
        authenticate("auth-basic") {
            post("/transcribe") {
                val multipartData = call.receiveMultipart()
                multipartData.forEachPart { part ->
                    when (part) {
                        is PartData.FileItem -> {
                            val fileName = part.originalFileName as String
                            val fileBytes = part.streamProvider().readBytes()
                            val path = "./uploads/${Instant.now().toEpochMilli()}_${fileName}"
                            File(path).writeBytes(fileBytes)
                            val text = async { whisperServiceImpl.transcribeAudio(path) }.await()
                            File(path).delete()
                            call.respondText(text)
                        }

                        else -> {}
                    }
                    part.dispose()
                }
            }

            get("/") {
                call.respondHtml {
                    body {
                        form(
                            action = "/transcribe",
                            encType = FormEncType.multipartFormData,
                            method = FormMethod.post
                        ) {
                            p {
                                +"Audio: "
                                fileInput(name = "audio")
                            }
                            p {
                                submitInput { value = "Submit audio" }
                            }
                        }
                    }
                }
            }
        }
    }
}
