package com.ovais.quickcode.features.scan_code.presentation

import android.graphics.Bitmap
import android.net.Uri
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.quickcode.features.code_details.domain.SaveScannedCodeUseCase
import com.ovais.quickcode.features.create.domain.CanAutoOpenUrlUseCase
import com.ovais.quickcode.features.create.domain.CanCopyToClipboardUseCase
import com.ovais.quickcode.features.history.data.SaveScannedCodeParam
import com.ovais.quickcode.features.scan_code.data.ScanCodeParam
import com.ovais.quickcode.features.scan_code.data.ScanCodeParamType
import com.ovais.quickcode.features.scan_code.data.ScanResult
import com.ovais.quickcode.features.scan_code.domain.CanVibrateAndBeepUseCase
import com.ovais.quickcode.features.scan_code.domain.ScanCodeUseCase
import com.ovais.quickcode.utils.DateTimeManager
import com.ovais.quickcode.utils.clipboard.ClipboardManager
import com.ovais.quickcode.utils.permissions.PermissionManager
import com.ovais.quickcode.utils.sound.AppSoundManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanViewModel(
    private val permissionManager: PermissionManager,
    private val scanCodeUseCase: ScanCodeUseCase,
    private val canBeepAndVibrateOnScanUseCase: CanVibrateAndBeepUseCase,
    private val soundManager: AppSoundManager,
    private val canAutoOpenUrlUseCase: CanAutoOpenUrlUseCase,
    private val canCopyToClipboardUseCase: CanCopyToClipboardUseCase,
    private val clipboardManager: ClipboardManager,
    private val saveScannedCodeUseCase: SaveScannedCodeUseCase,
    private val dateTimeManager: DateTimeManager
) : ViewModel() {

    private val _scanResult by lazy { MutableStateFlow<String?>(null) }
    val scanResult: StateFlow<String?>
        get() = _scanResult.asStateFlow()
    private var isScanning = false

    private val _permissionArray by lazy { MutableSharedFlow<ArrayList<String>>(replay = 1) }
    val permissionArray: SharedFlow<ArrayList<String>>
        get() = _permissionArray.asSharedFlow()


    private val _canAutoCopyToClipboard by lazy { MutableStateFlow(false) }
    val canAutoCopyToClipboard: StateFlow<Boolean>
        get() = _canAutoCopyToClipboard.asStateFlow()


    private val _canAutoOpenURL by lazy { MutableStateFlow(false) }
    val canAutoOpenURL: StateFlow<Boolean>
        get() = _canAutoOpenURL.asStateFlow()

    init {
        initAutoCopyState()
        initAutoOpenURLState()
    }

    private fun initAutoCopyState() {
        viewModelScope.launch {
            _canAutoCopyToClipboard.value = canCopyToClipboardUseCase()
        }
    }

    private fun initAutoOpenURLState() {
        viewModelScope.launch {
            _canAutoOpenURL.value = canAutoOpenUrlUseCase()
        }
    }

    fun checkPermissions() {
        val permissions = arrayListOf<String>()
        if (permissionManager.hasCameraPermission.not()) {
            permissions.add(permissionManager.cameraPermission)
        }
        if (permissionManager.hasStoragePermission.not()) {
            permissions.add(permissionManager.storagePermission)
        }
        if (permissionManager.hasNotificationPermission.not()) {
            permissions.add(permissionManager.notificationPermission)
        }
        viewModelScope.launch {
            _permissionArray.emit(permissions)
        }
    }

    fun onImageProxy(imageProxy: ImageProxy) {
        if (isScanning) {
            imageProxy.close()
            return
        }
        isScanning = true
        try {
            scanCode(
                param = ScanCodeParam(
                    imageProxy = imageProxy,
                    type = ScanCodeParamType.ImageProxy
                )
            )
        } finally {
            isScanning = false
        }
    }

    fun onImagePickedFromGallery(uri: Uri) {
        scanCode(
            param = ScanCodeParam(
                uri = uri,
                type = ScanCodeParamType.Uri
            )
        )
    }

    private fun scanCode(param: ScanCodeParam) {
        viewModelScope.launch {
            when (val result = scanCodeUseCase(param)) {
                is ScanResult.Success -> {
                    onCodeScanned(result)
                }

                is ScanResult.Failure -> Unit
            }
        }
    }

    private suspend fun onCodeScanned(result: ScanResult.Success) {
        val (canBeep, canVibrate) = canBeepAndVibrateOnScanUseCase()
        when {
            canBeep && canVibrate -> {
                soundManager.playBeep()
                soundManager.startVibrating()
            }

            canBeep -> {
                soundManager.playBeep()
            }

            canVibrate -> {
                soundManager.startVibrating()
            }
        }
        saveScannedCode(result.bitmap, result.content)
        _scanResult.update { result.content }
    }

    fun copyToClipboard(label: String, content: String) {
        clipboardManager.copy(label, content)
    }

    fun clearScannedResults() {
        _scanResult.value = null
    }

    private fun saveScannedCode(image: Bitmap?, content: String) {
        viewModelScope.launch {
            saveScannedCodeUseCase(
                param = SaveScannedCodeParam(
                    content = content,
                    bitmap = image,
                    scannedAt = dateTimeManager.now
                )
            )
        }
    }
}