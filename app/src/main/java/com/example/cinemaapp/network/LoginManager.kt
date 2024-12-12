package com.example.cinemaapp.network

import android.content.Context
import androidx.credentials.CredentialManager

import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.cinemaapp.R
import com.example.cinemaapp.data.UserModel
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


class LoginManager(private val context: Context) {
    private val auth = Firebase.auth

    fun createAccount(email: String, password: String): Flow<AuthResponse> = callbackFlow {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(AuthResponse.Success)
                } else {
                    trySend(AuthResponse.Error(it.exception?.message ?: ""))
                }
            }
        awaitClose()
    }

    fun login(email: String, password: String): Flow<AuthResponse> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(AuthResponse.Success)
                } else {
                    trySend(AuthResponse.Error(it.exception?.message ?: ""))
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

    fun loginWithGoogle(): Flow<AuthResponse> = callbackFlow {
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
                                    trySend(AuthResponse.Success)
                                } else {
                                    trySend(AuthResponse.Error(it.exception?.message ?: ""))
                                }
                            }
                    } catch (e : GoogleIdTokenParsingException) {
                        trySend(AuthResponse.Error(e.message ?: ""))
                    }
                }
            }
        } catch (e: Exception) {
            trySend(AuthResponse.Error(message = e.message ?: ""))
        }

        awaitClose()
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getUserInfo(): UserModel? {
        val user = auth.currentUser ?: return null
        return UserModel(
            name = user.displayName ?: "",
            email = user.email ?: "",
            photoUrl = user.photoUrl?.toString() ?: ""
        )
    }

    fun logout() = auth.signOut()
}


interface AuthResponse {
    data object Success: AuthResponse
    data class Error(val message: String): AuthResponse
}