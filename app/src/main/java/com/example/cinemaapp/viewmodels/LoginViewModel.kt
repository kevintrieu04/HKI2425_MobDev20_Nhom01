package com.example.cinemaapp.viewmodels

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.cinemaapp.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.security.MessageDigest
import java.util.UUID


class LoginViewModel(val context: Context) {
    private val auth = Firebase.auth

    fun createAccount(email: String, password: String): Flow<LoginUiState> = callbackFlow {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(LoginUiState.Success)
                } else {
                    trySend(LoginUiState.Error(it.exception?.message ?:""))
                }
            }
        awaitClose()
    }

    fun login(email: String, password: String): Flow<LoginUiState> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(LoginUiState.Success)
                } else {
                    trySend(LoginUiState.Error(it.exception?.message ?:""))
                }
            }
        awaitClose()
    }

    private fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val byte = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(byte)

        return digest.fold("") { str, it -> str + "%02x".format(it) }

    }

    fun loginWithGoogle(): Flow<LoginUiState> = callbackFlow {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .setAutoSelectEnabled(false)
            .setNonce(createNonce())
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(context = context, request = request)

            val credential = result.credential

            if (credential is CustomCredential) {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                        val firebaseCredential = GoogleAuthProvider
                            .getCredential(googleIdTokenCredential.idToken, null)

                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    trySend(LoginUiState.Success)
                                } else {
                                    trySend(LoginUiState.Error(it.exception?.message ?:""))
                                }
                            }
                    } catch (e : GoogleIdTokenParsingException) {
                        trySend(LoginUiState.Error(e.message ?:""))
                    }
                }
            }
        } catch (e: Exception) {
            trySend(LoginUiState.Error(message = e.message ?:""))
        }

        awaitClose()
    }
}

interface LoginUiState {
    data object Success: LoginUiState
    data class Error(val message: String): LoginUiState
}