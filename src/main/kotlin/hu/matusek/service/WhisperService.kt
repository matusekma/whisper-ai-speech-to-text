package hu.matusek.service

interface WhisperService {

    suspend fun transcribeAudio(filePath: String): String

}