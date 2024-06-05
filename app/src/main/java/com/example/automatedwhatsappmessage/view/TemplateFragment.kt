package com.example.automatedwhatsappmessage.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.automatedwhatsappmessage.Common.AppPreferences
import com.example.automatedwhatsappmessage.Common.showDialog
import com.example.automatedwhatsappmessage.R
import com.example.automatedwhatsappmessage.ViewModel.TemplateListViewModel
import com.example.automatedwhatsappmessage.adapter.TemplateAdapter
import com.example.automatedwhatsappmessage.api.ApiStatus
import com.example.automatedwhatsappmessage.api.DeleteTemplateBody
import com.example.automatedwhatsappmessage.api.TemplateBody
import com.example.automatedwhatsappmessage.api.TemplateList
import com.example.automatedwhatsappmessage.databinding.FragmentTemplateBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class TemplateFragment : Fragment(),TemplateAdapter.ClickListener{

    private var _binding:FragmentTemplateBinding? = null
    private val binding get() = _binding!!

    private val templateListViemodel: TemplateListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTemplateBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.txtvAdd.setOnClickListener {
            findNavController().navigate(R.id.viewFragment)
        }

        templateListViemodel.getTemplateList(TemplateBody(
            loginMobileNumber = AppPreferences.phoneNumber.toString()
        ))

        Log.d(TAG, "onViewCreated: ${AppPreferences.phoneNumber}")


        templateListViemodel.liveData.observe(viewLifecycleOwner){
            res -> res?.let {
                resource ->
                when(resource.status){
                    ApiStatus.SUCCESS-> {
                        lifecycleScope.launch(Dispatchers.Main){
                            val resp = resource.data?.results
                            // this creates a vertical layout Manager
                           // Log.d(TAG, "onViewCreated: ${resp}")
                            val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

                            binding.rcyviewTemplatelist.layoutManager = layoutManager

                            // This will pass the ArrayList to our Adapter
                            val adapter = resp?.let { TemplateAdapter(requireContext(),
                                it.reversed(),
                                this@TemplateFragment) }


                          val ReceivedCall =  resp?.filter { it.templatetype.equals("ReceivedCall") }
                          val DailedCalls =  resp?.filter { it.templatetype.equals("DailedCalls") }
                          val MissedCall =  resp?.filter { it.templatetype.equals("MissedCall") }

                            main(resp!!)
//                            main(DailedCalls!!)
//                            main(MissedCall!!)

                            Log.d(TAG, "dtime: ${main(resp)}")
//                            Log.d(TAG, "mtime: ${main(MissedCall!!)}")
//                            Log.d(TAG, "rtime: ${main(ReceivedCall!!)}")



                            // Setting the Adapter with the recyclerview
                            binding.rcyviewTemplatelist.adapter = adapter

                        }
                    }

                    ApiStatus.ERROR -> {
                            when (resource.code) {
                                404 -> {
                                    showDialog(
                                        "${res.message}",
                                        requireContext(),
                                        R.drawable.ic_close
                                    )
                                }

                                503 -> {
                                    showDialog(
                                        "${res.message}",
                                        requireContext(),
                                        R.drawable.ic_close
                                    )
                                }

                                500 -> {
                                    showDialog(
                                        res.message.toString(),
                                        requireContext(),
                                        R.drawable.ic_close
                                    )
                                }

                                else -> {
                                    showDialog(
                                        resource.message.toString(),
                                        requireContext(),
                                        R.drawable.ic_close
                                    )
                                }

                            }

                            Log.d(TAG, "response:${resource.code}")
                            Log.d(TAG, "responsemessage:${resource.message}")
                        }

                    ApiStatus.LOADING->{}

                }
        }
        }


        templateListViemodel.deleteTemplateListLiveData.observe(viewLifecycleOwner){
            response -> response.let {
             resource ->
                when(resource.status){

                    ApiStatus.SUCCESS ->{

                        Log.d(TAG, "onViewCreated:${response.message}")
                    }


                    ApiStatus.ERROR -> {
                        when (resource.code) {
                            404 -> {
                                showDialog(
                                    "${response.message}",
                                    requireContext(),
                                    R.drawable.ic_close
                                )
                            }

                            503 -> {
                                showDialog(
                                    "${response.message}",
                                    requireContext(),
                                    R.drawable.ic_close
                                )
                            }

                            500 -> {
                                showDialog(
                                    response.message.toString(),
                                    requireContext(),
                                    R.drawable.ic_close
                                )
                            }

                            else -> {
                                showDialog(
                                    resource.message.toString(),
                                    requireContext(),
                                    R.drawable.ic_close
                                )
                            }

                        }

                        Log.d(TAG, "response:${resource.code}")
                        Log.d(TAG, "responsemessage:${resource.message}")
                    }


                    ApiStatus.LOADING ->{

                    }
                }
        }
        }




    }

    companion object{
        const val TAG = "TemplateFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun deleteList(position: Int, List: TemplateList) {
        templateListViemodel.deleteTemplateList(
            DeleteTemplateBody(
               loginMobileNumber = AppPreferences.phoneNumber.toString(),
                messageTemplateId = List.messagetemplateid.toString(),
            )
        )

        templateListViemodel.getTemplateList(TemplateBody(
            loginMobileNumber = AppPreferences.phoneNumber.toString()
        ))

    }

    fun main(list: List<TemplateList>):List<TemplateList> {
        // Sample list of TemplateList objects
        val templateList = listOf(
            TemplateList(42, 25, "e", "2", "", "", "ReceivedCall", 1, "2024-04-16T01:14:36.833Z"),
            TemplateList(47, 25, "bh", "678", "", "", "ReceivedCall", 1, "2024-04-16T01:34:50.683Z"),
            TemplateList(48, 25, "ff", "556", "", "", "ReceivedCall", 1, "2024-04-16T01:49:32.341Z"),
            TemplateList(49, 25, "ff", "5563", "", "", "ReceivedCall", 1, "2024-04-16T01:52:55.495Z")
        )

        // Format pattern for parsing
        val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

        // Parse each createdon string into a Date object
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        val dates = list.map { sdf.parse(it.createdon) }

        // Find the greatest Date object
        val greatestDate = dates.maxOrNull()

        // Filter the TemplateList objects based on the greatest date
        val filteredList = list.filter { sdf.parse(it.createdon) == greatestDate }

        // Print the filtered list
        println("Filtered List based on Greatest Date and Time:")
        println(filteredList)
        Log.d(TAG, "main: ${filteredList}")

        return filteredList
    }
    override fun editList(position: Int, List: TemplateList) {
        AppPreferences.messageTemplateId = List.messagetemplateid.toString()
        AppPreferences.templateContent = List.templatecontent.toString()
        AppPreferences.templateLocation = List.templatelocation.toString()
        AppPreferences.templateName = List.templatename.toString()
        AppPreferences.templateImage = List.templateimage.toString()
        AppPreferences.templateType = List.templatetype.toString()
        findNavController().navigate(R.id.action_templateFragment_to_editTemplateFragment)
    }


}