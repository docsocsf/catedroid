package uk.co.catedroid.app.data.model

import com.google.gson.annotations.SerializedName

class UserInfo(val name: String, val login: String, val cid: String, val status: String,
               val department: String, val category: String, val email: String,
               @field:SerializedName("personal_tutor") val personalTutor: String) {

    override fun toString(): String {
        return "UserInfo{" +
                "name='" + name + '\''.toString() +
                ", login='" + login + '\''.toString() +
                '}'.toString()
    }
}
