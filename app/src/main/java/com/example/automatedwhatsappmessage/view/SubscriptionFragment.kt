package com.example.automatedwhatsappmessage.view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.automatedwhatsappmessage.MainActivity
import com.example.automatedwhatsappmessage.R
import com.example.automatedwhatsappmessage.adapter.CallLogAdapter
import com.example.automatedwhatsappmessage.adapter.SubcribtionAdapter
import com.example.automatedwhatsappmessage.api.CallLogData
import com.example.automatedwhatsappmessage.api.SubsCriptionData
import com.example.automatedwhatsappmessage.databinding.FragmentHomeBinding
import com.example.automatedwhatsappmessage.databinding.FragmentSubscriptionBinding
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.roundToInt

class SubscriptionFragment : Fragment(), SubcribtionAdapter.clicklistener, PaymentResultListener {

    private var _binding: FragmentSubscriptionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSubscriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyviewPack.layoutManager = layoutManager

        val data = ArrayList<SubsCriptionData>()
        data.add(SubsCriptionData("259", "THE SUPER VALUE PACK", "1 Month"))
        data.add(SubsCriptionData("359", "THE SUPER VALUE PACK", "3 Month"))
        data.add(SubsCriptionData("459", "THE SUPER VALUE PACK", "6 month"))
        data.add(SubsCriptionData("734", "INSTANT VALUE PACK", "1 year"))
        data.add(SubsCriptionData("854", "INSTANT VALUE PACK", "2 year"))
        data.add(SubsCriptionData("954", "INSTANT VALUE PACK", "3 year"))

        val adapter = SubcribtionAdapter(requireContext(), data, this@SubscriptionFragment)
        binding.recyviewPack.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun imageclick() {
        payment(3000F, requireActivity())
    }

    fun payment(amount: Float, context: Activity) {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_x2dF6tYOrfPZ0G")

        val amountInPaise = (amount * 100).toInt()

        val options = JSONObject()
        try {
            options.put("name", "Your App Name")
            options.put("description", "Test payment")
            options.put("currency", "INR")
            options.put("amount", amountInPaise)

            val retryObject = JSONObject()
            retryObject.put("enabled", true)
            retryObject.put("max_count", 4)
            options.put("retry", retryObject)

            checkout.open(context, options)
        } catch (e: JSONException) {
            e.printStackTrace()
          //  Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(requireContext(), "Payment Successful: $p0", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "onPaymentSuccess: $p0")
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(requireContext(), "Payment Error: $p1", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "onPaymentError: $p0, $p1")
    }

    companion object {
        const val TAG = "SubscriptionFragment"
    }
}



