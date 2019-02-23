package com.aykuttasil.sweetloc.data.local.entity

import androidx.databinding.BaseObservable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class UserEntity(
    @PrimaryKey var userUUID: String,
    var userEmail: String? = null,
    var userPassword: String? = null,
    var userName: String? = null,
    var userSurname: String? = null,
    var userTel: String? = null,
    var userRegId: String? = null,
    var userToken: String? = null,
    var userImageUrl: String? = null,
    var userOneSignalId: String? = null,
    var userCity: String? = null
) : BaseObservable() {

    /*
    var UserName: String
        @Bindable
        get() {
            return _UserName
        }
        set(value) {
            _UserName = value
            notifyPropertyChanged(BR.userName)
        }


    var UserJob: String?
        @Bindable
        get() {
            return _UserJob
        }
        set(value) {
            _UserJob = value
            notifyPropertyChanged(BR.userJob)
        }
        */
}