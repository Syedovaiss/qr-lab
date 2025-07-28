package com.ovais.quickcode.storage

import android.content.Context
import android.util.Base64
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ovais.quickcode.BuildConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec


private val Context.dataStore by preferencesDataStore(name = BuildConfig.STORE_FILE)

interface QuickCodePreferenceManager {
    suspend fun <T> save(context: Context, key: String, value: T)
    suspend fun <T> read(context: Context, key: String, clazz: Class<T>): T?
    suspend fun delete(context: Context, key: String)
}

class DefaultQuickCodePreferenceManager(
    private val dispatcherDefault: CoroutineDispatcher
) : QuickCodePreferenceManager {

    private companion object {
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val AES_KEY = BuildConfig.AES_KEY
    }

    private fun getSecretKey(): SecretKey {
        return SecretKeySpec(AES_KEY.toByteArray(), "AES")
    }

    private fun encrypt(value: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val iv = ByteArray(12).apply { SecureRandom().nextBytes(this) }
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), GCMParameterSpec(128, iv))
        val encrypted = cipher.doFinal(value.toByteArray())
        return Base64.encodeToString(iv + encrypted, Base64.DEFAULT)
    }

    private fun decrypt(encrypted: String): String {
        val data = Base64.decode(encrypted, Base64.DEFAULT)
        val iv = data.sliceArray(0 until 12)
        val enc = data.sliceArray(12 until data.size)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(128, iv))
        return String(cipher.doFinal(enc))
    }

    override suspend fun <T> save(context: Context, key: String, value: T) {
        val dataKey = stringPreferencesKey(key)
        val valueStr = value.toString()
        val encrypted = withContext(dispatcherDefault) { encrypt(valueStr) }

        context.dataStore.edit { prefs ->
            prefs[dataKey] = encrypted
        }
    }

    override suspend fun <T> read(context: Context, key: String, clazz: Class<T>): T? {
        val dataKey = stringPreferencesKey(key)
        val prefs = context.dataStore.data.first()
        val encrypted = prefs[dataKey] ?: return null

        val decrypted = withContext(dispatcherDefault) { decrypt(encrypted) }

        @Suppress("UNCHECKED_CAST")
        return when (clazz) {
            String::class.java -> decrypted as T
            Boolean::class.java -> decrypted.toBoolean() as T
            Int::class.java -> decrypted.toIntOrNull() as T
            Long::class.java -> decrypted.toLongOrNull() as T
            Float::class.java -> decrypted.toFloatOrNull() as T
            Double::class.java -> decrypted.toDoubleOrNull() as T
            else -> null
        }
    }

    override suspend fun delete(context: Context, key: String) {
        val dataKey = stringPreferencesKey(key)
        context.dataStore.edit { prefs -> prefs.remove(dataKey) }
    }

}