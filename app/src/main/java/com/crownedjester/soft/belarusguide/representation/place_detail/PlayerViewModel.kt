package com.crownedjester.soft.belarusguide.representation.place_detail

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crownedjester.soft.belarusguide.representation.util.BundleUtil.SOUND_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

private const val TAG = "PlayerViewModel"

@HiltViewModel
class PlayerViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _remainingSeconds = MutableStateFlow(0)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds

    private var playerTimerJob: Job? = null

    private var mediaPlayer: MediaPlayer? = null

    init {
        viewModelScope.launch {
            savedStateHandle.get<String>(SOUND_KEY)?.let {
                mediaPlayer = prepareMediaPlayer(it)
            }
        }
    }

    fun onEvent(event: PlayerEvent) {
        when (event) {

            PlayerEvent.OnPause -> {
                Log.d(TAG, "Invoked onPause")
                mediaPlayer?.pause()
                cancelPlayerJob()
            }

            PlayerEvent.OnStart -> {
                Log.d(TAG, "Invoked onStart")
                mediaPlayer?.start()
                runTimer()
            }

            PlayerEvent.OnStop -> {
                Log.d(TAG, "Invoked onStop")
                mediaPlayer?.apply {
                    stop()
                    _remainingSeconds.tryEmit(duration)
                }
                cancelPlayerJob()
            }

            PlayerEvent.OnRelease -> {
                Log.d(TAG, "Invoked onRelease")
                cancelPlayerJob()
                mediaPlayer?.release()
            }
        }

    }

    fun getDurationSeconds(): Int = if (mediaPlayer == null) -1 else mediaPlayer!!.duration / 1000

    fun toStringFormat(): String =
        (_remainingSeconds.value).let {
            val mins = it / 60
            val secs = it % 60
            "${if (mins < 10) "0$mins" else mins}:${if (secs < 10) "0$secs" else secs}"
        }

    private suspend fun prepareMediaPlayer(sound: String) =
        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(sound)
                prepare()
                setOnPreparedListener {
                    _remainingSeconds.tryEmit(duration / 1000)
                    Log.i("PlayerViewModel", "MediaPlayer has prepared")
                }

                setOnCompletionListener {
                    cancelPlayerJob()
                    _remainingSeconds.tryEmit(duration / 1000)
                }

            }
        }

    private fun runTimer() {
        playerTimerJob =
            viewModelScope.launch {
                setupTimer()
                    .onCompletion {
                        _remainingSeconds.apply {
                            emit(value)
                            Log.i("PlayerVM", "OnCompletion Invoked")
                        }
                    }
                    .collect {
                        _remainingSeconds.emit(it)
                    }
            }
    }

    private fun setupTimer() =
        (_remainingSeconds.value downTo 0).asFlow()
            .onEach { delay(1000) }
            .conflate()

    private fun cancelPlayerJob() {
        playerTimerJob?.cancel()
        playerTimerJob = null
    }

}