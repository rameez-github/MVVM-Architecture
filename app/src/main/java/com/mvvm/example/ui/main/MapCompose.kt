package com.mvvm.example.ui.main


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.mvvm.example.R
import com.mvvm.example.component.GoBackNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
https://medium.com/@ridvanozcan48/how-to-use-google-maps-in-jetpack-compose-step-by-step-android-guide-55aedac89e43
 */
@Composable
fun MapCompose(navController: NavHostController) {

    val atasehir = LatLng(40.9971, 29.1007)
    val cameraPositionState = rememberCameraPositionState {
        //position = CameraPosition.fromLatLngZoom(atasehir, 15f)
    }

    Column(
        modifier = Modifier
            .background(color = colorResource(id = R.color.white))
            .fillMaxWidth()
            .fillMaxHeight()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {

        GoBackNavigation(
            modifier = Modifier,
            screenTitle = stringResource(id = R.string.pick_address),
            showDoneIconOnRight = true,
            onBackClicked = {
                navController.navigateUp()
            },
            onDoneClicked = {
                navController.previousBackStackEntry?.savedStateHandle?.set("latitude", cameraPositionState.position.target.latitude)
                navController.previousBackStackEntry?.savedStateHandle?.set("longitude", cameraPositionState.position.target.longitude)
                navController.navigateUp()
            }
        )

        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight(),
            cameraPositionState = cameraPositionState
        )
    }

    val ctx = LocalContext.current
    LaunchedEffect(Unit) {

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ctx)
        //delay(5000)
        getCurrentLocation(
            ctx = ctx,
            fusedLocationProviderClient = fusedLocationProviderClient,
            onGetCurrentLocationFailed = {
            },
            onGetCurrentLocationSuccess = { loc->
                //Toast.makeText(ctx, "Current Lat: ${loc.first} Long: ${loc.second}", Toast.LENGTH_LONG).show()
                //cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(loc.first, loc.second), 18f)
                CoroutineScope(Dispatchers.Main).launch {
                    cameraPositionState.animate(
                        update = CameraUpdateFactory.newCameraPosition(
                            CameraPosition(LatLng(loc.first, loc.second), 18f, 0f, 0f)
                        ),
                        durationMs = 2000
                    )
                }
            }
        )
    }
}