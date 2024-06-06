package com.zen.accounts.db.model


data class User(
    var uid: String = "",
    var name: String = "",
    var phone: String = "",
    var email: String = "",
    var isAuthenticated: Boolean = false,
    var profilePic: ByteArray? = null,
    var profilePicFirebaseFormat: String? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (profilePic != null) {
            if (other.profilePic == null) return false
            if (!profilePic.contentEquals(other.profilePic)) return false
        } else if (other.profilePic != null) return false

        return true
    }

    override fun hashCode(): Int {
        return profilePic?.contentHashCode() ?: 0
    }
}
