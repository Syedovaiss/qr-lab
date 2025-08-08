package com.ovais.quickcode.storage.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ovais.quickcode.BuildConfig
import com.ovais.quickcode.features.history.data.CreatedCodeEntity
import com.ovais.quickcode.features.history.data.ScannedCodeEntity
import com.ovais.quickcode.logger.AppLogger
import com.ovais.quickcode.storage.data.LocalConfiguration
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import java.util.concurrent.Executors

interface AppStorageManager {
    val instance: QuickCodeDatabase
}

class DefaultAppStorageManager(
    private val context: Context,
    private val logger: AppLogger
) : AppStorageManager {
    @Volatile
    private var database: QuickCodeDatabase? = null
    private val factory by lazy {
        SupportOpenHelperFactory(BuildConfig.DATABASE_PASSWORD.toByteArray())
    }
    override val instance: QuickCodeDatabase
        get() = database ?: synchronized(this) {
            database ?: Room.databaseBuilder(
                context.applicationContext,
                QuickCodeDatabase::class.java,
                BuildConfig.DATABASE_NAME
            )
                .openHelperFactory(factory)
                .fallbackToDestructiveMigration(false)
                .setQueryCallback({ query, args ->
                    logger.logInfo("ROOM_SQL: Executing query: $query , args=>${args.map { it }}")
                }, Executors.newSingleThreadExecutor())
                .build()
                .also { database = it }
        }

}

@Database(
    entities = [
        LocalConfiguration::class,
        CreatedCodeEntity::class,
        ScannedCodeEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class QuickCodeDatabase : RoomDatabase() {
    abstract fun configDao(): ConfigurationDao
    abstract fun historyDao(): HistoryDao
}