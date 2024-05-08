package com.zen.accounts.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zen.accounts.db.model.BackupTracker

@Dao
interface BackupTrackerDao {
    @Insert
    suspend fun insertBackupTracker(notBackupExpense : BackupTracker)

    @Query("SELECT COUNT(*) FROM backup_tracker")
    suspend fun getTableEntryCount() : Long

    @Query("DELETE FROM backup_tracker")
    suspend fun deleteAll()
}