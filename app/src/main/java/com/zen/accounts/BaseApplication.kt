package com.zen.accounts

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.zen.accounts.workmanager.DeleteExpenseWorker
import com.zen.accounts.workmanager.PeriodicWorker
import com.zen.accounts.workmanager.UpdateExpenseWorker
import com.zen.accounts.workmanager.UploadExpenseWorker
import com.zen.accounts.workmanager.worker_repository.WorkRepository
import com.zen.accounts.workmanager.worker_repository.WorkerRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication: Application(), Configuration.Provider {
    @Inject
    lateinit var repo : WorkRepository

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(
                CombineWorkerFactory(repo)
            )
            .build()
}

class CombineWorkerFactory(
    private val repo: WorkRepository
): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        Log.d("asdf", "Running in the Create worker factory function")
        return when(workerClassName) {
            UploadExpenseWorker::class.java.name -> {
                UploadExpenseWorkerFactory(repo).createWorker(appContext, workerClassName, workerParameters)
            }
            UpdateExpenseWorker::class.java.name -> {
                UpdateExpenseWorkerFactory(repo).createWorker(appContext, workerClassName, workerParameters)
            }
            DeleteExpenseWorker::class.java.name -> {
                DeleteExpenseWorkerFactory(repo).createWorker(appContext, workerClassName, workerParameters)
            }
            PeriodicWorker::class.java.name -> {
                PeriodicWorkerFactory(WorkerRepository(appContext)).createWorker(appContext, workerClassName, workerParameters)
            }
            else -> null
        }
    }

}

class PeriodicWorkerFactory(
    private val repo: WorkerRepository
) : WorkerFactory(){
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return PeriodicWorker(
            appContext,
            workerParameters,
            repo
        )
    }

}

class UploadExpenseWorkerFactory(
    private val repo : WorkRepository
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return UploadExpenseWorker(
            appContext,
            workerParameters,
            repo
        )
    }
}

class UpdateExpenseWorkerFactory(
    private val repo: WorkRepository
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return UpdateExpenseWorker(
            appContext,
            workerParameters,
            repo
        )
    }

}
class DeleteExpenseWorkerFactory(
    private val repo: WorkRepository
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return DeleteExpenseWorker(
            appContext,
            workerParameters,
            repo
        )
    }

}