package com.ovais.quickcode.storage.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ovais.quickcode.storage.data.LocalConfiguration
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfigurationDao {
    @Query("SELECT * FROM local_configuration LIMIT 1")
    fun getConfiguration(): Flow<LocalConfiguration?>

    @Query("SELECT * FROM local_configuration LIMIT 1")
    suspend fun getConfigurationSync(): LocalConfiguration?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfiguration(configuration: LocalConfiguration)

    @Update
    suspend fun updateConfiguration(configuration: LocalConfiguration)

    @Query("UPDATE local_configuration SET default_foreground_color = :color WHERE id = :configId")
    suspend fun updateForegroundColor(configId: Long, color: String)

    @Query("UPDATE local_configuration SET default_background_color = :color WHERE id = :configId")
    suspend fun updateBackgroundColor(configId: Long, color: String)

    @Query("UPDATE local_configuration SET default_qr_format = :format WHERE id = :configId")
    suspend fun updateQRFormat(configId: Long, format: String)

    @Query("UPDATE local_configuration SET can_vibrate_on_scan = :enabled WHERE id = :configId")
    suspend fun updateVibrationSetting(configId: Long, enabled: Boolean)

    @Query("UPDATE local_configuration SET can_beep_on_scan = :enabled WHERE id = :configId")
    suspend fun updateBeepSetting(configId: Long, enabled: Boolean)

    @Query("UPDATE local_configuration SET can_auto_copy_on_scan = :enabled WHERE id = :configId")
    suspend fun updateAutoCopySetting(configId: Long, enabled: Boolean)

    @Query("UPDATE local_configuration SET can_auto_open_url_on_scan = :enabled WHERE id = :configId")
    suspend fun updateAutoOpenURLSetting(configId: Long, enabled: Boolean)

    @Query("UPDATE local_configuration SET default_app_language = :locale WHERE id = :configId")
    suspend fun updateLocale(configId: Long, locale: String)

    @Query("UPDATE local_configuration SET default_anonymous_usage_state = :enabled WHERE id = :configId")
    suspend fun updateAnonymousUsageDataSetting(configId: Long, enabled: String)

    @Query("DELETE FROM local_configuration")
    suspend fun clearAllConfigurations()

    @Query("SELECT default_app_language FROM local_configuration ORDER BY id LIMIT 1")
    suspend fun getLocale(): String?

    @Query("SELECT default_qr_format FROM local_configuration ORDER BY id LIMIT 1")
    suspend fun getQRFormat(): String?

    @Query("SELECT can_beep_on_scan FROM local_configuration ORDER BY id LIMIT 1")
    suspend fun canPlayBeepSound(): Boolean?

    @Query("SELECT can_vibrate_on_scan FROM local_configuration ORDER BY id LIMIT 1")
    suspend fun canVibrateOnScan(): Boolean?
}