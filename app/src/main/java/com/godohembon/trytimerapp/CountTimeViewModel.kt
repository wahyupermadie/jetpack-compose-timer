package com.godohembon.trytimerapp

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.godohembon.trytimerapp.utils.Helper.getCountDownHint
import com.godohembon.trytimerapp.utils.TimeCount

class CountTimeViewModel: ViewModel() {

    companion object {
        const val HOUR_IN_MIN = 60
        const val MIN_IN_SEC = 60
        const val MSEC_IN_SEC = 1000
    }

    private var  countDownTimer : CountDownTimer? = null

    private val _isFinish = MutableLiveData(false)
    val isFinish : LiveData<Boolean>
        get() = _isFinish

    private val _seconds = MutableLiveData(0)
    val seconds : LiveData<Int>
        get() = _seconds

    private val _minutes = MutableLiveData(0)
    val minutes : LiveData<Int>
        get() = _minutes

    private val _hour = MutableLiveData(0)
    val hour: LiveData<Int>
        get() = _hour

    private val _progress = MutableLiveData(1.0f)
    val progress : LiveData<Float>
        get() = _progress

    private val _curTime = MutableLiveData("00 - 00 - 00")
    val curTime : LiveData<String>
        get() = _curTime

    private val _timeChips = MutableLiveData(getCountDownHint())
    val timeChips : LiveData<MutableList<TimeCount>>
        get() = _timeChips

    private var totalTime = 0L

    fun setTime(seconds: Long) {
        totalTime = seconds * 1000
        setCurTime(totalTime)
    }

    fun startCountDown() {
        if (countDownTimer != null) {
            stopCountDown()
        }

        countDownTimer = object : CountDownTimer(totalTime, 1000) {
            override fun onTick(millisecs: Long) {
                setCurTime(millisecs)
                _progress.postValue(-(millisecs.toFloat() / totalTime.toFloat()))
            }

            override fun onFinish() {
                _progress.postValue(1.0f)
                _isFinish.value = true
            }
        }
        countDownTimer?.start()
        _isFinish.value = false
    }

    private fun setCurTime(millisecs: Long) {
        val sec = (millisecs / MSEC_IN_SEC % MIN_IN_SEC).toInt()
        if (sec != seconds.value) {
            _seconds.value = sec
        }

        val min = (millisecs / MSEC_IN_SEC / MIN_IN_SEC % MIN_IN_SEC).toInt()
        if (minutes.value != min) {
            _minutes.value = min
        }

        val hour = (millisecs / MSEC_IN_SEC / MIN_IN_SEC / HOUR_IN_MIN % HOUR_IN_MIN).toInt()
        if (this@CountTimeViewModel.hour.value != hour) {
            _hour.value = hour
        }
        setCountDownTime()
    }

    private fun setCountDownTime() {
        val curHour = if (hour.value!! >= 10) hour.value else "0${hour.value}"
        val curMin = if (minutes.value!! >= 10) minutes.value else "0${minutes.value}"
        val curSec = if (seconds.value!! >= 10) seconds.value else "0${seconds.value}"
        _curTime.value = "$curHour - $curMin - $curSec"
    }

    fun stopCountDown() {
        countDownTimer?.cancel()
        _progress.postValue(1.0f)
        _isFinish.value = true
    }
}