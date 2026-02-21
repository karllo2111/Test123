package Network

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("name") val username: String,
    val password: String
)
