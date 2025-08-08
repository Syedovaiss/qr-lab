package com.ovais.quickcode.worker.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.ovais.quickcode.notification.NotificationManager
import com.ovais.quickcode.storage.db.HistoryDao
import com.ovais.quickcode.utils.file.FileExportHelper
import com.ovais.quickcode.worker.job.ExportCSVWorker
import com.ovais.quickcode.worker.job.ExportPDFWorker

class AppWorkerFactory(
    private val dao: HistoryDao,
    private val fileExportHelper: FileExportHelper,
    private val notificationManager: NotificationManager
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        return when (workerClassName) {
            ExportCSVWorker::class.qualifiedName -> {
                ExportCSVWorker(
                    appContext,
                    workerParameters,
                    dao,
                    fileExportHelper,
                    notificationManager
                )
            }

            ExportPDFWorker::class.qualifiedName -> {
                ExportPDFWorker(
                    appContext,
                    workerParameters,
                    dao,
                    fileExportHelper,
                    notificationManager
                )
            }

            else -> {
                null
            }
        }
    }
}