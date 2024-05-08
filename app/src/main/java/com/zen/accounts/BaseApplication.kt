package com.zen.accounts

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.zen.accounts.repository.ExpenseRepository
import com.zen.accounts.workmanager.UploadExpenseWorker
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication: Application(), Configuration.Provider {
    @Inject
    lateinit var repo : ExpenseRepository
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setWorkerFactory(
                UploadExpenseWorkerFactory(repo)
            )
            .build()
}

class UploadExpenseWorkerFactory(
    private val repo : ExpenseRepository
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return UploadExpenseWorker(
            appContext,
            workerParameters,
            repo
        )
    }

}