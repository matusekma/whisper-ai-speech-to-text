package hu.matusek.service

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.audio.TranslationRequest
import com.aallam.openai.api.file.FileSource
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import io.ktor.client.*
import io.ktor.client.request.*
import okio.FileSystem
import okio.Path.Companion.toPath


class WhisperServiceImpl(private val openAIClient: OpenAI) : WhisperService {

    @OptIn(BetaOpenAI::class)
    override suspend fun transcribeAudio(filePath: String): String {
        val transcriptionRequest = TranslationRequest(
            audio = FileSource(path = filePath.toPath(), fileSystem = FileSystem.SYSTEM),
            model = ModelId("whisper-1"),
        )
        val transcription =
            openAIClient.translation(transcriptionRequest) // TODO this is a bug in the client, it should be transcription
        println(transcription.text)
        return transcription.text
    }

}