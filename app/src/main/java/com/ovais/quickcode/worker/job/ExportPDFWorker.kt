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

class ExportPDFWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val dao: HistoryDao,
    private val fileExportHelper: FileExportHelper,
    private val notificationManager: NotificationManager
) : CoroutineWorker(context, workerParameters) {

    private companion object Companion {
        private const val FILENAME = "quick_code_export"
    }

    override suspend fun doWork(): Result {
        return try {
            val scannedList = dao.getScannedCodesDescending()
            val createdList = dao.getAllCreatedCodeByDescOrder()

            val isSaved = fileExportHelper.exportToPDF(
                createdList,
                scannedList,
                FILENAME
            )
            if (isSaved) {
                notificationManager.showExportNotification(listOf(FILENAME))
                Result.success(
                    workDataOf(MESSAGE_KEY to EXPORT_MESSAGE)
                )
            } else {
                Result.failure(
                    workDataOf(ERROR_KEY to "Failed to export file")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(
                workDataOf(ERROR_KEY to e.localizedMessage)
            )
        }
    }
}