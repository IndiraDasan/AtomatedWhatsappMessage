package com.example.automatedwhatsappmessage.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.automatedwhatsappmessage.MainActivity
import com.example.automatedwhatsappmessage.R
import com.example.automatedwhatsappmessage.databinding.FragmentPaymentSuccessBinding
import com.example.automatedwhatsappmessage.databinding.FragmentSubscriptionBinding

class paymentSuccessFragment : Fragment() {


    private var _binding: FragmentPaymentSuccessBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPaymentSuccessBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSubmit.setOnClickListener {
            findNavController().navigate(R.id.action_paymentSuccessFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}