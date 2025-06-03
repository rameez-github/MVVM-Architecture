package com.mvvm.example.ui.main

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.LocationServices
import com.mvvm.example.R
import com.mvvm.example.component.RequestLocationPermission
import com.mvvm.example.data.room.model.StoreData
import com.mvvm.example.ui.cards.StoreCard
import com.mvvm.example.ui.theme.MVVMTheme
import com.mvvm.example.ui.theme.fonts
import com.mvvm.example.utilities.scaledSp


@Composable
fun SearchCompose(
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    onNavigateToStoreDetail: (storeData: StoreData) -> Unit
) {

    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    //val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()




    val ctx = LocalContext.current

    val locationSettingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Check the result code
        if (result.resultCode == Activity.RESULT_OK) {
            // Location settings were enabled
            Log.d("current", "taken enabled true")

        } else {
            // Location settings were not enabled
            Log.d("current", "taken enabled false")

        }
    }
    RequestLocationPermission ({
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ctx)

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

    Scaffold(
        topBar = {
            EmbeddedSearchBar(
                onQueryChange = { s->
                    homeViewModel.updateSearchQuery(query = s)
                },
                isSearchActive = isSearchActive,
                onActiveChanged = {
                    isSearchActive = it
                }
            ) {

                StoreResults(
                    modifier = Modifier,
                    homeViewModel = homeViewModel,
                    onNavigateToStoreDetail = onNavigateToStoreDetail
                )
            }
        },
        floatingActionButton = {
            IconButton(
                modifier = Modifier.size(75.dp),
                onClick = {
                    homeViewModel.selectedStore.value = null
                    navController.navigate(route = HomeNavEnum.AddStore.name)
                },
            ) {
                Icon(
                    modifier = Modifier.size(75.dp),
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = stringResource(R.string.search_text_field_clear),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    ) { padding->

        StoreResults(
            modifier = Modifier.padding(padding),
            homeViewModel = homeViewModel,
            onNavigateToStoreDetail = onNavigateToStoreDetail
        )
    }
}

@Composable
private fun StoreResults (
    modifier: Modifier,
    homeViewModel: HomeViewModel,
    onNavigateToStoreDetail: (storeData: StoreData) -> Unit
) {
    val state = homeViewModel.searchResultsFlow.collectAsState(initial = emptyList())

    val storeList = state.value
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(storeList) {
            StoreCard(storeData = it, label = "") { storeData->
                onNavigateToStoreDetail(storeData)
            }
        }
    }
}

/*
https://www.lordcodes.com/articles/compose-embed-searchbar-topappbar/#building-a-search-bar
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EmbeddedSearchBar(
    onQueryChange: (String) -> Unit,
    isSearchActive: Boolean,
    onActiveChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: ((String) -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val activeChanged: (Boolean) -> Unit = { active ->
        searchQuery = ""
        onQueryChange("")
        onActiveChanged(active)
    }

    val textStyle = TextStyle(
        fontSize = 18.scaledSp,
        letterSpacing = 0.sp,
        fontFamily = fonts,
        fontWeight = FontWeight.Bold
    )

    ProvideTextStyle(value = textStyle) {

        SearchBar(
            query = searchQuery,
            onQueryChange = { query ->
                searchQuery = query
                onQueryChange(query)
            },
            onSearch = onSearch ?: { activeChanged(false) },
            shape = RoundedCornerShape(8.dp),
            active = isSearchActive,
            onActiveChange = activeChanged,
            modifier = if (isSearchActive) {
                modifier
                    .animateContentSize(spring(stiffness = Spring.StiffnessHigh))
            } else {
                modifier
                    .padding(start = 12.dp, top = 2.dp, end = 12.dp, bottom = 12.dp)
                    .fillMaxWidth()
                    .animateContentSize(spring(stiffness = Spring.StiffnessHigh))
            },
            placeholder = {
                Text(text = "Search", style = /*MaterialTheme.typography.titleLarge*/TextStyle(
                    fontSize = 18.scaledSp,
                    letterSpacing = 0.sp,
                    fontFamily = fonts,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.CCCC2DC)
                ))
            },
            leadingIcon = {
                if (isSearchActive) {
                    IconButton(
                        onClick = { activeChanged(false) },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,

                            contentDescription = stringResource(R.string.navigation_action_back_cd),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.background,
                    )
                }
            },
            trailingIcon = if (isSearchActive && searchQuery.isNotEmpty()) {
                {
                    IconButton(
                        onClick = {
                            activeChanged(false)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(R.string.search_text_field_clear),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            } else {
                null
            },
            colors = SearchBarDefaults.colors(
                containerColor = if (isSearchActive) {
                    MaterialTheme.colorScheme.background
                } else {
                    MaterialTheme.colorScheme.primary
                }
            ),
            tonalElevation = 0.dp,
            windowInsets = if (isSearchActive) {
                WindowInsets(0.dp)//SearchBarDefaults.windowInsets
            } else {
                WindowInsets(0.dp)
            }
        ) {
            // Search suggestions or results
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }

}



@Preview(showBackground = true)
@Composable
fun SearchComposePreview() {
    MVVMTheme {
        SearchCompose(
            homeViewModel = hiltViewModel(),
            navController = rememberNavController()) {

        }
    }
}