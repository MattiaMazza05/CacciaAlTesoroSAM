package com.example.cacciaaltesorosam.media.record

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AndroidAudioRecorder(
    private val context: Context
) : AudioRecorder {

    private var recorder: MediaRecorder? = null

    private fun createRecorder(): MediaRecorder { //questo controllo mi serve per le versioni di android >= 12 e per essere retrocompatibile
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder((context))
        } else MediaRecorder()
    }

    private fun getNewFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "AUDIO_$timeStamp.m4a"
        return File(context.filesDir, fileName)
    }

    override fun start(): File {
        val audioFile = getNewFile()

        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(audioFile.absolutePath)

            prepare()
            start()

            recorder = this
        }
        return audioFile
    }

    override fun stop() {
        recorder?.stop()
        recorder?.reset()
        recorder = null
    }
}