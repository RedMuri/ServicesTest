package com.example.servicestest

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import android.util.Log
import kotlinx.coroutines.*

class MyJobService : JobService() {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        log("onStartCommand")
        scope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                var workItem = params?.dequeueWork()
                while (workItem!=null){
                    val page = workItem.intent.getIntExtra(PAGE_EXTRA,0)
                    for (i in 0 until 5) {
                        delay(1000)
                        log("Timer $i $page")
                    }
                    params?.completeWork(workItem)
                    workItem = params?.dequeueWork()
                }
                jobFinished(params, false)
            }
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
        private const val PAGE_EXTRA = "page"

        fun newIntent(page: Int): Intent {
            return Intent().apply {
                putExtra(PAGE_EXTRA,page)
            }
        }
    }
}