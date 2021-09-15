package com.turck.t1mobiledemo

import android.text.Editable
import retrofit2.Call
import retrofit2.http.*

data class ResponseDTO(var result: String? = null)

data class ResponseSel(var resultStatus: String? = null,
                       var userEmail: String? = null,
                       var resultMessage: String? = null,
                       var userId : String? = null)

data class ResponseIn(var resultStatus: String? = null,
                       var resultMessage: String? = null)

data class ResponseLogin(var resultStatus: String? = null,
                      var resultMessage: String? = null,
                         var userInfo : UserInfo? = null)

data class UserInfo (var USER_AUTHORITY : String? = null,
                     var CREATE_DATE : String? = null,
                     var USE_YN : String? = null,
                     var CREATE_USER : String? = null,
                     var USER_PASSWORD : String? = null,
                     var USER_EMAIL : String? = null,
                     var USER_ID : String? = null,
                     var USER_NAME : String? = null)

interface HowlService {

    @FormUrlEncoded
    @POST("selectUserId")
    fun selectUserId(@Field("userEmail") userEmail: String): Call<ResponseSel>

    @FormUrlEncoded
    @POST("insertRegister")
    fun insertRegister(@Field("userId") userId: String,
                       @Field("userName") userName: String,
                       @Field("userEmail") userEmail: String,
                       @Field("userPassword") userPassword: String): Call<ResponseIn>

    @FormUrlEncoded
    @POST("userLogin")
    fun userLogin(@Field("userId") userId: String,
                  @Field("userPassword") userPassword: String
    ): Call<ResponseLogin>

//    @FormUrlEncoded
//    @PUT("/howl/{id}")
//    fun putRequest(@Path("id") id: String,
//                   @Field("content") content: String): Call<ResponseDTO>
//
//    @DELETE("/howl/{id}")
//    fun deleteRequest(@Path("id") id: String): Call<ResponseDTO>

}