package com.zen.accounts.workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.zen.accounts.ui.screens.common.work_manager_input_data
import com.zen.accounts.workmanager.worker_repository.WorkerRepository
import kotlinx.coroutines.flow.collectLatest

class PeriodicWorker(
    context : Context,
    workerParameters: WorkerParameters,
    private val workerRepository: WorkerRepository
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val uid = inputData.getString(work_manager_input_data)
        return if(uid != null) {
            val requestIds = workerRepository.startUploadingNow(uid)
            workerRepository.getWorkInfoById(requestIds[0]).collectLatest {
                Log.d("asdf", "doWork: create periodic work ----> ${it.state}")
            }

            workerRepository.getWorkInfoById(requestIds[1]).collectLatest {
                Log.d("asdf", "doWork: update periodic work ----> ${it.state}")
            }

            workerRepository.getWorkInfoById(requestIds[2]).collectLatest {
                Log.d("asdf", "doWork: delete periodic work ----> ${it.state}")
            }
            Result.success()
        } else {
            Result.failure()
        }
    }
}