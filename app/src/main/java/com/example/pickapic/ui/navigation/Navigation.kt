package com.example.pickapic.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pickapic.ui.screens.*

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.RegistrationScreen.route) {
            RegistrationScreen(navController = navController)
        }
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.PicturesScreen.route) {
            val topic = it.arguments?.getString("topic")
            PicturesScreen(topic?: "Error", navController = navController)
        }
        composable(route = Screen.FavouritePicScreen.route) {
            FavouritePicScreen()
        }
        composable(route = Screen.FullPicScreen.route) {
            val pictureUrl = it.arguments?.getString("picUrl") ?: "Error"
            FullPicScreen(pictureUrl = pictureUrl)
        }
    }
}