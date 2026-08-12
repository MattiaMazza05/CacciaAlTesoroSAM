package com.example.cacciaaltesorosam.media.record

import java.io.File

interface AudioRecorder {
    fun start(): File
    fun stop()
}