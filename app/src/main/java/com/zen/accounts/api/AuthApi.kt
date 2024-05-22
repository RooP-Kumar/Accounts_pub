package com.zen.accounts.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.zen.accounts.api.resource.Response
import com.zen.accounts.db.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthApi @Inject constructor(){
    suspend fun registerUsingEmailAndPassword(uid: String, pass : String, user : User) : Response<String> = suspendCoroutine { continuation ->
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Users").document(uid)
        val auth = FirebaseAuth.getInstance()
        val res = Response(value = "")
        auth.createUserWithEmailAndPassword(user.email, pass)
            .addOnSuccessListener { authResult ->
                docRef.set(user.copy(isAuthenticated = false))
                    .addOnSuccessListener {
                        authResult.user?.sendEmailVerification()
                        res.status = true
                        res.message = "Verification code sent to you email."
                        res.value = uid
                        continuation.resume(res)
                    }
                    .addOnFailureListener {
                        res.status = false
                        res.message = it.message.toString()
                        continuation.resume(res)
                    }
            }
            .addOnFailureListener {
                res.status = false
                res.message = it.message.toString()
                continuation.resume(res)
            }
    }

    suspend fun loginUsingEmailAndPassword(email: String, pass : String) : Response<User> = suspendCoroutine { continuation ->
        val response = Response(value = User())
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        CoroutineScope(Dispatchers.IO).launch {
            val authResult = auth.signInWithEmailAndPassword(email, pass).await()
            val uidMapResult = db.collection("uidmap").document(authResult.user?.uid!!).get().await()
            if(!authResult.user?.isEmailVerified!!) {
                response.status = false
                response.message = "Please! Verify your email."
                continuation.resume(response)
            } else {
                val userUid = uidMapResult.get("uid").toString()
                db.collection("Users").document(userUid).update("authenticated", true, "uid", userUid)
                val userResult = db.collection("Users").document(userUid).get().await()
                response.value = userResult.toObject(User::class.java)!!
                response.status = true
                continuation.resume(response)
            }
        }
    }

    suspend fun uploadProfilePic(user: User) : Response<Unit> = suspendCoroutine { continuation ->
        val response = Response(value = Unit)
        val db = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReference("user/${user.uid}/profile")

        val docRef = db.collection("Users").document(user.uid)
        user.profilePic?.let {imageBytes ->
            storageRef.putBytes(imageBytes)
                .continueWithTask { task ->
                    if(!task.isSuccessful){
                        task.exception?.let {
                            throw it
                        }
                    }
                    storageRef.downloadUrl
                }
                .addOnCompleteListener {task ->
                    if(task.isSuccessful) {
                        val imageUri = task.result
                        docRef.update("profilePic", imageUri)
                            .addOnSuccessListener {
                                response.status = true
                                response.message = "Profile pic is successfully uploaded."
                                continuation.resume(response)
                            }
                            .addOnFailureListener {
                                response.status = false
                                response.message = "Something went wrong."
                                continuation.resume(response)
                            }
                    } else {
                        response.status = false
                        response.message = "Something went wrong."
                        continuation.resume(response)
                    }
                }
                .addOnFailureListener {
                    response.status = false
                    response.message = "Something went wrong."
                    continuation.resume(response)
                }
        }
    }

    fun logout() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
    }
}