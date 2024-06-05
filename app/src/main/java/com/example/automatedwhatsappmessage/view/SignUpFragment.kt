package com.example.automatedwhatsappmessage.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.automatedwhatsappmessage.Common.AppPreferences
import com.example.automatedwhatsappmessage.Common.showDialog
import com.example.automatedwhatsappmessage.MainActivity
import com.example.automatedwhatsappmessage.R
import com.example.automatedwhatsappmessage.ViewModel.SignUpViewModel
import com.example.automatedwhatsappmessage.api.ApiStatus
import com.example.automatedwhatsappmessage.api.RegisterLogin
import com.example.automatedwhatsappmessage.databinding.FragmentSingUpBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar


@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSingUpBinding? = null
    private val binding get() = _binding!!

    private val signUpViewModel:SignUpViewModel by viewModels()

    private var genderType = arrayOf(
        "MALE","FEMALE"
    )
    private var accountType = arrayOf(
        "PERSONAL","BUSINESS"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSingUpBinding.inflate(inflater,container,false)
        return binding.root
    }


    @SuppressLint("HardwareIds")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.editDob?.setOnClickListener { showDateTimeDialog(binding.editDob) }

        // on below line we are getting device id.
        val android_device_id = Settings.Secure.getString(requireActivity().contentResolver, Settings.Secure.ANDROID_ID)
        val name  = Build.BRAND

        binding.includeSignupbg.imageBackButton.visibility = View.GONE

        val genderAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireActivity(), R.layout.customlayout,genderType)
        val typeAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireActivity(), R.layout.customlayout,accountType)

        // Give the suggestion after 1 words.

        // Give the suggestion after 1 words.
        binding.editType.setThreshold(1)
        binding.editGender.setThreshold(1)

        // Set the adapter for data as a list

        // Set the adapter for data as a list
        binding.editType.setAdapter(typeAdapter)
        binding.editGender.setAdapter(genderAdapter)
        binding.editType.setTextColor(Color.BLACK)
        binding.editGender.setTextColor(Color.BLACK)



        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        if (AppPreferences.isConfigured==false){
            (activity as MainActivity).disableTheSwipeGesture()

        }


        binding.submitBtn.setOnClickListener {

            if (binding.editName.text.isEmpty()){
                binding.editName.error = "name required"
                binding.editName.requestFocus()
                return@setOnClickListener
            }


            if (binding.editEmail.text.isEmpty()){
                binding.editEmail.error = "Email required"
                binding.editEmail.requestFocus()
                return@setOnClickListener
            }


            if (binding.editDob.text.isEmpty()){
                binding.editDob.error = "DOB required"
                binding.editDob.requestFocus()
                return@setOnClickListener
            }

            if (binding.editGender.text.isEmpty()){
                binding.editGender.error = "Gender required"
                binding.editGender.requestFocus()
                return@setOnClickListener
            }


            if (binding.editType.text.isEmpty()){
                binding.editType.error = " Type required"
                binding.editType.requestFocus()
                return@setOnClickListener
            }

            if (binding.editPassword.text.isEmpty()){
                binding.editPassword.error = "Password required"
                binding.editPassword.requestFocus()
                return@setOnClickListener
            }


            signUpViewModel.createRegister(
                RegisterLogin(
                    deviceUniqueId = android_device_id,
                    deviceName = name,
                    loginMobileNumber = binding.editMobilenumber.text.toString(),
                    userPassword = binding.editPassword.text.toString(),
                    email = binding.editEmail.text.toString(),
                    customerName = binding.editName.text.toString(),
                    accountType = binding.editType.text.toString(),
                    businessMobileNumber = binding.editMobilenumber.text.toString(),
                    businessAddress = "34343",
                    businessName = "wwww"
            ))
        }

        signUpViewModel.liveData.observe(viewLifecycleOwner) { res ->
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                res.let { resource ->
                    when (resource.status) {
                        ApiStatus.SUCCESS -> {
                            Handler(requireContext().mainLooper).postDelayed({
                                findNavController().navigate(R.id.action_singUpFragment_to_sigInFragment)
                            },2000)
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
        }






    }

    private fun showDateTimeDialog(date_in:TextView?){
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar[Calendar.YEAR] = year
            calendar[Calendar.MONTH] = month
            calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            val dat = (dayOfMonth.toString() + "-" + (month + 1) + "-" + year)
            date_in?.text = dat

        }
        DatePickerDialog(this.requireContext(), dateSetListener, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]).show()
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{

        const val TAG = "SingUpFragment"
    }

}