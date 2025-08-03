package com.ovais.quickcode.features.scan_code.presentation

import android.net.Uri
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.quickcode.features.scan_code.data.ScanCodeParam
import com.ovais.quickcode.features.scan_code.data.ScanCodeParamType
import com.ovais.quickcode.features.scan_code.data.ScanResult
import com.ovais.quickcode.features.scan_code.domain.CanVibrateAndBeepUseCase
import com.ovais.quickcode.features.scan_code.domain.ScanCodeUseCase
import com.ovais.quickcode.utils.permissions.PermissionManager
import com.ovais.quickcode.utils.sound.AppSoundManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScanViewModel(
    private val permissionManager: PermissionManager,
    private val scanCodeUseCase: ScanCodeUseCase,
    private val dispatcherMain: CoroutineDispatcher,
    private val canBeepAndVibrateOnScanUseCase: CanVibrateAndBeepUseCase,
    private val soundManager: AppSoundManager
) : ViewModel() {

    private val _scanResult by lazy { MutableStateFlow<String?>(null) }
    val scanResult: StateFlow<String?>
        get() = _scanResult.asStateFlow()
    private var isScanning = false

    private val _permissionArray by lazy { MutableSharedFlow<ArrayList<String>>(replay = 1) }
    val permissionArray: SharedFlow<ArrayList<String>>
        get() = _permissionArray.asSharedFlow()


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
                    withContext(dispatcherMain) {
                        onCodeScanned(result.content)
                    }
                }

                is ScanResult.Failure -> Unit
            }
        }
    }

    private suspend fun onCodeScanned(content: String) {
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
        _scanResult.update { content }
    }
}