package com.example.rentfage.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rentfage.ui.screen.HomeScreen
import com.example.rentfage.ui.screen.LoginScreenVm
import com.example.rentfage.ui.screen.RegisterScreenVm
import com.example.rentfage.ui.components.AppDrawer
import com.example.rentfage.ui.components.AppTopBar
import com.example.rentfage.ui.components.defaultDrawerItems
import kotlinx.coroutines.launch

@Composable
fun AppNavGraph(navController: NavHostController) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val goHome: () -> Unit    = { navController.navigate(Route.Home.path) }
    val goLogin: () -> Unit   = { navController.navigate(Route.Login.path) }
    val goRegister: () -> Unit = { navController.navigate(Route.Register.path) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { 
            AppDrawer(
                currentRoute = null, 
                items = defaultDrawerItems(
                    onHome = {
                        scope.launch { drawerState.close() }
                        goHome()
                    },
                    onLogin = {
                        scope.launch { drawerState.close() }
                        goLogin()
                    },
                    onRegister = {
                        scope.launch { drawerState.close() }
                        goRegister()
                    }
                )
            )
        }
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    onOpenDrawer = { scope.launch { drawerState.open() } },
                    onHome = goHome,
                    onLogin = goLogin,
                    onRegister = goRegister
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                // Se cambia la pantalla de inicio a Login.
                startDestination = Route.Login.path,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Route.Home.path) { 
                    HomeScreen(
                        onGoLogin = goLogin,
                        onGoRegister = goRegister
                    )
                }
                composable(Route.Login.path) { 
                    LoginScreenVm(
                        onLoginOkNavigateHome = goHome,
                        onGoRegister = goRegister
                    )
                }
                composable(Route.Register.path) { 
                    RegisterScreenVm(
                        onRegisteredNavigateLogin = goLogin,
                        onGoLogin = goLogin
                    )
                }
            }
        }
    }
}