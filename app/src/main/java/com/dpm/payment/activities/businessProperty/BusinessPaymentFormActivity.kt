package com.dpm.payment.activities.businessProperty

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dpm.payment.exfun.afterTextChanged
import com.dpm.payment.exfun.isVisible
import com.dpm.payment.kotlinHelper.ExFun.Companion.disableGesture
import com.dpm.payment.kotlinHelper.ExFun.Companion.selected
import com.dpm.payment.kotlinHelper.ExFun.Companion.toEditable
import com.dpm.payment.retrofit.Utills.ApiRequest
import com.dpm.payment.retrofit.interfaces.OnCallBackListner
import com.dpm.payment.utils.PrefUtil
import com.payment.databinding.ActivityBusinessPaymentFormBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap

class BusinessPaymentFormActivity : AppCompatActivity(), OnCallBackListner {

    lateinit var binding: ActivityBusinessPaymentFormBinding

    val apiRequest by lazy { ApiRequest(this, this) }


    var method: String? = ""
    var businessId: String? = ""
    var feesType: String? = ""
    var BusinessType: String? = ""
    var BusinessCategory: String? = ""
    var RegistrationFee: String? = ""
    var PaymentStatus: String? = ""
    var plenty: Double = 0.0

    var isRegistration = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessPaymentFormBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        BusinessType = intent.getStringExtra("BusinessType")
        BusinessCategory = intent.getStringExtra("BusinessCategory")
        RegistrationFee = (intent.getStringExtra("RegistrationFee")?.toDoubleOrNull()
            ?: 0.0.toString()).toString()
        PaymentStatus = intent.getStringExtra("PaymentStatus")
        feesType = intent.getStringExtra("option")
        businessId = intent.getStringExtra("businessId")
        method = intent.getStringExtra("method")
        plenty = intent.getStringExtra("plenty")?.toDoubleOrNull() ?: 0.0

        Log.d("fees_type", feesType.toString())
        isRegistration = feesType?.startsWith("Registration") == true

        // if (!isRegistration) {
        binding.checkboxPlenty.visibility = if (isRegistration) View.GONE else View.VISIBLE
        binding.tvDiscount.visibility = if (isRegistration) View.GONE else View.VISIBLE
        binding.edtDiscount.visibility = if (isRegistration) View.GONE else View.VISIBLE
        // }


        setDetails()

        //plenty = 20.0
        if (!isRegistration) {
            binding.checkboxPlenty.text = "plenty $plenty"
            binding.checkboxPlenty.setOnCheckedChangeListener { compoundButton, b ->
                calculatePayableAmount()
            }



            binding.edtDiscount.afterTextChanged {
                val currentTotal = binding.edtPrice.text.toString().toDouble()
                val discount = it.toDoubleOrNull() ?: 0.0
                if (discount > currentTotal) {
                    binding.edtDiscount.text = "".toEditable()
                }
                calculatePayableAmount()

            }


        }


    }

    private fun calculatePayableAmount() {
        binding.apply {
            val subTotal = RegistrationFee?.toDoubleOrNull() ?: 0.0
            val totalDiscount = edtDiscount.text.toString().toDoubleOrNull() ?: 0.0
            if (checkboxPlenty.isChecked) {
                binding.edtPrice.text = "${subTotal - (totalDiscount + plenty)}".toEditable()
            } else binding.edtPrice.text = "${subTotal - totalDiscount}".toEditable()
        }

    }

    private fun setDetails() {
        binding?.apply {
            tvBusinessType.disableGesture()
            tvBusinessCategory.disableGesture()
            toolbarText.text = "Business ID : $businessId"
            tvDate.text = SimpleDateFormat("dd/MM/yyyy").format(Date())
            tvFeesType.text = feesType
            tvBusinessCategory.text = BusinessCategory?.toEditable()
            tvBusinessType.text = BusinessType?.toEditable()
            edtPrice.text = RegistrationFee?.toEditable()

            val paymentType =
                if (feesType?.startsWith("Registration") == true) listOf("Full Payment") else listOf(
                    "Full Payment", "partial"
                )

            val sportsArrayAdapter = ArrayAdapter(
                applicationContext, android.R.layout.simple_spinner_dropdown_item, paymentType
            )
            spinnerPayment.adapter = sportsArrayAdapter
            spinnerPayment.selected { position, parent ->
                spinnerPayment.tag = spinnerPayment.selectedItem
                tvPayingAMount.isVisible = spinnerPayment.selectedItem == "partial"
                edtPayingAmount.isVisible = tvPayingAMount.isVisible
            }


        }

        binding?.btnSave?.setOnClickListener {


            if (validate()) {
                val map = HashMap<String, String>().apply {
                    this["business_id"] = businessId.toString()
                    this["method"] = method.toString()
                    this["pay_type"] = binding.tvPayType.text.toString()
                    this["payer_name"] = binding.edtPayerName.text.toString()
                    this["pay_taken_by"] = binding.edtPayTakenBy.text.toString()
                    this["price"] = binding.edtPrice.text.toString()
                    this["payment_type"] = binding.spinnerPayment.selectedItem.toString()
                    if (!isRegistration) {
                        this["discount"] = binding.edtDiscount.text.toString()
                        this["plenty"] = if (binding.checkboxPlenty.isChecked) "0" else "1"
                        if (binding.spinnerPayment.selectedItem == "partial") {
                            this["price"] = binding.edtPayingAmount.text.toString()
                        }

                    }
                }
                apiRequest.callPostFormData(
                    "http://wardc.org/apiv2/payment-save",
                    map,
                    PrefUtil.getAuthType(this) + " " + PrefUtil.getToken(this),
                    ""
                )
            }


        }
    }

    fun validate(): Boolean {
        if (binding.edtPrice!!.text.isEmpty()) {
            Toast.makeText(this, "Please enter Price", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.tvPayType!!.text.isEmpty()) {
            Toast.makeText(this, "Please enter PayType", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.edtPayerName!!.text.isEmpty()) {
            Toast.makeText(this, "Please enter PayerName", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.edtPayTakenBy!!.text.isEmpty()) {
            Toast.makeText(this, "Please enter PayTakenBy", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    override fun OnCallBackSuccess(tag: String?, response: String?) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun OnCallBackError(tag: String?, error: String?, i: Int) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }
}