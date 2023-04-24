package com.paywings.onboarding.kyc.android.sample_app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.rememberNavController
import com.paywings.onboarding.kyc.android.sample_app.network.NetworkState
import com.paywings.onboarding.kyc.android.sample_app.ui.nav.graph.StartUpNavGraph
import com.paywings.onboarding.kyc.android.sample_app.ui.theme.PayWingsOnboardingKycTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "KycSettings")

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkState: NetworkState

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        installSplashScreen()
        super.onCreate(savedInstanceState)
        networkState.start()
        setActivityContent()
    }

    override fun onDestroy() {
        super.onDestroy()
        networkState.destroy()
    }

    private fun setActivityContent() {
        setContent {
            PayWingsOnboardingKycTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    StartUpNavGraph(
                        navController,
                        onCloseApp = { onCloseApp() }
                    )
                }
            }
        }
    }

    private fun onCloseApp() {
        finishAndRemoveTask()
    }

}