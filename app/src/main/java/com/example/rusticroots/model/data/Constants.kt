package com.example.rusticroots.model.data

import com.google.android.gms.wallet.WalletConstants

object Constants {
    /**
     * Types of networks to be requested from the API
     */
    val SUPPORTED_NETWORKS = listOf(
        "MASTERCARD",
        "VISA",
        "AMEX",
        "DISCOVER",
        "JCB",
    )

    /**
     * Accepted authentication methods
     */
    val SUPPORTED_METHODS = listOf(
        "PAN_ONLY",
        "CRYPTOGRAM_3DS",
    )

    /**
     * Need to enable ENVIRONMENT_PRODUCTION for cards to be charged
     */
    const val PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST

    /**
     * The name of your payment processor/gateway.
     * ex. Pay.com: https://apiref.pay.com/reference/googlepay
     */
    private const val PAYMENT_GATEWAY_TOKENIZATION_NAME = "example"

    /**
     * Custom parameters required by the processor/gateway.
     *
     * Dummy data for testing
     * */
    val PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS = mapOf(
        "gateway" to PAYMENT_GATEWAY_TOKENIZATION_NAME,
        "gatewayMerchantId" to "exampleGatewayMerchantId"
    )

    /**
     * Required by the API, but not visible to the user.
     */
    const val COUNTRY_CODE = "FI"
    const val CURRENCY_CODE = "EUR"

    /**
     * Merchant Info
     * Merchant ID to be modified once application is approved in Google Pay Business Console
     */
    const val MERCHANT_NAME = "RusticRoots"
    const val MERCHANT_ID = "01234567890123456789"

    const val COLLECTION_BOOKINGS = "bookings"
    const val COLLECTION_TABLES = "tables"
}