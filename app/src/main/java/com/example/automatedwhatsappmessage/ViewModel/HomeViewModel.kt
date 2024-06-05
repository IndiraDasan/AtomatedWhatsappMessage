package com.example.automatedwhatsappmessage.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.automatedwhatsappmessage.Common.runCatchingResult
import com.example.automatedwhatsappmessage.api.ApiResponse
import com.example.automatedwhatsappmessage.api.Login
import com.example.automatedwhatsappmessage.api.Resource
import com.example.automatedwhatsappmessage.api.TemplateBody
import com.example.automatedwhatsappmessage.api.UserProfile
import com.example.automatedwhatsappmessage.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val apiRepository: ApiRepository):ViewModel() {


    val mutableCallListResponse = MutableLiveData<Resource<ApiResponse<List<Any>>>>()

    val liveData: LiveData<Resource<ApiResponse<List<Any>>>> get() = mutableCallListResponse


    fun getCallDetails(callStatus: TemplateBody) = viewModelScope.launch(Dispatchers.Main) {
        runCatchingResult(apiRepository.getCallStatus(callStatus),mutableCallListResponse)
    }
}