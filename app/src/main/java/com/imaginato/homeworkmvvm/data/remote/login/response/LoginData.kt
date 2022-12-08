package com.imaginato.homeworkmvvm.data.remote.login.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Parth Soni on 12/8/2022.
 */
data class LoginData(

    @field:SerializedName("isDeleted")
    val isDeleted: Boolean? = null,

    @field:SerializedName("userName")
    val userName: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null
)