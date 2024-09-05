package com.craftsilicon.shumul.agency.data.security.interaction

import android.os.CountDownTimer
import com.craftsilicon.shumul.agency.data.source.storage.pref.StorageDataSource
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserInteractionWatcher @Inject constructor(
    private val dataSource: StorageDataSource
) : InteractionDataSource {

    private var countDownTimer: CountDownTimer? = null

    override fun onUserInteracted() {
        setTimer()
    }

    override fun setTimer() {
        AppLogger.instance.appLog("Interaction", "YES")
        updateTimeout()
    }


    private fun updateTimeout() {
        if (countDownTimer != null) timerControl(false)
        val timeout = dataSource.timeout.value
        val time = timeout ?: startTime
        countDownTimer = OTPCountDownTimer(
            startTime = time,
            interval = INTERVAL,
            this
        )
        setTime()
    }

    private fun setTime() {
        dataSource.setInactivity(false)
        timerControl(true)
        done(false)
    }

    private fun timerControl(startTimer: Boolean) {
        if (startTimer) {
            countDownTimer!!.start()
        } else {
            countDownTimer!!.cancel()
            countDownTimer = null
        }
    }


    companion object {
        private var startTime = (24 * 1000).toLong()
        private const val INTERVAL = (3 * 1000).toLong()
    }

    override fun timer(str: String) {
        AppLogger.instance.appLog("TIMER", str)
    }

    override fun done(boolean: Boolean) {
        if (boolean) {
            timerControl(false)
            dataSource.setInactivity(true)
        }
    }
}

class OTPCountDownTimer(
    startTime: Long, interval: Long,
    private val interaction: InteractionDataSource
) : CountDownTimer(startTime, interval) {
    override fun onFinish() {
        interaction.done(true)
    }

    override fun onTick(millisUntilFinished: Long) {
        val currentTime = millisUntilFinished / 1000
        interaction.timer(
            "" + currentTime / 60 + " : " +
                    if (currentTime % 60 >= 10) currentTime % 60 else "0" + currentTime % 60
        )
    }
}