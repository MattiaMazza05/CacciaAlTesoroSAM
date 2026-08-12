package com.example.cacciaaltesorosam.media.playback

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}