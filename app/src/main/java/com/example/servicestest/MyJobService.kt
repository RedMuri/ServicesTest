package com.example.servicestest

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class MyJobService : JobService() {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        log("onStartCommand")
        scope.launch {
            for (i in 0 until 10) {
                delay(1000)
                log("Timer $i")
            }
            jobFinished(p0, false)
        }
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.d("muri", "JobService: $message")
    }

    companion object{
        const val JOB_ID = 111
    }
}