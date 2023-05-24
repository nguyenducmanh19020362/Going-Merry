import com.google.gson.annotations.SerializedName

data class ForgotPasswordDto(
    @SerializedName("email") val email: String
)

data class ResetPasswordDto(
    @SerializedName("password") val password: String,
    @SerializedName("token") val token: String
)