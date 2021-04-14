package com.heycode.aizi.dailyroutine

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.widget.Toast
import com.heycode.aizi.R

var media: MediaPlayer? = null

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        media = MediaPlayer.create(context, R.raw.alarmmusic)
        media!!.start()
        Toast.makeText(context, "Alarm is running..", Toast.LENGTH_LONG).show()
    }

    companion object {
        fun stopMedia() {
            if (media != null) {
                media!!.stop()
            }
        }
    }
}
