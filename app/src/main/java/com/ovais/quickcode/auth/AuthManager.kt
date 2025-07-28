package com.ovais.quickcode.auth

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.ovais.quickcode.BuildConfig
import com.ovais.quickcode.logger.AppLogger

interface AuthManager {
    suspend fun signIn(context: Context, type: AuthType, completion: (AuthResult) -> Unit)
    suspend fun signOut()
}

class DefaultAuthManager(
    private val credentialManager: CredentialManager,
    private val logger: AppLogger
) : AuthManager {

    private val serverClientId by lazy {
        BuildConfig.SERVER_CLIENT_ID
    }

    private val auth by lazy { Firebase.auth }

    override suspend fun signIn(
        context: Context,
        type: AuthType,
        completion: (AuthResult) -> Unit
    ) {
        when (type) {
            is AuthType.Google -> signInWithGoogle(context, completion)
            else -> Unit
        }
    }

    private suspend fun signInWithGoogle(context: Context, completion: (AuthResult) -> Unit) {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(serverClientId)
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = result.credential
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                firebaseAuthWithGoogle(googleCredential.idToken, completion)
            } else {
                completion(AuthResult.Failure("Unexpected credential type"))
                logger.logException("Unexpected credential type: ${credential::class.java.simpleName}")
            }

        } catch (e: Exception) {
            completion(AuthResult.Failure("Sign-in failed: ${e.localizedMessage}"))
            logger.logException("Sign-in failed: ${e.localizedMessage}")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, completion: (AuthResult) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    logger.logInfo("Signed In with Credential Success")
                    val user = auth.currentUser
                    completion(
                        AuthResult.Success(
                            user?.uid,
                            avatar = user?.photoUrl?.path,
                            email = user?.email,
                            name = user?.displayName
                        )
                    )
                    logger.logInfo("Authenticated: ${user?.email}, ${user?.uid}")
                } else {
                    completion(AuthResult.Failure("Signed In with Credential Failure: ${task.exception}"))
                    logger.logException("Signed In with Credential Failure: ${task.exception}")
                }
            }
    }

    override suspend fun signOut() {
        auth.signOut()
        try {
            val clearRequest = ClearCredentialStateRequest()
            credentialManager.clearCredentialState(clearRequest)
        } catch (e: ClearCredentialException) {
            logger.logException("Could not clear credentials: ${e.localizedMessage}")
        }
    }
}