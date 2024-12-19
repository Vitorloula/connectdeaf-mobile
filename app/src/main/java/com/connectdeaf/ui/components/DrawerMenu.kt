package com.connectdeaf.ui.components

import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.connectdeaf.viewmodel.DrawerViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun DrawerMenu(
    navController: NavController,
    scope: CoroutineScope,
    drawerViewModel: DrawerViewModel,
    content: @Composable () -> Unit
) {


    ModalNavigationDrawer(
        drawerState = drawerViewModel.drawerStateNotifications,
        gesturesEnabled = true,
        drawerContent = {
            NotificationDrawerContent(
                navController = navController,
                drawerState = drawerViewModel.drawerStateNotifications,
                scope = scope,
            )
        },
        content = {
            ModalNavigationDrawer(
                drawerState = drawerViewModel.drawerStateMenu,
                gesturesEnabled = true,
                drawerContent = {
                    MenuDrawerContent(
                        navController = navController,
                        drawerState = drawerViewModel.drawerStateMenu,
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