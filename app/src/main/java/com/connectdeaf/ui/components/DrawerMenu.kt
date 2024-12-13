package com.connectdeaf.ui.components

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope

@Composable
fun DrawerMenu(
    navController: NavHostController,
    scope: CoroutineScope,
    drawerStateMenu: DrawerState,
    drawerStateNotifications: DrawerState,
    content: @Composable () -> Unit
) {


    ModalNavigationDrawer(
        drawerState = drawerStateNotifications,
        gesturesEnabled = true,
        drawerContent = {
            NotificationDrawerContent(
                navController = navController,
                drawerState = drawerStateNotifications,
                scope = scope,
            )
        },
        content = {
            ModalNavigationDrawer(
                drawerState = drawerStateMenu,
                gesturesEnabled = true,
                drawerContent = {
                    MenuDrawerContent(
                        navController = navController,
                        drawerState = drawerStateMenu,
                        scope = scope
                    )
                },
                content = {
                    content()
                }
            )
        }
    )
}