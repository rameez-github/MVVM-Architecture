package com.mvvm.example.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvvm.example.R
import com.mvvm.example.component.ChooseSuggestion
import com.mvvm.example.component.GoBackNavigation
import com.mvvm.example.component.InputFieldState
import com.mvvm.example.ui.theme.MVVMTheme

class SmartInputActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        setContent {
            MVVMTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    SmartInputScreen()
                }
            }
        }
    }

    @Composable
    private fun SmartInputScreen(
        smartInputViewModel: SmartInputViewModel = hiltViewModel<SmartInputViewModel>()
    ) {
        val state = smartInputViewModel.suggestionsList.collectAsState(initial = emptyList())
        val suggestions = state.value

        Column {

            var input by rememberSaveable { mutableStateOf("") }

            GoBackNavigation(
                modifier = Modifier,
                screenTitle = intent.getStringExtra("input_type") ?: "",
                onBackClicked = {
                    finish()
                },
            )

            InputFieldState(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                label = stringResource(id = R.string.store_name),
                //changedText = viewState.updateCompanyRequest.m_address.value,
                changedText = input,
                keyboardType = KeyboardType.Text) {
                //viewState.updateCompanyRequest.m_address.value = it
                input = it
            }

            ChooseSuggestion(
                suggestionList = suggestions,
                modifier = Modifier.padding(
                    top = 8.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                isChipSelected = { code ->
                    //selectedSuggestion.any { str->
                        //str == code
                    //}
                    false
                },
                onSelectDeviceData = { sData->
                    /*if (selectedDevices.contains(sData?.device_code)) {
                        selectedDevices.remove(sData?.device_code)
                    } else {
                        selectedDevices.add(sData?.device_code?:"")
                    }
                    updateAlertRequest()
                    dashBoardViewModel.getPreviousAlertsCount()*/
                }
            )
        }
    }
}