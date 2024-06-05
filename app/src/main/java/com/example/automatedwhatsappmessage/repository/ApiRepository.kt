package com.example.automatedwhatsappmessage.repository

import com.example.automatedwhatsappmessage.api.AddTemplateBody
import com.example.automatedwhatsappmessage.api.ApiInterface
import com.example.automatedwhatsappmessage.api.ApiResponse
import com.example.automatedwhatsappmessage.api.AppStatusBody
import com.example.automatedwhatsappmessage.api.AppStatusResponse
import com.example.automatedwhatsappmessage.api.DeleteTemplateBody
import com.example.automatedwhatsappmessage.api.DeviceCheckResponse
import com.example.automatedwhatsappmessage.api.DeviceDetail
import com.example.automatedwhatsappmessage.api.Login
import com.example.automatedwhatsappmessage.api.MessageTypeBody
import com.example.automatedwhatsappmessage.api.RegisterLogin
import com.example.automatedwhatsappmessage.api.TemplateBody
import com.example.automatedwhatsappmessage.api.TemplateList
import com.example.automatedwhatsappmessage.api.UserProfile
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(private val apiService:ApiInterface) {

    suspend fun getLoginResponse(login: Login):Response<ApiResponse<List<UserProfile>>> = apiService.login(login)

    suspend fun createRegisterResponse(registerlogin: RegisterLogin):Response<ApiResponse<List<Any>>> = apiService.createRegister(registerlogin)

    suspend fun messageType(messageType: MessageTypeBody):Response<ApiResponse<List<AppStatusResponse>>> = apiService.messagetype(messageType)

    suspend fun appStatus(appStatus: AppStatusBody):Response<ApiResponse<List<AppStatusResponse>>> = apiService.appStatus(appStatus)


    suspend fun getCallStatus(getCallStatus: TemplateBody):Response<ApiResponse<List<Any>>> = apiService.getCallStatus(getCallStatus)


    suspend fun getTemplateList(templateBody: TemplateBody):Response<ApiResponse<List<TemplateList>>> = apiService.getTemplateList(templateBody)


    suspend fun addTemplateList(addTemplateBody: AddTemplateBody):Response<ApiResponse<List<Any>>> = apiService.addTemplate(addTemplateBody)

    suspend fun editTemplateList(editTemplateBody: AddTemplateBody):Response<ApiResponse<List<Any>>> = apiService.editTemplate(editTemplateBody)

    suspend fun deleteTemplateList(deleteTemplateBody: DeleteTemplateBody):Response<ApiResponse<List<Any>>> = apiService.deleteTemplate(deleteTemplateBody)


    suspend fun getDeviceResponse(deviceDetail: DeviceDetail):Response<ApiResponse<List<DeviceCheckResponse>>> = apiService.deviceCheck(deviceDetail)


}