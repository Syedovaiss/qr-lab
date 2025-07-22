package com.ovais.qrlab.features.scan_qr.presentation

import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.qrlab.features.scan_qr.data.ScanResult
import com.ovais.qrlab.features.scan_qr.domain.ScanCodeUseCase
import com.ovais.qrlab.utils.permissions.PermissionManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ScanViewModel(
    private val permissionManager: PermissionManager,
    private val scanCodeUseCase: ScanCodeUseCase
) : ViewModel() {

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
        viewModelScope.launch {
            when (val result = scanCodeUseCase(imageProxy)) {
                is ScanResult.Success -> {
                    Timber.i(result.content)
                }

                is ScanResult.Failure -> {
                    Timber.e(result.message)
                }
            }
        }
    }
}