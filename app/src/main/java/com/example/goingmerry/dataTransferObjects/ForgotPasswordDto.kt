import com.google.gson.annotations.SerializedName

data class ForgotPasswordDto(
    @SerializedName("email") val email: String
)

data class ResetPassword(
    @SerializedName("newPassword") val password: String,
    @SerializedName("token") val token: String
)