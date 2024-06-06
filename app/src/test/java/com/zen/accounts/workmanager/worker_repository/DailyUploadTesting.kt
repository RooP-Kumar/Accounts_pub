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
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}