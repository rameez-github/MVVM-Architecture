package com.mvvm.example.ui.cards

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.mvvm.example.R
import com.mvvm.example.data.room.model.StoreData
import com.mvvm.example.ui.theme.PurpleGrey80
import com.mvvm.example.utilities.scaledSp
import com.mvvm.example.ui.theme.fonts


@Composable
fun StoreCard (storeData: StoreData, label: String, onClick: (storeData: StoreData)-> Unit) {
    /*Card(
        elevation = 6.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {

        Text(
            text = "${stringResource(id = R.string.order_id)}: $data\n${stringResource(id = R.string.label)}: $label",
            fontSize = 12.scaledSp,
            fontFamily = fonts,
            letterSpacing = 0.sp,
            fontWeight = FontWeight.Normal,
            color = colorResource(id = R.color.C1B1C1E),
            lineHeight = 14.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 6.dp)
        )
    }*/

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 16.dp)
            .clickable {
                onClick(storeData)
            },
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = PurpleGrey80)
    ) {
        Row {


            val painter = rememberAsyncImagePainter(model = ImageRequest.Builder(LocalContext.current)
                .data(Uri.fromFile(java.io.File(storeData.store_image_path)))
                //.placeholder(R.drawable.ic_add_company_logo)
                .crossfade(false)
                .build())

            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(92.dp),
                painter = painter/*rememberImagePainter(capturedImageUri)*/,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Column (modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)) {

                Text(
                    text = if (storeData.store_number == 0) "" else "(${storeData.store_number}) ${storeData.store_name}",
                    textAlign = TextAlign.Start,
                    fontSize = 18.scaledSp,
                    fontFamily = fonts,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.black),
                    lineHeight = TextUnit.Unspecified,
                    modifier = Modifier
                )

                Text(
                    text = storeData.road,
                    textAlign = TextAlign.Start,
                    fontSize = 18.scaledSp,
                    fontFamily = fonts,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.black),
                    lineHeight = TextUnit.Unspecified,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = storeData.address,
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

                Spacer(modifier = Modifier.padding(bottom = 8.dp))
            }



        }
    }
}