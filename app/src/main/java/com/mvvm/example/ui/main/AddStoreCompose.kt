package com.mvvm.example.ui.main


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
//import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.mvvm.example.R
import com.mvvm.example.component.GoBackNavigation
import com.mvvm.example.component.InputFieldState
import com.mvvm.example.component.RequestLocationPermission
import com.mvvm.example.component.TextAction
import com.mvvm.example.data.room.model.StoreData
import com.mvvm.example.ui.theme.MVVMTheme
import com.mvvm.example.utilities.MediaManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File


@Composable
fun AddStoreCompose (
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    addStore: (storeData: StoreData) -> Unit,
    updateStore: (storeData: StoreData) -> Unit
){

    var store_name by rememberSaveable { mutableStateOf(homeViewModel.selectedStore.value?.store_name ?: "") }
    var address by rememberSaveable { mutableStateOf(homeViewModel.selectedStore.value?.address ?: "") }
    var road by rememberSaveable { mutableStateOf(homeViewModel.selectedStore.value?.road ?: "") }
    var store_number by rememberSaveable { mutableStateOf(homeViewModel.selectedStore.value?.store_number?.toString() ?: "") }
    var phone by rememberSaveable { mutableStateOf(homeViewModel.selectedStore.value?.phone ?: "") }
    var latitude by remember { mutableDoubleStateOf(homeViewModel.selectedStore.value?.latitude ?: 0.0) }
    var longitude by remember { mutableDoubleStateOf(homeViewModel.selectedStore.value?.longitude ?: 0.0) }
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var capturedImageUri by rememberSaveable {
        var fileUri: Uri? = null
        homeViewModel.selectedStore.value?.store_image_path?.let { path->
            fileUri = Uri.fromFile (File(path))
        }
        mutableStateOf<Uri?>(fileUri)
    }

    var capturedImageFile by rememberSaveable {
        var file: File? = null
        homeViewModel.selectedStore.value?.store_image_path?.let { path->
            file = File (path)
        }
        mutableStateOf<File?>(file)
    }

    val cameraPositionState = rememberCameraPositionState {
        homeViewModel.selectedStore.value?.latitude?.let {
            position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 18f)
        }
    }
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val savedLatitude = savedStateHandle?.getLiveData<Double>("latitude")
    val savedLongitude = savedStateHandle?.getLiveData<Double>("longitude")

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    // Observe the result
    savedLatitude?.observe(lifecycleOwner) { resultValue ->
        // Do something with the result
        android.util.Log.d("editstore", "editstore ${resultValue}")
        latitude = resultValue
        savedLongitude?.observe(lifecycleOwner) { resultValue2 ->
            // Do something with the result
            longitude = resultValue2

            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition(LatLng(latitude, longitude), 18f, 0f, 0f)
                    ),
                    durationMs = 2000
                )
            }
        }
    }

    val launcherInputActivity = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {

        }
    }

    Column(
        modifier = Modifier
            .background(color = colorResource(id = R.color.white))
            .fillMaxWidth()
            .fillMaxHeight()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {

        val ctx = LocalContext.current

        val locationSettingsLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            // Check the result code
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                // Location settings were enabled
                android.util.Log.d("current", "taken enabled true")

            } else {
                // Location settings were not enabled
                android.util.Log.d("current", "taken enabled false")

            }
        }

        RequestLocationPermission ({
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ctx)
            //Toast.makeText(ctx, "Permission granted", Toast.LENGTH_LONG).show()
            /*getLastUserLocation(
                ctx = ctx,
                fusedLocationProviderClient = fusedLocationProviderClient,
                onGetLastLocationFailed = {
                    Toast.makeText(ctx, "Last Location denied", Toast.LENGTH_LONG).show()
                },
                onGetLastLocationSuccess = { loc->
                    Toast.makeText(ctx, "Last Lat: ${loc.first} Long: ${loc.second}", Toast.LENGTH_LONG).show()
                }
            )*/

            getCurrentLocation(
                ctx = ctx,
                fusedLocationProviderClient = fusedLocationProviderClient,
                onGetCurrentLocationFailed = {
                    //Toast.makeText(ctx, "Current Location denied", Toast.LENGTH_LONG).show()
                    locationSettingsLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                                             },
                onGetCurrentLocationSuccess = { loc->
                    //Toast.makeText(ctx, "Current Lat: ${loc.first} Long: ${loc.second}", Toast.LENGTH_LONG).show()
                }
            )
        }, {
            Toast.makeText(ctx, "Permission denied", Toast.LENGTH_LONG).show()
        }, {})

        GoBackNavigation(
            modifier = Modifier,
            screenTitle = stringResource(id =
            if (homeViewModel.selectedStore.value == null) R.string.add_store
            else R.string.edit_store),
            onBackClicked = {
                navController.navigateUp()
            },
        )

        LazyColumn {

            item {

                InputFieldState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 16.dp, end = 16.dp),
                    label = stringResource(id = R.string.store_no),
                    //changedText = viewState.updateCompanyRequest.m_city.value,
                    changedText = store_number,
                    keyboardType = KeyboardType.Number) {
                    //viewState.updateCompanyRequest.m_city.value = it
                    store_number = it
                }
            }

            item {

                val activity = ctx as ComponentActivity

                InputFieldState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        /*.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                launcherInputActivity.launch(
                                    Intent(
                                        activity,
                                        SmartInputActivity::class.java
                                    )
                                )
                            }
                        )*/,
                    label = stringResource(id = R.string.store_name),
                    enabled = true,
                    //changedText = viewState.updateCompanyRequest.m_address.value,
                    changedText = store_name,
                    keyboardType = KeyboardType.Text) {
                    //viewState.updateCompanyRequest.m_address.value = it
                    store_name = it
                }
            }

            item {

                InputFieldState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 16.dp, end = 16.dp),
                    label = stringResource(id = R.string.address),
                    //changedText = viewState.updateCompanyRequest.m_city.value,
                    changedText = address,
                    keyboardType = KeyboardType.Text) {
                    //viewState.updateCompanyRequest.m_city.value = it
                    address = it
                }
            }

            item {

                InputFieldState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 16.dp, end = 16.dp),
                    label = stringResource(id = R.string.road),
                    //changedText = viewState.updateCompanyRequest.m_city.value,
                    changedText = road,
                    keyboardType = KeyboardType.Text) {
                    //viewState.updateCompanyRequest.m_city.value = it
                    road = it
                }
            }

            item {

                InputFieldState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 16.dp, end = 16.dp),
                    label = stringResource(id = R.string.phone),
                    //changedText = viewState.updateCompanyRequest.m_city.value,
                    changedText = phone,
                    keyboardType = KeyboardType.Phone) {
                    //viewState.updateCompanyRequest.m_city.value = it
                    phone = it
                }
            }

            if (latitude != 0.0 && longitude != 0.0) {
                item {

                    val uiSettings by remember {
                        mutableStateOf(MapUiSettings(
                            zoomControlsEnabled = false,
                            zoomGesturesEnabled = false,
                            scrollGesturesEnabled = false
                        ))
                    }
                    val properties by remember {
                        mutableStateOf(MapProperties(mapType = MapType.SATELLITE))
                    }

                    GoogleMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(top = 24.dp, start = 16.dp, end = 16.dp),
                        cameraPositionState = cameraPositionState,
                        properties = properties,
                        uiSettings = uiSettings
                    ) {
                        Marker(
                            state = MarkerState(position = LatLng(latitude, longitude)),
                            title = "Current Location"
                        )
                    }
                }
            }

            item {

                TextAction(
                    textValue = stringResource(id = R.string.change_location),
                    textColor = R.color.white,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
                ) {

                    navController.navigate(route = HomeNavEnum.MapCompose.name)
                }
            }

            item {

                val launcher = rememberLauncherForActivityResult(

                    contract = ActivityResultContracts.TakePicture(),
                    onResult = { isTaken: Boolean ->
                        if (isTaken) {
                            capturedImageUri = imageUri
                            android.util.Log.d("taken", "taken ${capturedImageFile?.exists()} ${capturedImageFile?.absolutePath}")
                        }
                    }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(horizontal = 16.dp)
                        .background(
                            color = colorResource(id = R.color.CCCC2DC),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {

                        }
                ) {

                    if (capturedImageUri == null) {
                        IconButton(
                            onClick = {
                                capturedImageFile = MediaManager.getAppPath(ctx)
                                imageUri = FileProvider.getUriForFile(
                                    ctx,
                                    ctx.packageName + ".provider",  //(use your app signature + ".provider" )
                                    capturedImageFile!!
                                )

                                //val takePictureFromCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                //takePictureFromCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                                launcher.launch(imageUri!!)
                            },
                            modifier = Modifier
                                .align(alignment = Alignment.Center)
                                //.size(48.dp)
                                .wrapContentWidth()
                                .wrapContentHeight()
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_photo_camera_24),
                                contentDescription = stringResource(R.string.navigation_action_back_cd),
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    } else {

                        val painter = rememberAsyncImagePainter(model = ImageRequest.Builder(LocalContext.current)
                            .data(capturedImageUri)
                            //.placeholder(R.drawable.ic_add_company_logo)
                            .crossfade(false)
                            .build())

                        Image(
                            modifier = Modifier
                                .align(alignment = Alignment.Center)
                                .padding(16.dp, 8.dp),
                            painter = painter/*rememberImagePainter(capturedImageUri)*/,
                            contentDescription = null
                        )

                        IconButton(
                            onClick = {
                                capturedImageUri = null
                                capturedImageFile = null
                                imageUri = null
                            },
                            modifier = Modifier
                                .align(alignment = Alignment.TopEnd)
                                .background(
                                    color = colorResource(id = R.color.CCCC2DC),
                                    shape = CircleShape
                                )
                                .wrapContentWidth()
                                .wrapContentHeight()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(R.string.navigation_action_back_cd),
                                tint = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                }
            }

            item {

                TextAction(
                    textValue = stringResource(
                        id = if (homeViewModel.selectedStore.value == null) R.string.save else R.string.update
                    ),
                    textColor = R.color.white,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
                ) {
                    val data = StoreData(
                        id = homeViewModel.selectedStore.value?.id?: 0,
                        store_name = store_name,
                        store_number = if (store_number.isEmpty()) 0 else store_number.toInt(),
                        address = address,
                        road = road,
                        latitude = latitude,
                        longitude = longitude,
                        phone = phone,
                        store_image_path = capturedImageFile?.absolutePath?: ""
                    )
                    if (homeViewModel.selectedStore.value == null) {
                        addStore(data)
                    } else {
                        updateStore(data)
                    }
                }
            }
        }
    }
}


