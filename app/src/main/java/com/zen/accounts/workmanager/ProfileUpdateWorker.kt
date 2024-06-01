package com.zen.accounts.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.zen.accounts.api.resource.Resource
import com.zen.accounts.repository.AuthRepository
import com.zen.accounts.ui.screens.common.work_manager_input_data
import com.zen.accounts.ui.screens.common.work_manager_output_data
import com.zen.accounts.utility.stringToUser

class ProfileUpdateWorker(
    context : Context,
    param : WorkerParameters,
    private val repository : AuthRepository
) : CoroutineWorker(
    context,
    param
) {
    override suspend fun doWork(): Result {
        return when(val res = repository.uploadProfilePic()) {
            is Resource.SUCCESS -> {
                val outputData = workDataOf(work_manager_output_data to res.value.message)
                Result.success(outputData)
            }
            is Resource.FAILURE -> {
                val outputData = workDataOf(work_manager_output_data to res.message)
                Result.failure(outputData)
            }
        }
    }
}