package com.example.pickapic.ui.screens

sealed class Screen(val route: String) {
    object HomeScreen: Screen("homeScreen")
    object PicturesScreen: Screen("picScreen/{topic}")
    object FavouritePicScreen: Screen("favPicScreen")
    object FullPicScreen: Screen("fullPicScreen/{picUrl}")
}