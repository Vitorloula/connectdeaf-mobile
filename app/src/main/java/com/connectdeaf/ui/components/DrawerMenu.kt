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
    gesturesEnabled: Boolean = true,
    content: @Composable () -> Unit,
) {


    ModalNavigationDrawer(
        drawerState = drawerViewModel.drawerStateNotifications,
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            NotificationDrawerContent(
                navController = navController,
                drawerState = drawerViewModel.drawerStateNotifications,
                scope = scope,
                notificationViewModel = drawerViewModel.notificationViewModel
            )
        },
        content = {
            ModalNavigationDrawer(
                drawerState = drawerViewModel.drawerStateMenu,
                gesturesEnabled = gesturesEnabled,
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