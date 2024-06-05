package com.example.automatedwhatsappmessage.view

import android.animation.LayoutTransition
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.automatedwhatsappmessage.Common.AppPreferences
import com.example.automatedwhatsappmessage.Common.observeOnce
import com.example.automatedwhatsappmessage.Common.openAccessibilitySettings
import com.example.automatedwhatsappmessage.Common.setSafeOnClickListener
import com.example.automatedwhatsappmessage.Common.showDialog
import com.example.automatedwhatsappmessage.R
import com.example.automatedwhatsappmessage.ViewModel.ConfigViewModel
import com.example.automatedwhatsappmessage.adapter.TemplateAdapter
import com.example.automatedwhatsappmessage.api.ApiStatus
import com.example.automatedwhatsappmessage.api.AppStatusBody
import com.example.automatedwhatsappmessage.api.MessageTypeBody
import com.example.automatedwhatsappmessage.api.TemplateBody
import com.example.automatedwhatsappmessage.api.TemplateList
import com.example.automatedwhatsappmessage.databinding.FragmentConfigurationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


@AndroidEntryPoint
class ConfigurationFragment : Fragment() {

    private var _binding: FragmentConfigurationBinding? = null
    private val binding get() = _binding!!
    private var button:Int? = 0
    private var type:Boolean? = false

    private val configurationViewModel: ConfigViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentConfigurationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.layoutDailedCall.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.layoutReceived.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.layoutMissedCall.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.radioBusiness.isChecked = AppPreferences.whatsAppType?.trim().equals("personal")
        binding.radioPersonal.isChecked = AppPreferences.whatsAppType?.trim().equals("business")
        binding.switchServiceButton.isChecked = AppPreferences.serviceOnOff

        binding.radioAutomatic.isChecked = AppPreferences.messageType?.trim().equals("automatic")
        binding.radioManual.isChecked = AppPreferences.messageType?.trim().equals("Manual")
        type = AppPreferences.messageType?.trim().equals("Manual") || AppPreferences.messageType?.trim().equals("automatic")

        binding.radioBusiness.isEnabled = false
        binding.radioPersonal.isEnabled = false


        binding.cardDailedCall.setOnClickListener {
            val v = if (binding.textDdetails.visibility === View.GONE) View.VISIBLE else View.GONE
            TransitionManager.beginDelayedTransition(binding.layoutDailedCall, AutoTransition())
            binding.textDdetails.visibility = v
        }


        binding.cardReceivedContent.setOnClickListener {
            val v = if (binding.textRdetails.visibility === View.GONE) View.VISIBLE else View.GONE
            TransitionManager.beginDelayedTransition(binding.layoutReceived, AutoTransition())
            binding.textRdetails.visibility = v

        }


