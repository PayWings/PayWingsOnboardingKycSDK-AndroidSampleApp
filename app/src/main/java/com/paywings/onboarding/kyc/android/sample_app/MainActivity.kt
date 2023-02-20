package com.paywings.onboarding.kyc.android.sample_app

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.paywings.onboarding.kyc.android.sample_app.databinding.ActivityMainBinding
import com.paywings.onboarding.kyc.android.sdk.PayWingsOnboardingKyc
import com.paywings.onboarding.kyc.android.sdk.data.model.KycCredentials
import com.paywings.onboarding.kyc.android.sdk.data.model.KycSettings
import com.paywings.onboarding.kyc.android.sdk.data.model.KycUserData
import com.paywings.onboarding.kyc.android.sdk.util.PayWingsOnboardingKycResult
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var prefs: SharedPreferences
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val  KYC_SDK_ACTIVITY_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        setDefaultSettings()

        binding.btnStartKyc.setOnClickListener {
            PayWingsOnboardingKyc.startKyc(this@MainActivity,
                KYC_SDK_ACTIVITY_REQUEST_CODE,
                KycCredentials(prefs.getString("sdkEndpointUrl", "")!!, prefs.getString("sdkEndpointUsername", "")!!, prefs.getString("sdkEndpointPassword", "")!!),
                KycSettings(UUID.randomUUID().toString(), prefs.getString("language", "en")!!, prefs.getString("referenceNumber", null)),
                KycUserData(prefs.getString("userDataFirstName", "")!!, prefs.getString("userDataMiddleName", "")!!, prefs.getString("userDataLastName", "")!!, prefs.getString("userDataEmail", "")!!,   prefs.getString("userDataMobileNumber", "")!!, prefs.getString("userDataAddress1", "")!!
                    , prefs.getString("userDataAddress2", "")!!, prefs.getString("userDataAddress3", "")!!, prefs.getString("userDataZipCode", "")!!, prefs.getString("userDataCity", "")!!, prefs.getString("userDataState", "")!!, prefs.getString("userDataCountry", "")!!)
            )
        }

        binding.tvVersion.text = BuildConfig.VERSION_NAME.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == KYC_SDK_ACTIVITY_REQUEST_CODE) {
            when(val result = PayWingsOnboardingKyc.getKycResult(data!!)) {
                is PayWingsOnboardingKycResult.Success -> {

                    binding.tvStatus.text = "Successfull"
                    binding.tvStatus.setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.holo_green_dark))
                    binding.tvKycReferenceID.text = result.kycReferenceID?:""
                    binding.tvReferenceNumber.text = result.referenceNumber?:""
                    binding.tvPersonID.text = result.personID?:""
                    binding.tvKycID.text = result.kycID?:""

                }
                is PayWingsOnboardingKycResult.Failure -> {
                    binding.tvStatus.text = "Failed with status code: %s (%s)".format(result.statusCode, result.statusDescription)
                    binding.tvStatus.setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.holo_red_dark))
                    binding.tvKycReferenceID.text = result.kycReferenceID?:""
                    binding.tvReferenceNumber.text = result.referenceNumber?:""
                    binding.tvPersonID.text = result.personID?:""
                    binding.tvKycID.text = result.kycID?:""
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDefaultSettings() {

        if (!prefs.getBoolean("settingsSet", false)) {
            val editor = prefs.edit()
            editor.putString("sdkEndpointUrl", "https://onboarding-kyc-dev.paywings.io/mobile/")
            editor.putString("sdkEndpointUsername", "111")
            editor.putString("sdkEndpointPassword", "222")
            editor.putString("language", "en")
            editor.putBoolean("settingsSet", true)
            editor.commit()
        }

    }
}