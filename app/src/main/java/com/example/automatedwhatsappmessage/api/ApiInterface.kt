package com.example.automatedwhatsappmessage.api

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.protocol.HTTP
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiInterface {

    @POST("register")
    suspend fun createRegister(@Body registerLogin:RegisterLogin):Response<ApiResponse<List<Any>>>


    @POST("deviceCheck")
    suspend fun deviceCheck(@Body deviceUniqueId:DeviceDetail): Response<ApiResponse<List<DeviceCheckResponse>>>

    @POST("login")
    suspend fun login(@Body login:Login): Response<ApiResponse<List<UserProfile>>>


    @POST("template/get")
    suspend fun getTemplateList(@Body templateBody:TemplateBody
    ):Response<ApiResponse<List<TemplateList>>>


    @POST("template")
    suspend fun addTemplate(@Body templateBody:AddTemplateBody
    ):Response<ApiResponse<List<Any>>>

    @POST("config/appstatus")
    suspend fun appStatus(@Body appStatusBody:AppStatusBody):Response<ApiResponse<List<AppStatusResponse>>>


    @POST("config/messagetype")
    suspend fun messagetype(@Body appStatusBody:MessageTypeBody):Response<ApiResponse<List<AppStatusResponse>>>


    @PUT("template")
    suspend fun editTemplate(@Body editTemplateBody:AddTemplateBody
    ):Response<ApiResponse<List<Any>>>



    @POST("messagetrans")
    suspend fun addCallStatus(@Body addCallStatus:AddCallStatus): retrofit2.Call<AddCallStatusResults>



    @retrofit2.http.HTTP(method = "GET", path = "messagetrans", hasBody = true)
    suspend fun getCallStatus(@Body getCallStatus:TemplateBody):Response<ApiResponse<List<Any>>>





    @retrofit2.http.HTTP(method = "DELETE", path = "template", hasBody = true)
    suspend fun deleteTemplate(@Body deleteTemplateBody:DeleteTemplateBody):Response<ApiResponse<List<Any>>>








}