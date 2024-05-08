package com.zen.accounts.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import androidx.work.Operation.State
import androidx.work.WorkInfo
import com.zen.accounts.repository.AuthRepository
import com.zen.accounts.repository.ExpenseItemRepository
import com.zen.accounts.repository.ExpenseRepository
import com.zen.accounts.repository.WorkerRepository
import com.zen.accounts.ui.screens.common.BackupPlan
import com.zen.accounts.ui.screens.common.LoadingState
import com.zen.accounts.ui.screens.common.single_work_request_tag
import com.zen.accounts.ui.screens.main.setting.SettingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val expenseRepository: ExpenseRepository,
    private val expenseItemRepository: ExpenseItemRepository,
    private val workerRepository: WorkerRepository
) : BaseViewmodel() {
    val settingUIState by lazy { SettingUiState() }

    fun logout() {
        viewModelScope.launch {
            settingUIState.apply {
                loadingState.value = LoadingState.LOADING
                if(expenseRepository.isBackupTableEmpty()) {
                    expenseRepository.clearExpenseTable()
                    expenseItemRepository.clearExpenseItemTable()
                    authRepository.logout()
                    dataStore.logoutUser()

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

    fun logoutConfirmation(logoutWithoutBackup : Boolean) {
        viewModelScope.launch {
            settingUIState.apply {
                if(!logoutWithoutBackup){
                    dataStore.getUser()?.let {user ->
                        workerRepository.startUploadingNow(user.uid, single_work_request_tag)
                        workerRepository.getWorkInfoByTag(single_work_request_tag)
                            .collectLatest {
                                when (it?.last()?.state) {
                                    WorkInfo.State.RUNNING -> {
                                        loadingState.value = LoadingState.LOADING
                                    }

                                    WorkInfo.State.ENQUEUED -> {
                                        loadingState.value = LoadingState.IDLE
                                    }

                                    WorkInfo.State.SUCCEEDED -> {
                                        expenseRepository.clearExpenseTable()
                                        expenseItemRepository.clearExpenseItemTable()
                                        authRepository.logout()
                                        dataStore.logoutUser()
                                        loadingState.value = LoadingState.SUCCESS
                                        showSnackBarText.value = "Backup Successful"
                                        showSnackBar()
                                    }

                                    WorkInfo.State.FAILED -> {
                                        loadingState.value = LoadingState.FAILURE
                                        showSnackBarText.value = "Backup Failed! try again later."
                                        showSnackBar()
                                    }
                                    else -> {}
                                }
                            }
                    }
                } else {
                    expenseRepository.clearBackupTable()
                    expenseRepository.clearExpenseTable()
                    expenseItemRepository.clearExpenseItemTable()
                    authRepository.logout()
                    dataStore.logoutUser()
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
                val backupPlan = dataStore.getBackupPlan()
                if (backupDropDownText.value == BackupPlan.Off && backupPlan != BackupPlan.Off) {
                    backupDropDownText.value = backupPlan
                }
            }
        }
    }

    suspend fun updateBackupPlan() {
        viewModelScope.launch(Dispatchers.IO) {
            settingUIState.apply {
                dataStore.updateBackupPlan(if (backupDropDownText.value == BackupPlan.Now) BackupPlan.Off else backupDropDownText.value)
            }
        }
    }
}