/**
 * Retrieves the last known user location asynchronously.
 *
 * @param onGetLastLocationSuccess Callback function invoked when the location is successfully retrieved.
 *        It provides a Pair representing latitude and longitude.
 * @param onGetLastLocationFailed Callback function invoked when an error occurs while retrieving the location.
 *        It provides the Exception that occurred.
 */
@SuppressLint("MissingPermission")
private fun getLastUserLocation(
    ctx: Context,
    fusedLocationProviderClient: FusedLocationProviderClient,
    onGetLastLocationSuccess: (Pair<Double, Double>) -> Unit,
    onGetLastLocationFailed: (Exception) -> Unit
) {
    // Check if location permissions are granted
    if (areLocationPermissionsGranted(ctx)) {
        // Retrieve the last known location
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    // If location is not null, invoke the success callback with latitude and longitude
                    onGetLastLocationSuccess(Pair(it.latitude, it.longitude))
                }
            }
            .addOnFailureListener { exception ->
                // If an error occurs, invoke the failure callback with the exception
                onGetLastLocationFailed(exception)
            }
    }
}


/**
 * Retrieves the current user location asynchronously.
 *
 * @param onGetCurrentLocationSuccess Callback function invoked when the current location is successfully retrieved.
 *        It provides a Pair representing latitude and longitude.
 * @param onGetCurrentLocationFailed Callback function invoked when an error occurs while retrieving the current location.
 *        It provides the Exception that occurred.
 * @param priority Indicates the desired accuracy of the location retrieval. Default is high accuracy.
 *        If set to false, it uses balanced power accuracy.
 */
