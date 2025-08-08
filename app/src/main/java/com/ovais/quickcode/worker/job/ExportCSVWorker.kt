package com.ovais.quickcode.worker.job

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.ovais.quickcode.notification.NotificationManager
import com.ovais.quickcode.storage.db.HistoryDao
import com.ovais.quickcode.utils.WorkConstants.ERROR_KEY
import com.ovais.quickcode.utils.WorkConstants.EXPORT_MESSAGE
import com.ovais.quickcode.utils.WorkConstants.MESSAGE_KEY
import com.ovais.quickcode.utils.file.FileExportHelper

class ExportCSVWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val dao: HistoryDao,
    private val fileExportHelper: FileExportHelper,
    private val notificationManager: NotificationManager
) : CoroutineWorker(context, workerParameters) {

    private companion object Companion {
        private const val SCANNED_FILENAME = "quick_code_scanned"
        private const val CREATED_FILENAME = "quick_code_created"
    }

    override suspend fun doWork(): Result {
        return try {
            val scannedList = dao.getScannedCodesDescending()
            val createdList = dao.getAllCreatedCodeByDescOrder()

            fileExportHelper.exportScannedToCsv(scannedList, SCANNED_FILENAME)
            fileExportHelper.exportCreatedToCsv(createdList, CREATED_FILENAME)
            notificationManager.showExportNotification(
                listOf(
                    "$SCANNED_FILENAME.csv",
                    "$CREATED_FILENAME.csv"
                )
            )
            Result.success(
                workDataOf(MESSAGE_KEY to EXPORT_MESSAGE)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(
                workDataOf(ERROR_KEY to e.localizedMessage)
            )
        }
    }
}