        binding.cardMissedCallField.setOnClickListener {
            val v = if (binding.textMdetails.visibility === View.GONE) View.VISIBLE else View.GONE
            TransitionManager.beginDelayedTransition(binding.layoutMissedCall, AutoTransition())
            binding.textMdetails.visibility = v

        }

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, _ ->
            type = true
            when (radioGroup.checkedRadioButtonId) {
                R.id.radio_manual -> {
                    AppPreferences.messageType = "Manual"
                    binding.radioManual.isChecked = true
                    binding.radioAutomatic.isChecked = false

                }
                R.id.radio_automatic -> {
                    AppPreferences.messageType = "automatic"
                    binding.radioAutomatic.isChecked = true
                    binding.radioManual.isChecked = false
                }

            }
            Log.d(TAG, "onViewCreated: ENV changed to " + AppPreferences.messageType)
        }

        binding.switchServiceButton.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // The toggle is enabled
                AppPreferences.serviceOnOff = true
               // requireActivity().openAccessibilitySettings()

                button = 1

            } else {
                AppPreferences.serviceOnOff = false
                //requireActivity().openAccessibilitySettings()
                button = 0
                // The toggle is disabled
            }
        })


        configurationViewModel.getTemplateList(
            TemplateBody(
                loginMobileNumber = AppPreferences.phoneNumber.toString()
            )
        )


        binding.buttonSave.setOnClickListener{
            if (type==false){
                Toast.makeText(requireContext(), "please select type", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            configurationViewModel.getAppStatusDetails(
                AppStatusBody(
                    LoginMobileNumber = AppPreferences.phoneNumber.toString(),
                    AppStatus = button.toString()
                )
            )
            Log.d(TAG, "onViewCreated: ${button}")

        }




        configurationViewModel.liveData.observe(viewLifecycleOwner) { res ->
                res.let { resource ->
                    when (resource.status) {
                        ApiStatus.SUCCESS -> {
                            configurationViewModel.getMessageTypeDetails(
                                MessageTypeBody(
                                    loginMobileNumber = AppPreferences.phoneNumber.toString(),
                                    messageType = AppPreferences.messageType?.trim()
                                )
                            )

                            Log.d(TAG, "onViewCreated:online")

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

                        ApiStatus.LOADING -> {}
                    }
                }
            }

        configurationViewModel.messageTypeLiveData.observe(viewLifecycleOwner) { res ->
            res.let { resource ->
                when (resource.status) {
                    ApiStatus.SUCCESS -> {
                        Log.d(TAG, "onViewCreated:online11")
                        lifecycleScope.launch(Dispatchers.Main) {
                            findNavController().navigate(R.id.action_configurationFragment_to_homeFragment)
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

                        Log.d(SignUpFragment.TAG, "response:${resource.code}")
                        Log.d(SignUpFragment.TAG, "responsemessage:${resource.message}")
                    }

                    ApiStatus.LOADING -> {}
                }
            }
        }





        Log.d(TAG, "onViewCreated: ${AppPreferences.phoneNumber}")


        configurationViewModel.templateLiveData.observe(viewLifecycleOwner){
                res -> res?.let {
                resource ->
            when(resource.status){
                ApiStatus.SUCCESS-> {
                    lifecycleScope.launch(Dispatchers.Main){
                        val resp = resource.data?.results
                        if (resp?.isNotEmpty() == true){

                            val ReceivedCall =  resp?.filter { it.templatetype.equals("ReceivedCall") }
                            val DailedCalls =  resp?.filter { it.templatetype.equals("DailedCall") }
                            val MissedCall =  resp?.filter { it.templatetype.equals("MissedCall") }

                            binding.textDailedField.text = DailedCalls.let { main(it!!).map { it.templatetype }.toString().replace("[", "").replace("]", "") }
                            binding.textDtname.text = DailedCalls.let { main(it!!).map { it.templatename }.toString().replace("[", "").replace("]", "") }
                            binding.textDdetails.text = DailedCalls.let { main(it!!).map { it.templatecontent }.toString().replace("[", "").replace("]", "") }


                            binding.textReceivedField.text = ReceivedCall?.let { main(it).map { it.templatetype }.toString().replace("[", "").replace("]", "") }
                            binding.textRtname.text = ReceivedCall?.let { main(it).map { it.templatename }.toString().replace("[", "").replace("]", "") }
                            binding.textRdetails.text = ReceivedCall?.let { main(it).map { it.templatecontent }.toString().replace("[", "").replace("]", "")}


                            binding.textMissedField.text = MissedCall?.let { main(it).map { it.templatetype }.toString().replace("[", "").replace("]", "") }
                            binding.textMtname.text = MissedCall?.let { main(it).map { it.templatename }.toString().replace("[", "").replace("]", "") }
                            binding.textMdetails.text = MissedCall?.let { main(it).map { it.templatecontent }.toString().replace("[", "").replace("]", "") }


                        }



//                            Log.d(TAG, "mtime: ${main(MissedCall!!)}")
//                            Log.d(TAG, "rtime: ${main(ReceivedCall!!)}")



                        // Setting the Adapter with the recyclerview

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

                    Log.d(TemplateFragment.TAG, "response:${resource.code}")
                    Log.d(TemplateFragment.TAG, "responsemessage:${resource.message}")
                }

                ApiStatus.LOADING->{}

            }
        }
        }



    }


    fun main(list: List<TemplateList>):List<TemplateList> {
        // Sample list of TemplateList objects

        // Format pattern for parsing
        val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

        // Parse each createdon string into a Date object
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        val dates = list.map { sdf.parse(it.createdon) }

        // Find the greatest Date object
        val greatestDate = dates?.maxOrNull()

        // Filter the TemplateList objects based on the greatest date
        val filteredList = list.filter { sdf.parse(it.createdon) == greatestDate }

        // Print the filtered list
        println("Filtered List based on Greatest Date and Time:")
        println(filteredList)
        Log.d(TemplateFragment.TAG, "main: ${filteredList}")

        return filteredList
    }


    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }

    companion object{
        const val TAG = "ConfigurationFragment"
    }




}