@SuppressLint("MissingPermission")
fun getCurrentLocation(
    ctx: Context,
    fusedLocationProviderClient: FusedLocationProviderClient,
    onGetCurrentLocationSuccess: (Pair<Double, Double>) -> Unit,
    onGetCurrentLocationFailed: (Exception?) -> Unit,
    priority: Boolean = true
) {
    // Determine the accuracy priority based on the 'priority' parameter
    val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
    else Priority.PRIORITY_BALANCED_POWER_ACCURACY

    // Check if location permissions are granted
    if (areLocationPermissionsGranted(ctx)) {
        // Retrieve the current location asynchronously
        fusedLocationProviderClient.getCurrentLocation(
            accuracy, CancellationTokenSource().token,
        ).addOnSuccessListener { location ->
            android.util.Log.d("current", "taken success ${location == null}")
            if (location == null) {
                onGetCurrentLocationFailed(null)
            }
            location?.let {
                // If location is not null, invoke the success callback with latitude and longitude
                onGetCurrentLocationSuccess(Pair(it.latitude, it.longitude))
            }
        }.addOnFailureListener { exception ->
            android.util.Log.d("current", "taken failed ${exception.message}")
            // If an error occurs, invoke the failure callback with the exception
            onGetCurrentLocationFailed(exception)
        }
    }
}

/**
 * Checks if location permissions are granted.
 *
 * @return true if both ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permissions are granted; false otherwise.
 */
private fun areLocationPermissionsGranted(ctx: Context): Boolean {
    return (ActivityCompat.checkSelfPermission(
        ctx, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                ctx, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
}

@Preview(showBackground = true)
@Composable
fun AddStorePreview() {
    MVVMTheme {
        AddStoreCompose(homeViewModel = hiltViewModel(), navController = rememberNavController(), addStore = {}) {

        }
    }
}