package com.example.automatedwhatsappmessage.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.automatedwhatsappmessage.Common.runCatchingResult
import com.example.automatedwhatsappmessage.api.ApiResponse
import com.example.automatedwhatsappmessage.api.AppStatusBody
import com.example.automatedwhatsappmessage.api.AppStatusResponse
import com.example.automatedwhatsappmessage.api.Login
import com.example.automatedwhatsappmessage.api.MessageTypeBody
import com.example.automatedwhatsappmessage.api.Resource
import com.example.automatedwhatsappmessage.api.TemplateBody
import com.example.automatedwhatsappmessage.api.TemplateList
import com.example.automatedwhatsappmessage.api.UserProfile
import com.example.automatedwhatsappmessage.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(private val apiRepository: ApiRepository):ViewModel(){

    val mutableListResponse = MutableLiveData<Resource<ApiResponse<List<AppStatusResponse>>>>()

    val liveData: LiveData<Resource<ApiResponse<List<AppStatusResponse>>>> get() = mutableListResponse


    fun getAppStatusDetails(appStatus:AppStatusBody) = viewModelScope.launch(Dispatchers.IO){
        runCatchingResult(apiRepository.appStatus(appStatus),mutableListResponse)
    }


   //messageType
    val messageTypeMutableListResponse = MutableLiveData<Resource<ApiResponse<List<AppStatusResponse>>>>()

    val messageTypeLiveData: LiveData<Resource<ApiResponse<List<AppStatusResponse>>>> get() = messageTypeMutableListResponse


    fun getMessageTypeDetails(messageType:MessageTypeBody) = viewModelScope.launch(Dispatchers.IO){
        runCatchingResult(apiRepository.messageType(messageType),messageTypeMutableListResponse)
    }

//getTemplateList
    val templateMutableListResponse = MutableLiveData<Resource<ApiResponse<List<TemplateList>>>>()

    val templateLiveData:LiveData<Resource<ApiResponse<List<TemplateList>>>> get() = templateMutableListResponse


    fun getTemplateList(templateBody: TemplateBody) = viewModelScope.launch(Dispatchers.IO) {
        runCatchingResult(apiRepository.getTemplateList(templateBody),templateMutableListResponse)
    }
}