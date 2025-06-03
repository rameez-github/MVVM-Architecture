package com.mvvm.example.ui.main

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.mvvm.example.R
import com.mvvm.example.component.GoBackNavigation
import com.mvvm.example.data.room.model.StoreData
import com.mvvm.example.ui.theme.fonts
import com.mvvm.example.utilities.scaledSp
import java.io.File


@Composable
fun StoreDetailsCompose (
    navController: NavHostController,
    storeData: StoreData
){
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
            screenTitle = stringResource(id = R.string.details),
            onBackClicked = {
                navController.navigateUp()
            },
            showEditIcon = true,
            onNavigateToEditScreen = {
                navController.navigate(route = HomeNavEnum.EditStore.name)
            }
        )

        //Row {}
        Column (
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .verticalScroll(state = rememberScrollState())

        ) {

            Text(
                text = "Store Number: ${storeData.store_number}",
                textAlign = TextAlign.Start,
                fontSize = 18.scaledSp,
                fontFamily = fonts,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.black),
                lineHeight = TextUnit.Unspecified,
                modifier = Modifier
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = colorResource(id = R.color.CCCC2DC),
                thickness = 1.dp)

            Text(
                text = "Store Name: ${storeData.store_name}",
                textAlign = TextAlign.Start,
                fontSize = 18.scaledSp,
                fontFamily = fonts,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.black),
                lineHeight = TextUnit.Unspecified,
                modifier = Modifier
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = colorResource(id = R.color.CCCC2DC),
                thickness = 1.dp)

            Text(
                text = "Road: ${storeData.road}",
                textAlign = TextAlign.Start,
                fontSize = 18.scaledSp,
                fontFamily = fonts,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.black),
                lineHeight = TextUnit.Unspecified,
                modifier = Modifier.padding(top = 8.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = colorResource(id = R.color.CCCC2DC),
                thickness = 1.dp)

            Text(
                text = "Address: ${storeData.address}",
                textAlign = TextAlign.Start,
                fontSize = 18.scaledSp,
                fontFamily = fonts,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.black),
                lineHeight = TextUnit.Unspecified,
                modifier = Modifier
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = colorResource(id = R.color.CCCC2DC),
                thickness = 1.dp)

            Text(
                text = "Phone#: ${storeData.phone}",
                textAlign = TextAlign.Start,
                fontSize = 18.scaledSp,
                fontFamily = fonts,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.black),
                lineHeight = TextUnit.Unspecified,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = colorResource(id = R.color.CCCC2DC),
                thickness = 1.dp)

            if (storeData.latitude != 0.0 && storeData.longitude != 0.0) {
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(LatLng(storeData.latitude, storeData.longitude), 16f)
                }
                val uiSettings by remember {
                    mutableStateOf(
                        MapUiSettings(
                            zoomControlsEnabled = false,
                            zoomGesturesEnabled = false,
                            scrollGesturesEnabled = false
                        )
                    )
                }
                val properties by remember {
                    mutableStateOf(MapProperties(mapType = MapType.SATELLITE))
                }

                Text(
                    text = "Google Map",
                    textAlign = TextAlign.Start,
                    fontSize = 18.scaledSp,
                    fontFamily = fonts,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.black),
                    lineHeight = TextUnit.Unspecified,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(top = 16.dp)
                )

                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(top = 0.dp),
                    cameraPositionState = cameraPositionState,
                    properties = properties,
                    uiSettings = uiSettings
                ) {
                    Marker(
                        state = MarkerState(position = LatLng(storeData.latitude, storeData.longitude)),
                        title = "Current Location"
                    )
                }
            }

            Text(
                text = "Image",
                textAlign = TextAlign.Start,
                fontSize = 18.scaledSp,
                fontFamily = fonts,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.black),
                lineHeight = TextUnit.Unspecified,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .padding(top = 16.dp)
            )

            val painter = rememberAsyncImagePainter(model = ImageRequest.Builder(LocalContext.current)
                .data(Uri.fromFile(File(storeData.store_image_path)))
                //.placeholder(R.drawable.ic_add_company_logo)
                .crossfade(false)
                .build())

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                painter = painter/*rememberImagePainter(capturedImageUri)*/,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.padding(bottom = 32.dp))
        }
    }

}