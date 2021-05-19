package com.me.livedataretrofitcoroutine.data

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.lang.Exception

data class LoginResp(val token:String)
data class LoginInfo(val userName:String,val password:String)
data class BaseResp<T>(
    val code:Int,
    val message:String,
    val result:T,
    val success:Boolean
)
interface NetService{
    @GET
    suspend fun login(loginInfo: LoginInfo):BaseResp<LoginResp>
}
class NetData {
    companion object{
        inline fun getInstance():NetService{
            val retrofit = Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(NetService::class.java)
        }
        suspend fun login(loginInfo: LoginInfo,error:MutableLiveData<String>):BaseResp<LoginResp>?{
            try {
                return getInstance().login(loginInfo)
            }catch (e:Exception){
                error.postValue(e.message)
            }
            return null
        }
    }
}