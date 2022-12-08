package com.imaginato.homeworkmvvm.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.imaginato.homeworkmvvm.data.local.login.User
import com.imaginato.homeworkmvvm.data.local.login.UserDataRepository
import com.imaginato.homeworkmvvm.data.local.login.UserDatabase
import com.imaginato.homeworkmvvm.data.remote.login.repo.LoginRepository
import com.imaginato.homeworkmvvm.data.remote.login.request.LoginRequest
import com.imaginato.homeworkmvvm.data.remote.login.response.LoginResponse
import com.imaginato.homeworkmvvm.ui.base.BaseViewModel
import com.imaginato.homeworkmvvm.ui.base.IApp
import com.imaginato.homeworkmvvm.utils.AppPreference
import com.imaginato.homeworkmvvm.utils.Constant
import com.imaginato.homeworkmvvm.utils.Utils
import com.imaginato.homeworkmvvm.utils.inputValidator
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.inject

@KoinApiExtension
class LoginActivityViewModel : BaseViewModel() {
    private val repository: LoginRepository by inject()
    private val userRepository: UserDataRepository by inject()
    private var _resultLiveData: MutableLiveData<LoginResponse> = MutableLiveData()
    private var _progress: MutableLiveData<Boolean> = MutableLiveData()
    val progress: LiveData<Boolean>
        get() {
            return _progress
        }

    val resultLiveData: LiveData<LoginResponse>
        get() {
            return _resultLiveData
        }

    // private var userDao: UserDao? = null

    /**
     * Do something and handle business logic here
     * */
    fun getLoginData(loginRequest: LoginRequest) {
        if (inputValidator.validateLogin(loginRequest.userName, loginRequest.password)) {
            viewModelScope.launch {
                repository.getLoginData(loginRequest)
                    .onStart {
                        _progress.value = true
                    }.catch {
                        _progress.value = false
                    }
                    .onCompletion {
                    }.collect {
                        //Stops loader as soon as any of the response arrives
                        _progress.value = false

                        try {
                            val loginResponse =
                                Gson().fromJson(
                                    JSONObject(it.body()!!.string()).toString(),
                                    LoginResponse::class.java
                                )

                            when (loginResponse.errorCode) {
                                //API Response OK=00
                                Constant.API_RESPONSE_OK -> {
                                    //Saving X-Acc the token from header in preference for easy access
                                    val token: String = it.headers()["X-Acc"].toString()
                                    try {
                                        appPreference = AppPreference(IApp.applicationContext())
                                        appPreference!!.token = token
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    //Saving User in Room Database
                                    userRepository.insertUser(
                                        User(
                                            0,
                                            loginResponse.data!!.userId!!,
                                            loginResponse.data.userName!!,
                                            loginResponse.data.isDeleted!!
                                        )
                                    )
                                    _resultLiveData.value = loginResponse
                                }
                                Constant.API_RESPONSE_ERROR -> {
                                    //API Response ERROR=01
                                    Utils.showErrorToast(
                                        IApp.applicationContext(),
                                        loginResponse.errorMessage
                                    )
                                }
                                else -> {
                                    //can acknowledge developers that code is not handled for this API
                                    //Logs to server can be updated for unknown response for particular user/API.
                                }

                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
            }
        } else {
            Utils.showErrorToast(IApp.applicationContext(), "Please enter required fields")
        }
    }


}