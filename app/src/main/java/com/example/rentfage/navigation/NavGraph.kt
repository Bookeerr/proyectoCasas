package com.example.rentfage.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.rentfage.data.local.storage.UserPreferences
import com.example.rentfage.ui.components.AppDrawer
import com.example.rentfage.ui.components.AppTopBar
import com.example.rentfage.ui.components.defaultDrawerItems
import com.example.rentfage.ui.screen.AddEditPropertyScreen
import com.example.rentfage.ui.screen.AdminDashboardScreen
import com.example.rentfage.ui.screen.AdminPropertyListScreen
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

    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val userRole by userPreferences.userRole.collectAsState(initial = null)

    val showTopBar = currentRoute != "login" && currentRoute != "register"

    val goHome: () -> Unit = { navController.navigate("home") }
    val goLogin: () -> Unit = { navController.navigate("login") }
    val goRegister: () -> Unit = { navController.navigate("register") }
    val goPerfil: () -> Unit = { navController.navigate("perfil") }
    val goFavoritos: () -> Unit = { navController.navigate("favoritos") }
    val goNosotros: () -> Unit = { navController.navigate("nosotros") }
    val goAdminDashboard: () -> Unit = { navController.navigate("admin_dashboard") }
    val goAdminPropertyList: () -> Unit = { navController.navigate("admin_property_list") }
    val goAddEditProperty: (Int?) -> Unit = { casaId ->
        if (casaId != null) {
            navController.navigate("add_edit_property/$casaId")
        } else {
            navController.navigate("add_edit_property/-1") // Usamos -1 para indicar "añadir nuevo"
        }
    }
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
                    onPerfil = { scope.launch { drawerState.close() }; goPerfil() },
                    onFavoritos = { scope.launch { drawerState.close() }; goFavoritos() },
                    onNosotros = { scope.launch { drawerState.close() }; goNosotros() },
                    onAdmin = { scope.launch { drawerState.close() }; goAdminDashboard() },
                    userRole = userRole
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

                composable("home") { HomeScreenVm(vm = casasViewModel, onHouseClick = onHouseClick) }
                composable("login") { LoginScreenVm(onLoginOkNavigateHome = goHome, onGoRegister = goRegister) }
                composable("register") { RegisterScreenVm(onRegisteredNavigateLogin = goLogin, onGoLogin = goLogin) }
                composable("perfil") { PerfilScreenVm(onLogout = goLogin) }
                composable("favoritos") { FavoritosScreenVm(vm = casasViewModel, onHouseClick = onHouseClick) }
                composable(
                    route = "detalle_casa/{casaId}",
                    arguments = listOf(navArgument("casaId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val casaId = backStackEntry.arguments?.getInt("casaId") ?: 0
                    // Novedad: Se pasa el ViewModel a la pantalla de detalle.
                    DetalleCasaScreen(casaId = casaId, onGoHome = goHome, casasViewModel = casasViewModel)
                }
                composable("nosotros") { NosotrosScreen() }
                composable("admin_dashboard") {
                    AdminDashboardScreen(casasViewModel = casasViewModel, onGoToPropertyList = goAdminPropertyList)
                }
                composable("admin_property_list") {
                    AdminPropertyListScreen(
                        casasViewModel = casasViewModel, 
                        onAddProperty = { goAddEditProperty(null) },
                        onEditProperty = { casaId -> goAddEditProperty(casaId) }
                    )
                }
                composable(
                    route = "add_edit_property/{casaId}",
                    arguments = listOf(navArgument("casaId") { type = NavType.IntType; defaultValue = -1 })
                ) { backStackEntry ->
                    val casaId = backStackEntry.arguments?.getInt("casaId")
                    AddEditPropertyScreen(
                        casasViewModel = casasViewModel,
                        casaId = if (casaId == -1) null else casaId,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
