package com.example.rentfage.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

// Estructura de un ítem de menú lateral (como en el ejemplo)
data class DrawerItem(
    val label: String, 
    val icon: ImageVector,
    val onClick: () -> Unit
)

// Componente Drawer (ahora sí, como en el ejemplo)
@Composable
fun AppDrawer(
    currentRoute: String?, // Ruta actual (para marcar seleccionado si quieres)
    items: List<DrawerItem>,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(modifier = modifier) {
        // Recorremos las opciones y pintamos ítems, sin añadir elementos extra
        items.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.label) },
                selected = false, // Lo dejamos simple por ahora
                onClick = item.onClick,
                icon = { Icon(item.icon, contentDescription = item.label) },
                colors = NavigationDrawerItemDefaults.colors()
            )
        }
    }
}

// Helper para construir la lista estándar de ítems (en el mismo archivo)
@Composable
fun defaultDrawerItems(
    onHome: () -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit
): List<DrawerItem> = listOf(
    DrawerItem("Home", Icons.Filled.Home, onHome),
    DrawerItem("Login", Icons.Filled.AccountCircle, onLogin), // icono del ejemplo
    DrawerItem("Registro", Icons.Filled.Person, onRegister) // icono del ejemplo
)
