package com.example.automatedwhatsappmessage.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.automatedwhatsappmessage.Common.AppPreferences
import com.example.automatedwhatsappmessage.MainActivity
import com.example.automatedwhatsappmessage.R
import com.example.automatedwhatsappmessage.ViewModel.HomeViewModel
import com.example.automatedwhatsappmessage.ViewModel.LoginViewModel
import com.example.automatedwhatsappmessage.adapter.CallLogAdapter
import com.example.automatedwhatsappmessage.api.ApiStatus
import com.example.automatedwhatsappmessage.api.CallLogData
import com.example.automatedwhatsappmessage.api.TemplateBody
import com.example.automatedwhatsappmessage.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: HomeViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        (activity as MainActivity).enableTheSwipeGesture()

//
//        loginViewModel.getCallDetails(
//            TemplateBody(
//                loginMobileNumber = AppPreferences.phoneNumber
//            )
//        )
//
//
//        loginViewModel.liveData.observe(viewLifecycleOwner){
//            resp -> resp.let {
//                resource ->
//                when(resource.status){
//                    ApiStatus.LOADING ->{}
//                    ApiStatus.ERROR ->{}
//                    ApiStatus.SUCCESS ->{
//                        Log.d(TAG, "onViewCreated:SUCCESS")
//                    }
//                }
//            }
//        }




        binding.includeHomebg.imageBackButton.visibility = View.INVISIBLE

        // this creates a vertical layout Manager
        val layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
        binding.recyviewCalllogs.layoutManager = layoutManager

        // ArrayList of class ItemsViewModel
        val data = ArrayList<CallLogData>()

        // This loop will create 20 Views containing
        // the image with the count of view

            data.add(CallLogData("33", "Missed call messages"))
            data.add(CallLogData("13", "Received call messages"))
            data.add(CallLogData("3", "Dialed call messages"))
            data.add(CallLogData("43", "Total messages"))


        // This will pass the ArrayList to our Adapter
        val adapter = CallLogAdapter(requireContext(),data)

        // Setting the Adapter with the recyclerview
        binding.recyviewCalllogs.adapter = adapter


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{

        const val TAG = "HomeFragment"
    }

}