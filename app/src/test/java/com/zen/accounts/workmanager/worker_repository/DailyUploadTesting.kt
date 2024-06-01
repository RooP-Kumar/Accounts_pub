package com.zen.accounts.workmanager.worker_repository

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.work.impl.utils.taskexecutor.TaskExecutor
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.impl.TestWorkManagerImpl
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.impl.utils.taskexecutor.SerialExecutor
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class DailyUploadTesting {
    private lateinit var context: Context
    private lateinit var executor : Executor

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        executor = Executors.newSingleThreadExecutor()
        val config = androidx.work.Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(executor)
            .build()
        
    }

    @Test
    fun startUploadingDaily() {

    }
}