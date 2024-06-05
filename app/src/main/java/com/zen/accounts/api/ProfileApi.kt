package com.zen.accounts.api

import com.zen.accounts.api.resource.Response
import com.zen.accounts.db.model.User
import com.zen.accounts.utility.Utility
import com.zen.accounts.utility.io
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ProfileApi @Inject constructor() {
    suspend fun updateProfilePic(user: User) : Response<Unit> = suspendCoroutine { continuation ->
        val response = Response(value = Unit)
        val docRef = Utility.getUserDocRef(user.uid)

        val storageRef = Utility.getStorageRef(user.uid)
        val profileRef = storageRef.child("profile")

        io {
            val uploadTask = profileRef.putBytes(user.profilePic!!).await()
            if(uploadTask.task.isSuccessful){
                val imageDownloadUrl = uploadTask.storage.downloadUrl.await()
                docRef.update("profilePicFirebaseFormat", imageDownloadUrl)
                    .addOnSuccessListener {
                        response.message = "Profile successfully uploaded into cloud."
                        response.status = false
                        continuation.resume(response)
                    }
                    .addOnFailureListener {
                        response.message = it.message.toString()
                        response.status = false
                        continuation.resume(response)
                    }
            } else {
                response.message = "File did not upload to the cloud !"
                response.status = false
                continuation.resume(response)
            }
        }
    }
}