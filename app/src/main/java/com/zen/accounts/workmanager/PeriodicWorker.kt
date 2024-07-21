package com.zen.accounts.workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.zen.accounts.ui.screens.common.work_manager_input_data
import com.zen.accounts.workmanager.worker_repository.WorkerRepository

class PeriodicWorker(
    context : Context,
    workerParameters: WorkerParameters,
    private val workerRepository: WorkerRepository
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val uid = inputData.getString(work_manager_input_data)
        return if(uid != null) {
            workerRepository.startUploadingNow(uid, true)
            Result.success()
        } else {
            Result.failure()
        }
    }
}