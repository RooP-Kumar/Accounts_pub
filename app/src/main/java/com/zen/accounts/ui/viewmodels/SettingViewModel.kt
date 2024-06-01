package com.zen.accounts.ui.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.zen.accounts.api.resource.Resource
import com.zen.accounts.repository.AuthRepository
import com.zen.accounts.repository.ExpenseItemRepository
import com.zen.accounts.repository.ExpenseRepository
import com.zen.accounts.repository.MediaStoreRepository
import com.zen.accounts.ui.screens.common.BackupPlan
import com.zen.accounts.ui.screens.common.LoadingState
import com.zen.accounts.ui.screens.common.work_manager_output_data
import com.zen.accounts.ui.screens.main.setting.SettingUiState
import com.zen.accounts.utility.io
import com.zen.accounts.workmanager.worker_repository.WorkerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val expenseRepository: ExpenseRepository,
    private val expenseItemRepository: ExpenseItemRepository,
    private val workerRepository: WorkerRepository,
    private val mediaStoreRepository: MediaStoreRepository
) : ViewModel() {
    val settingUIState by lazy { SettingUiState() }

    fun logout() {
        viewModelScope.launch {
            settingUIState.apply {
                loadingState.value = LoadingState.LOADING
                if (expenseRepository.isBackupTableEmpty()) {
                    expenseRepository.clearExpenseTable()
                    expenseItemRepository.clearExpenseItemTable()
                    authRepository.logout()
                    expenseRepository.dataStore.logoutUser()
                    user.value = null
                    profilePic.value = null

                    delay(500)
                    loadingState.value = LoadingState.SUCCESS
                    showSnackBarText.value = "Logout Successfully"
                    showSnackBar()
                } else {
                    loadingState.value = LoadingState.IDLE
                    showConfirmationPopUp.value = true
                }
            }
        }
    }

    fun logoutConfirmation(logoutWithoutBackup: Boolean) {
        viewModelScope.launch {
            settingUIState.apply {
                if (!logoutWithoutBackup) {
                    startSingleUploadRequest(true)
                } else {
                    expenseRepository.clearBackupTable()
                    expenseRepository.clearExpenseTable()
                    expenseItemRepository.clearExpenseItemTable()
                    authRepository.logout()
                    expenseRepository.dataStore.logoutUser()
                }
            }
        }
    }

    fun showSnackBar() {
        viewModelScope.launch {
            settingUIState.apply {
                delay(200)
                showSnackBar.value = true
                delay(2500)
                showSnackBar.value = false
            }
        }
    }

    fun getBackupPlan() {
        viewModelScope.launch {
            settingUIState.apply {
                val backupPlan = expenseRepository.dataStore.getBackupPlan()
                if (backupDropDownText.value == BackupPlan.Off && backupPlan != BackupPlan.Off) {
                    backupDropDownText.value = backupPlan
                }
            }
        }
    }

    suspend fun updateBackupPlan() {
        viewModelScope.launch(Dispatchers.IO) {
            settingUIState.apply {
                expenseRepository.dataStore.updateBackupPlan(if (backupDropDownText.value == BackupPlan.Now) BackupPlan.Off else backupDropDownText.value)
            }
        }
    }

    fun startSingleUploadRequest(fromLogoutConfirmation: Boolean = false) {
        viewModelScope.launch {
            expenseRepository.dataStore.getUser()?.let { user ->
                settingUIState.backupLoadingState.value = LoadingState.LOADING
                val requestIds =
                    workerRepository.startUploadingNow(user.uid)
                workerRepository.getWorkInfoById(requestIds[2]).collectLatest {
                    when (it.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            if (fromLogoutConfirmation) {
                                expenseRepository.clearExpenseTable()
                                expenseItemRepository.clearExpenseItemTable()
                                authRepository.logout()
                                expenseRepository.dataStore.logoutUser()
                            }
                            delay(200)
                            settingUIState.backupDropDownText.value = BackupPlan.Off
                            settingUIState.backupLoadingState.value = LoadingState.SUCCESS
                        }

                        WorkInfo.State.FAILED -> {
                            delay(500)
                            settingUIState.backupDropDownText.value = BackupPlan.Off
                            settingUIState.backupLoadingState.value = LoadingState.FAILURE
                        }

                        WorkInfo.State.RUNNING -> {
                            settingUIState.backupLoadingState.value = LoadingState.LOADING
                        }

                        WorkInfo.State.BLOCKED -> {
                            settingUIState.backupLoadingState.value = LoadingState.LOADING
                        }

                        else -> {}
                    }
                }
            }
        }
    }


    fun startDailyUploadRequest() {
        viewModelScope.launch {
            expenseRepository.dataStore.getUser()?.let { user ->
                workerRepository.startUploadingDaily(user.uid)
            }
        }
    }

    fun startWeeklyUploadRequest() {
        viewModelScope.launch {
            expenseRepository.dataStore.getUser()?.let { user ->
                workerRepository.startUploadingWeekly(user.uid)
            }
        }
    }

    fun startMonthlyUploadRequest() {
        viewModelScope.launch {
            expenseRepository.dataStore.getUser()?.let { user ->
                workerRepository.startUploadingMonthly(user.uid)
            }
        }
    }

    suspend fun cancelAllWork() {
        io {
            workerRepository.cancelAllWorker()
            updateBackupPlan()
        }
    }

    fun saveImageToStorage(uri: Uri): Deferred<Bitmap?> {
        return com.zen.accounts.utility.async {
            settingUIState.loadingState.value = LoadingState.LOADING
            return@async mediaStoreRepository.saveImageToStorage(uri).await()
        }
    }

    suspend fun uploadUserProfilePicture(imageBitmap: Bitmap) {
        io {
            val bos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            expenseRepository.dataStore.getUser()?.let {
                expenseRepository.dataStore.saveUser(it.copy(profilePic = bos.toByteArray()))
                settingUIState.profilePic.value = imageBitmap
                settingUIState.loadingState.value = LoadingState.SUCCESS
                val requestId = workerRepository.updateProfile()
                workerRepository.getWorkInfoById(requestId)
                    .collectLatest { workInfo ->
                        val outputData = workInfo.outputData.getString(work_manager_output_data)
                        when (workInfo.state) {

                            WorkInfo.State.SUCCEEDED -> {
                                settingUIState.showSnackBarText.value = outputData.toString()
                                showSnackBar()
                            }

                            WorkInfo.State.FAILED -> {
                                settingUIState.showSnackBarText.value = outputData.toString()
                                showSnackBar()
                            }

                            else -> {}
                        }
                    }
            }
        }
    }
}