package com.zen.accounts.workmanager.worker_repository

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.zen.accounts.ui.screens.common.daily_work_request_tag
import com.zen.accounts.ui.screens.common.daily_worker_name
import com.zen.accounts.ui.screens.common.monthly_work_request_tag
import com.zen.accounts.ui.screens.common.monthly_worker_name
import com.zen.accounts.ui.screens.common.single_work_request_tag
import com.zen.accounts.ui.screens.common.weekly_work_request_tag
import com.zen.accounts.ui.screens.common.weekly_worker_name
import com.zen.accounts.ui.screens.common.work_manager_input_data
import com.zen.accounts.workmanager.DeleteExpenseWorker
import com.zen.accounts.workmanager.PeriodicWorker
import com.zen.accounts.workmanager.UpdateExpenseWorker
import com.zen.accounts.workmanager.UploadExpenseWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import java.util.UUID
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

    // Cancel All Worker
    fun cancelAllWorker() {
        workManager.cancelAllWork()
    }

    // Single Work Request
    fun startUploadingNow(uid : String) : List<UUID> {
        val inputData = workDataOf(work_manager_input_data to uid)

        val customConstraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .build()

        val createRequest = OneTimeWorkRequestBuilder<UploadExpenseWorker>()
            .setConstraints(customConstraints)
            .addTag(single_work_request_tag)
            .setInputData(inputData)
            .build()

        val updateRequest = OneTimeWorkRequestBuilder<UpdateExpenseWorker>()
            .setConstraints(customConstraints)
            .addTag(single_work_request_tag)
            .setInputData(inputData)
            .build()

        val deleteRequest = OneTimeWorkRequestBuilder<DeleteExpenseWorker>()
            .setConstraints(customConstraints)
            .addTag(single_work_request_tag)
            .setInputData(inputData)
            .build()

        workManager.cancelAllWork()
        // Not recommended. Because create, update and delete can be done in single transaction.
        // I am doing it because of learning purpose
        // I wanted to learning chaining the work
        workManager.beginWith(createRequest)
            .then(updateRequest)
            .then(deleteRequest)
            .enqueue()

        return listOf(createRequest.id, updateRequest.id, deleteRequest.id)

    }

    // Daily Work Request
    fun startUploadingDaily(uid : String) {
        val inputData = Data.Builder()
            .putString(work_manager_input_data, uid)
            .build()

        val customConstraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .build()

        val request = PeriodicWorkRequestBuilder<PeriodicWorker>(repeatInterval = 1, repeatIntervalTimeUnit = TimeUnit.DAYS)
            .addTag(daily_work_request_tag)
            .setConstraints(customConstraints)
            .setInputData(inputData)
            .build()

        workManager.cancelAllWork()
        workManager
            .enqueueUniquePeriodicWork(
                daily_worker_name,
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )

    }

    // Weekly Work Request
    fun startUploadingWeekly(uid: String) {
        val inputData = Data.Builder()
            .putString(work_manager_input_data, uid)
            .build()

        val customConstraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .build()

        val request = PeriodicWorkRequestBuilder<PeriodicWorker>(repeatInterval = 7, repeatIntervalTimeUnit = TimeUnit.DAYS)
            .addTag(weekly_work_request_tag)
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
    fun startUploadingMonthly(uid: String) {
        val inputData = Data.Builder()
            .putString(work_manager_input_data, uid)
            .build()

        val customConstraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .build()

        val request = PeriodicWorkRequestBuilder<PeriodicWorker>(repeatInterval = 30, repeatIntervalTimeUnit = TimeUnit.DAYS)
            .addTag(monthly_work_request_tag)
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

    fun getWorkInfoById(id: UUID): Flow<WorkInfo> {
        return workManager.getWorkInfoByIdFlow(id)
    }

    fun getWorkInfoByTag(tag: String): Flow<List<WorkInfo>?> {
        return workManager.getWorkInfosByTagFlow(tag)
    }
}