package com.zen.accounts.repository

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.zen.accounts.ui.screens.common.daily_worker_name
import com.zen.accounts.ui.screens.common.monthly_worker_name
import com.zen.accounts.ui.screens.common.single_worker_name
import com.zen.accounts.ui.screens.common.weekly_worker_name
import com.zen.accounts.ui.screens.common.work_manager_input_data
import com.zen.accounts.workmanager.UploadExpenseWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import java.time.Duration
import java.time.Period
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkerRepository @Inject constructor(
    @ApplicationContext private val context : Context
) {

    companion object {
        private lateinit var workManager : WorkManager
    }

    init {
        workManager = WorkManager.getInstance(context)
    }

    // Single Work Request
    fun startUploadingNow(uid : String, tag : String) {
        val inputData = Data.Builder()
            .putString(work_manager_input_data, uid)
            .build()

        val customConstraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .build()

        val request = OneTimeWorkRequestBuilder<UploadExpenseWorker>()
            .addTag(tag)
            .setConstraints(customConstraints)
            .setInputData(inputData)
            .build()

        workManager.cancelAllWork()
        workManager.beginUniqueWork(
            single_worker_name,
            ExistingWorkPolicy.REPLACE,
            request
        ).enqueue()

    }

    // Daily Work Request
    fun startUploadingDaily(uid : String, tag : String) {
        val inputData = Data.Builder()
            .putString(work_manager_input_data, uid)
            .build()

        val customConstraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .build()

        val request = PeriodicWorkRequestBuilder<UploadExpenseWorker>(repeatInterval = 1, repeatIntervalTimeUnit = TimeUnit.DAYS)
            .addTag(tag)
            .setConstraints(customConstraints)
            .setInputData(inputData)
            .build()

        workManager.cancelAllWork()
        workManager.enqueueUniquePeriodicWork(
            daily_worker_name,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )

    }

    // Weekly Work Request
    fun startUploadingWeekly(uid: String, tag: String) {
        val inputData = Data.Builder()
            .putString(work_manager_input_data, uid)
            .build()

        val customConstraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .build()

        val request = PeriodicWorkRequestBuilder<UploadExpenseWorker>(repeatInterval = 7, repeatIntervalTimeUnit = TimeUnit.DAYS)
            .addTag(tag)
            .setConstraints(customConstraints)
            .setInputData(inputData)
            .build()


        workManager.cancelAllWork()
        workManager.enqueueUniquePeriodicWork(
            weekly_worker_name,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    // Monthly Work Request
    fun startUploadingMonthly(uid: String, tag: String) {
        val inputData = Data.Builder()
            .putString(work_manager_input_data, uid)
            .build()

        val customConstraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .build()

        val request = PeriodicWorkRequestBuilder<UploadExpenseWorker>(repeatInterval = 30, repeatIntervalTimeUnit = TimeUnit.DAYS)
            .addTag(tag)
            .setConstraints(customConstraints)
            .setInputData(inputData)
            .build()


        workManager.cancelAllWork()
        workManager.enqueueUniquePeriodicWork(
            monthly_worker_name,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }


    fun getWorkInfoByTag(tag: String): Flow<List<WorkInfo>?> {
        return workManager.getWorkInfosByTagFlow(tag)
    }
}