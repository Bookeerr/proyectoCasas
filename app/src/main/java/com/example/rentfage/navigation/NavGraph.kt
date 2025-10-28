package com.example.rentfage.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.rentfage.ui.components.AppDrawer
import com.example.rentfage.ui.components.AppTopBar
import com.example.rentfage.ui.components.defaultDrawerItems
import com.example.rentfage.ui.screen.DetalleCasaScreen
import com.example.rentfage.ui.screen.FavoritosScreenVm
import com.example.rentfage.ui.screen.HomeScreenVm
import com.example.rentfage.ui.screen.LoginScreenVm
import com.example.rentfage.ui.screen.NosotrosScreen
import com.example.rentfage.ui.screen.PerfilScreenVm
import com.example.rentfage.ui.screen.RegisterScreenVm
import com.example.rentfage.ui.viewmodel.CasasViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavGraph(navController: NavHostController) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val casasViewModel: CasasViewModel = viewModel()

    val showTopBar = currentRoute != "login" && currentRoute != "register"

    val goHome: () -> Unit = { navController.navigate("home") }
    val goLogin: () -> Unit = { navController.navigate("login") }
    val goRegister: () -> Unit = { navController.navigate("register") }
    val goPerfil: () -> Unit = { navController.navigate("perfil") }
    val goFavoritos: () -> Unit = { navController.navigate("favoritos") }
    // Novedad: Creamos la acción para navegar a la nueva pantalla.
    val goNosotros: () -> Unit = { navController.navigate("nosotros") }
    val onHouseClick: (Int) -> Unit = { casaId ->
        navController.navigate("detalle_casa/$casaId")
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showTopBar,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                items = defaultDrawerItems(
                    onHome = { scope.launch { drawerState.close() }; goHome() },
                    onLogin = { scope.launch { drawerState.close() }; goLogin() },
                    onRegister = { scope.launch { drawerState.close() }; goRegister() },
                    onPerfil = { scope.launch { drawerState.close() }; goPerfil() },
                    onFavoritos = { scope.launch { drawerState.close() }; goFavoritos() },
                    onNosotros = { scope.launch { drawerState.close() }; goNosotros() } // Novedad: Pasamos la nueva acción.
                )
            )
        }
    ) {
        Scaffold(
            topBar = {
                if (showTopBar) {
                    AppTopBar(onOpenDrawer = { scope.launch { drawerState.open() } })
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "login",
                modifier = Modifier.padding(innerPadding)
            ) {

                composable("home") {
                    HomeScreenVm(
                        vm = casasViewModel,
                        onGoLogin = goLogin,
                        onGoRegister = goRegister,
                        onHouseClick = onHouseClick
                    )
                }
                composable("login") {
                    LoginScreenVm(
                        onLoginOkNavigateHome = goHome,
                        onGoRegister = goRegister
                    )
                }
                composable("register") {
                    RegisterScreenVm(
                        onRegisteredNavigateLogin = goLogin,
                        onGoLogin = goLogin
                    )
                }
                composable("perfil") {
                    PerfilScreenVm(onLogout = goLogin)
                }
                composable("favoritos") {
                    FavoritosScreenVm(
                        vm = casasViewModel,
                        onHouseClick = onHouseClick
                    )
                }
                composable(
                    route = "detalle_casa/{casaId}",
                    arguments = listOf(navArgument("casaId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val casaId = backStackEntry.arguments?.getInt("casaId") ?: 0
                    DetalleCasaScreen(casaId = casaId, onGoHome = goHome)
                }

                // Novedad: Añadimos el nuevo destino al NavHost.
                composable("nosotros") {
                    NosotrosScreen()
                }
            }
        }
    }
}
