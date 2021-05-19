package com.me.livedataretrofitcoroutine.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.me.livedataretrofitcoroutine.data.LoginInfo
import com.me.livedataretrofitcoroutine.data.LoginResp
import com.me.livedataretrofitcoroutine.data.NetData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainVIewModel:ViewModel(){
    private lateinit var error: MutableLiveData<String>
    private lateinit var loginResp: MutableLiveData<LoginResp>
    fun getError(): MutableLiveData<String> {
        if (!::error.isInitialized) {
            error = MutableLiveData()
        }
        return error
    }

    fun getLoginResp(): MutableLiveData<LoginResp> {
        if (!::loginResp.isLateinit) {
            loginResp = MutableLiveData()
        }
        return loginResp
    }

    fun makeRequest(request: () -> Unit) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                request
            }
        }
    }

    fun login(loginInfo: LoginInfo) {
        makeRequest {
            suspend {
                NetData.login(loginInfo, getError()).apply {
                    if (this!=null){
                        if (success){
                            getLoginResp().postValue(result)
                        }else{
                            getError().postValue(message)
                        }
                    }
                }
            }
        }
    }
}