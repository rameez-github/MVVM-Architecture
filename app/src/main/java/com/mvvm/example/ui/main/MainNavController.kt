package com.mvvm.example.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mvvm.example.activities.MainActivity
import com.mvvm.example.data.Constants



enum class HomeNavEnum( val title: String) {
    Search(title = Constants.NAV_SEARCH),
    AddStore (title = Constants.NAV_ADD_STORE),
    StoreDetails (title = Constants.NAV_STORE_DETAILS),
    EditStore (title = Constants.NAV_EDIT_STORE),
    MapCompose (title = Constants.NAV_MAP_COMPOSE)
}

@Composable
fun HomeNavController (
    mainActivity: MainActivity,
    homeViewModel: HomeViewModel = hiltViewModel<HomeViewModel>(),
    navController: NavHostController = rememberNavController()
) {

    BackHandler {
        // your action
        if (!navController.navigateUp()) {
            /*Toast.makeText(LocalContext, "")*/
            mainActivity.finish()
        }
    }

    NavHost(navController = navController, startDestination = HomeNavEnum.Search.name ) {

        composable(route = HomeNavEnum.Search.name) {
            SearchCompose(
                homeViewModel = homeViewModel,
                //selectedBottomNav = selectedBottomNav,
                //mutableList = mutableList,
                navController = navController,
                onNavigateToStoreDetail = {
                    homeViewModel.selectedStore.value = it
                    navController.navigate(HomeNavEnum.StoreDetails.name)
                }
            )
        }

        composable(route = HomeNavEnum.AddStore.name) { backStackEntry ->

            AddStoreCompose(
                homeViewModel = homeViewModel,
                navController = navController,
                addStore = { storeData->
                    homeViewModel.addStore(storeData)
                    navController.navigateUp()
                },
                updateStore = {

                }
            )
        }

        composable(route = HomeNavEnum.EditStore.name) { backStackEntry ->

            AddStoreCompose(
                homeViewModel = homeViewModel,
                navController = navController,
                addStore = { storeData->
                    homeViewModel.addStore(storeData)
                    navController.navigateUp()
                },
                updateStore = { storeData->
                    homeViewModel.selectedStore.value = storeData
                    homeViewModel.updateStore(storeData)
                    navController.navigateUp()
                }
            )
        }

        composable(route = HomeNavEnum.StoreDetails.name) {

            StoreDetailsCompose(
                navController = navController,
                storeData = homeViewModel.selectedStore.value!!
            )
        }

        composable(route = HomeNavEnum.MapCompose.name) {
            MapCompose(
                navController = navController
            )
        }
    }

}