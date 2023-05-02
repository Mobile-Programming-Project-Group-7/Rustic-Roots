package com.example.rusticroots.ui.modules

import android.app.Activity
import android.app.PendingIntent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rusticroots.viewmodel.PaymentGViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.wallet.PaymentData
import com.example.rusticroots.R
import com.google.android.gms.common.api.CommonStatusCodes
import org.json.JSONException
import org.json.JSONObject

@Composable
fun GooglePayButton(paymentGVM: PaymentGViewModel = viewModel()) {
    val resolvePaymentForResult = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            result: ActivityResult ->
        when (result.resultCode) {
            Activity.RESULT_OK ->
                result.data?.let { intent ->
                    PaymentData.getFromIntent(intent)?.let(::handlePaymentSuccess)
                    Log.e("****","RESULT_OK")
                }

            Activity.RESULT_CANCELED -> {
                Log.e("****","RESULT_CANCELLED")
            // The user cancelled the payment attempt
            }
        }
    }

    // Passes val resolvePaymentForResult to the requestPayment() function
    val resolvePayment: (PendingIntent) -> Unit = { r -> resolvePaymentForResult.launch(
        IntentSenderRequest.Builder(r).build()
    )}

    Button(
        onClick = {
            requestPayment(paymentGVM, resolvePayment)
        },
        modifier = Modifier
            .padding(8.dp)
            .defaultMinSize(minWidth = 152.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
    ) {
        // Imports Drawable "Pay with G Pay" Image
        Image(painter = painterResource(
            id = R.drawable.pay_with_googlepay_button_content),
            contentDescription = "Google pay Button"
        )
    }
}

private fun requestPayment(paymentGVM: PaymentGViewModel, resolvePayment: (PendingIntent) -> Unit){
    val dummyPriceCents = 100L
    val shippingCostCents = 900L
    val task = paymentGVM.getLoadPaymentDataTask(dummyPriceCents + shippingCostCents)

    task.addOnCompleteListener { completedTask ->
        if (completedTask.isSuccessful) {
            completedTask.result.let(::handlePaymentSuccess)
            Log.d("*************************", "completedTask.isSuccessful")
        } else {
            Log.e("*************************", "completedTask.isNOTSuccessful")
            when (val exception = completedTask.exception) {
                is ResolvableApiException -> {
                    Log.d("*************************", "ResolvableApiException")
                    resolvePayment(exception.resolution)
                }
                is ApiException -> {
                    handleError(exception.statusCode, exception.message)
                }
                else -> {
                    handleError(
                        CommonStatusCodes.INTERNAL_ERROR, "Unexpected non API" +
                                " exception when trying to deliver the task result to an activity!"
                    )
                }
            }
        }
    }
}

// Logs API ERROR
private fun handleError(statusCode: Int, message: String?) {
    Log.e("Google Pay API error", "Error code: $statusCode, Message: $message")
}

// Handles Payment Success
private fun handlePaymentSuccess(paymentData: PaymentData) {
    val paymentInformation = paymentData.toJson()

    try {
        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        val paymentMethodData = JSONObject(paymentInformation).getJSONObject("paymentMethodData")
        val billingName = paymentMethodData.getJSONObject("info")
            .getJSONObject("billingAddress").getString("name")
        Log.d("BillingName", billingName)

        //Toast.makeText(this, getString(R.string.payments_show_name, billingName), Toast.LENGTH_LONG).show()

        // Logging token string.
        Log.d("Google Pay token", paymentMethodData
            .getJSONObject("tokenizationData")
            .getString("token"))

    } catch (error: JSONException) {
        Log.e("handlePaymentSuccess", "Error: $error")
    }
}