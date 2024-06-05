package com.example.automatedwhatsappmessage.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.automatedwhatsappmessage.Common.runCatchingResult
import com.example.automatedwhatsappmessage.api.AddTemplateBody
import com.example.automatedwhatsappmessage.api.ApiResponse
import com.example.automatedwhatsappmessage.api.DeleteTemplateBody
import com.example.automatedwhatsappmessage.api.Resource
import com.example.automatedwhatsappmessage.api.TemplateBody
import com.example.automatedwhatsappmessage.api.TemplateList
import com.example.automatedwhatsappmessage.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplateListViewModel @Inject constructor(private val apiRepository: ApiRepository):ViewModel(){

    val mutableListResponse = MutableLiveData<Resource<ApiResponse<List<TemplateList>>>>()

    val liveData:LiveData<Resource<ApiResponse<List<TemplateList>>>> get() = mutableListResponse


    fun getTemplateList(templateBody: TemplateBody) = viewModelScope.launch(Dispatchers.IO) {
        runCatchingResult(apiRepository.getTemplateList(templateBody),mutableListResponse)
    }


    //addTemlateList
    val addMutableListResponse = MutableLiveData<Resource<ApiResponse<List<Any>>>>()

    val addTemplateListLiveData:LiveData<Resource<ApiResponse<List<Any>>>> get() = addMutableListResponse


    fun addTemplateList(addTemplateBody:AddTemplateBody) = viewModelScope.launch(Dispatchers.IO) {
        runCatchingResult(apiRepository.addTemplateList(addTemplateBody),addMutableListResponse)
    }


    //deleteTemplateList

    val deleteMutableListResponse = MutableLiveData<Resource<ApiResponse<List<Any>>>>()

    val deleteTemplateListLiveData:LiveData<Resource<ApiResponse<List<Any>>>> get() = deleteMutableListResponse


    fun deleteTemplateList(deleteTemplateBody:DeleteTemplateBody) = viewModelScope.launch(Dispatchers.IO) {
        runCatchingResult(apiRepository.deleteTemplateList(deleteTemplateBody),deleteMutableListResponse)
    }


    //editTemplateList

    val editMutableListResponse = MutableLiveData<Resource<ApiResponse<List<Any>>>>()

    val editTemplateListLiveData:LiveData<Resource<ApiResponse<List<Any>>>> get() = editMutableListResponse


    fun editTemplateList(editTemplateBody:AddTemplateBody) = viewModelScope.launch(Dispatchers.IO) {
        runCatchingResult(apiRepository.editTemplateList(editTemplateBody),editMutableListResponse)
    }
}