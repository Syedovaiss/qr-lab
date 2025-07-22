package com.ovais.qrlab.features.scan_qr.presentation

import android.net.Uri
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.qrlab.features.scan_qr.data.ScanCodeParam
import com.ovais.qrlab.features.scan_qr.data.ScanCodeParamType
import com.ovais.qrlab.features.scan_qr.data.ScanResult
import com.ovais.qrlab.features.scan_qr.domain.ScanCodeUseCase
import com.ovais.qrlab.utils.permissions.PermissionManager
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
    private val scanCodeUseCase: ScanCodeUseCase
) : ViewModel() {

    private val _scanResult by lazy { MutableStateFlow<String?>(null) }
    val scanResult: StateFlow<String?>
        get() = _scanResult.asStateFlow()

    private val _permissionArray by lazy { MutableSharedFlow<ArrayList<String>>(replay = 1) }
    val permissionArray: SharedFlow<ArrayList<String>>
        get() = _permissionArray.asSharedFlow()

    private val _errorMessage by lazy { MutableSharedFlow<String>() }
    val errorMessage: SharedFlow<String>
        get() = _errorMessage.asSharedFlow()


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
        scanCode(
            param = ScanCodeParam(
                imageProxy = imageProxy,
                type = ScanCodeParamType.ImageProxy
            )
        )
    }

    private fun onError(message: String) {
        viewModelScope.launch {
            _errorMessage.emit(message)
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
                    _scanResult.update { result.content }
                }

                is ScanResult.Failure -> {
                    onError(result.message)
                }
            }
        }
    }
}