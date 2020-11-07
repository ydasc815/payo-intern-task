package com.aditya.internshiptask.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aditya.internshiptask.model.APIModel
import com.aditya.internshiptask.model.RecordsData
import com.aditya.internshiptask.network.RecordsApi
import com.aditya.internshiptask.utils.logs
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _apiModelValues = MutableLiveData<APIModel>()
    val apiModelValues: LiveData<APIModel>
        get() = _apiModelValues

    private val _recordsData = MutableLiveData<ArrayList<RecordsData>>()
    val recordsData: LiveData<ArrayList<RecordsData>>
        get() = _recordsData

    private val _networkFailureStatus = MutableLiveData<Boolean?>()
    val networkFailureStatus: LiveData<Boolean?>
        get() = _networkFailureStatus

    init {
        getRecordData()
    }

    private fun getRecordData() {
        logs("called")
        RecordsApi.retrofitService.getRecords(1).enqueue(
            object : Callback<APIModel> {
                override fun onResponse(call: Call<APIModel>, response: Response<APIModel>) {
                    _networkFailureStatus.value = false
                    logs(response.body().toString())
                    _apiModelValues.value = response.body()
                    val list: List<RecordsData> = _apiModelValues.value?.records!!
                    val arrayList: ArrayList<RecordsData> = ArrayList()
                    for (i in list.indices) {
                        arrayList.add(list[i])
                    }
                    _recordsData.value = arrayList
                }

                override fun onFailure(call: Call<APIModel>, t: Throwable) {
                    logs("Network Failure : ${t.message}")
                    _networkFailureStatus.value = true
                }

            }
        )
    }
}