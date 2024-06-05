package com.example.automatedwhatsappmessage.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.automatedwhatsappmessage.Common.runCatchingResult
import com.example.automatedwhatsappmessage.api.ApiResponse
import com.example.automatedwhatsappmessage.api.RegisterLogin
import com.example.automatedwhatsappmessage.api.Resource
import com.example.automatedwhatsappmessage.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val apiRepository: ApiRepository):ViewModel() {

    val mutableListResponse = MutableLiveData<Resource<ApiResponse<List<Any>>>>()

    val liveData: LiveData<Resource<ApiResponse<List<Any>>>> get() = mutableListResponse


    fun createRegister(createRegister: RegisterLogin) = viewModelScope.launch(Dispatchers.IO) {
        runCatchingResult(apiRepository.createRegisterResponse(createRegister),mutableListResponse)
    }
}