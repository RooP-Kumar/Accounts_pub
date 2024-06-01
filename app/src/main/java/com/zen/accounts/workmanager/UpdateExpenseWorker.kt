package com.zen.accounts.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.zen.accounts.api.resource.Resource
import com.zen.accounts.ui.screens.common.work_manager_input_data
import com.zen.accounts.ui.screens.common.work_manager_output_data
import com.zen.accounts.workmanager.worker_repository.WorkRepository

class UpdateExpenseWorker(
    context : Context,
    workParams : WorkerParameters,
    private val repo : WorkRepository
) : CoroutineWorker(context, workParams){
    override suspend fun doWork(): Result {
        val uid = inputData.getString(work_manager_input_data)
        val outputData = Data.Builder()
        return if (uid != null) {
            return when(val res = repo.updateExpenseToFirebase(uid)) {
                is Resource.SUCCESS -> {
                    repo.clearUpdatedExpenseFromBackupTable()
                    Result.success(outputData.putString(work_manager_output_data, res.value.message).build())
                }
                is Resource.FAILURE -> {
                    Result.failure(outputData.putString(work_manager_output_data, res.message).build())
                }
            }
        } else {
            Result.failure(outputData.putString(work_manager_output_data, "No user found.").build())
        }
    }
}