package com.example.automatedwhatsappmessage.view

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.automatedwhatsappmessage.Common.AppPreferences
import com.example.automatedwhatsappmessage.Common.setSafeOnClickListener
import com.example.automatedwhatsappmessage.Common.showDialog
import com.example.automatedwhatsappmessage.MainActivity
import com.example.automatedwhatsappmessage.R
import com.example.automatedwhatsappmessage.ViewModel.LoginViewModel
import com.example.automatedwhatsappmessage.api.ApiStatus
import com.example.automatedwhatsappmessage.api.Login
import com.example.automatedwhatsappmessage.api.RegisterLogin
import com.example.automatedwhatsappmessage.api.RetrofitClient
import com.example.automatedwhatsappmessage.api.UserProfile
import com.example.automatedwhatsappmessage.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding:FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel:LoginViewModel by viewModels()

    private var lastClickTime: Long = 0
    private val clickDelay = 1000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.includeSigninbg.imageBackButton.visibility = View.INVISIBLE

        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        if (AppPreferences.isConfigured==true){
            findNavController().navigate(R.id.action_sigInFragment_to_homeFragment)
            (activity as MainActivity).enableTheSwipeGesture()
        }else{
            (activity as MainActivity).disableTheSwipeGesture()

        }

        Log.d(TAG, "onViewCreated:${AppPreferences.isConfigured}")

        binding.textSignup.setOnClickListener {
            findNavController().navigate(R.id.singUpFragment)
        }

        binding.buttonSubmit.setSafeOnClickListener {


                if (binding.editUsername.text.isEmpty()) {
                    binding.editUsername.error = "Number required"
                    binding.editUsername.requestFocus()
                    return@setSafeOnClickListener
                }

                if (binding.editPassword.text.isEmpty()) {
                    binding.editPassword.error = "Password required"
                    binding.editPassword.requestFocus()
                    return@setSafeOnClickListener
                }


                loginViewModel.getLoginDetails(
                    Login(
                        userPassword = binding.editPassword.text.toString(),
                        loginMobileNumber = binding.editUsername.text.toString(),
                    )
                )

        }

        loginViewModel.liveData.observe(viewLifecycleOwner) { res ->
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                res.let { resource ->
                    when (resource.status) {
                        ApiStatus.SUCCESS -> {
                            binding.buttonSubmit.isEnabled = false
                            val response = resource.data?.results
                            AppPreferences.phoneNumber = resource.data?.results?.map { it.loginmobilenumber }.toString().replace("[", "").replace("]", "")
                            AppPreferences.whatsAppType = resource.data?.results?.map { it.accounttype }.toString().replace("[", "").replace("]", "")
                            AppPreferences.deviceUniqueId = resource.data?.results?.map { it.deviceuniqueid }.toString().replace("[", "").replace("]", "")
                            AppPreferences.isConfigured = true
                            AppPreferences.serviceOnOff = resource.data?.results?.map { it.deviceuniqueid }?.equals(1) == true
                            Log.d(TAG, "onViewCreated:${response}")
                            Log.d(TAG, "onViewCreated:${AppPreferences.phoneNumber!!}")
                            Handler(requireContext().mainLooper).postDelayed({
                            findNavController().navigate(R.id.action_sigInFragment_to_homeFragment)
                        },2000)
                        }

                        ApiStatus.ERROR -> {
                            binding.buttonSubmit.isEnabled = true
                            when (resource.code) {
                                404 -> {
                                    showDialog(
                                        "${res.message}",
                                        requireContext(),
                                        R.drawable.ic_close
                                    )
                                }

                                400-> {
                                    showDialog("${res.message}", requireContext(), R.drawable.ic_close
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
                            Log.d(TAG, "responsemessage:${resource.message.toString()}")
                        }

                        ApiStatus.LOADING -> {}
                    }
                }
            }
        }


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        const val TAG = "SignInFragment"
    }



}