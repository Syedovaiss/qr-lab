package com.ovais.quickcode.storage.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ovais.quickcode.utils.TABLE_LOCAL_CONFIGURATION
import kotlinx.serialization.Serializable


private const val FOREGROUND_COLOR = "default_foreground_color"
private const val BACKGROUND_COLOR = "default_background_color"
private const val QR_FORMAT = "default_qr_format"
private const val VIBRATION_STATUS = "can_vibrate_on_scan"
private const val BEEP_STATUS = "can_beep_on_scan"
private const val AUTO_COPY_STATUS = "can_auto_copy_on_scan"
private const val AUTO_OPEN_URL_STATUS = "can_auto_open_url_on_scan"
private const val APP_LANGUAGE = "default_app_language"
private const val ANONYMOUSE_USAGE_STATUS = "default_anonymous_usage_state"

@Entity(
    tableName = TABLE_LOCAL_CONFIGURATION
)
@Serializable
data class LocalConfiguration(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(FOREGROUND_COLOR)
    val foregroundColor: String,
    @ColumnInfo(BACKGROUND_COLOR)
    val backgroundColor: String,
    @ColumnInfo(QR_FORMAT)
    val qrFormat: String,
    @ColumnInfo(VIBRATION_STATUS)
    val canVibrateOnScan: Boolean,
    @ColumnInfo(BEEP_STATUS)
    val canBeepOnScan: Boolean,
    @ColumnInfo(AUTO_COPY_STATUS)
    val canAutoCopyOnScan: Boolean,
    @ColumnInfo(AUTO_OPEN_URL_STATUS)
    val canAutoOpenURLOnScan: Boolean,
    @ColumnInfo(APP_LANGUAGE)
    val appLanguage: String,
    @ColumnInfo(ANONYMOUSE_USAGE_STATUS)
    val canSendAnonymousUsageData: String
)
