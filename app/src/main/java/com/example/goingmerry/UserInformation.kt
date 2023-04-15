package com.example.goingmerry

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Entity
data class UserInformation (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "token") val token: String?,
    @ColumnInfo(name = "expiredToken") val expiredToken: String?
)
@Dao
interface UserInformationDao {
    @Query("SELECT * FROM userinformation")
    fun getAll(): List<UserInformation>

    @Insert
    fun insertAll(vararg info: UserInformation)

    @Query("DELETE FROM userinformation")
    fun deleteAll()
}

@Database(entities = [UserInformation::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userInformationDao(): UserInformationDao
}

class DataUserInfo (private val userInformation: UserInformationDao): ViewModel() {
    private val _listInfo = MutableStateFlow(listOf<UserInformation>())
    val listInfo = _listInfo.asStateFlow()
    fun insert(token: String, expired: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            userInformation.deleteAll()
            userInformation.insertAll(UserInformation(1, token, expired))
        }
    }
    fun getAllInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            _listInfo.emit(userInformation.getAll())
        }
    }
}