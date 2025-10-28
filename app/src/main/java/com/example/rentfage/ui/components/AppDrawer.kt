package com.example.rentfage.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerItem(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun AppDrawer(
    currentRoute: String?,
    items: List<DrawerItem>,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(modifier = modifier) {
        items.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.label) },
                selected = false,
                onClick = item.onClick,
                icon = { Icon(item.icon, contentDescription = item.label) },
                colors = NavigationDrawerItemDefaults.colors()
            )
        }
    }
}

@Composable
fun defaultDrawerItems(
    onHome: () -> Unit,
    onPerfil: () -> Unit,
    onFavoritos: () -> Unit,
    onNosotros: () -> Unit,
    onAdmin: () -> Unit,      // Novedad: Recibimos la acción para el panel.
    userRole: String?        // Novedad: Recibimos el rol del usuario.
): List<DrawerItem> {
    // Creamos la lista base de items que todos los usuarios ven.
    val baseItems = mutableListOf(
        DrawerItem("Home", Icons.Filled.Home, onHome),
        DrawerItem("Mi Perfil", Icons.Filled.AccountCircle, onPerfil),
        DrawerItem("Mis Favoritos", Icons.Filled.Favorite, onFavoritos),
        DrawerItem("Nosotros", Icons.Filled.Info, onNosotros)
    )

    // Si el rol del usuario es "Admin", añadimos el botón secreto a la lista.
    if (userRole == "Admin") {
        baseItems.add(
            DrawerItem("Panel de Administrador", Icons.Filled.AdminPanelSettings, onAdmin)
        )
    }

    // Devolvemos la lista final.
    return baseItems
}
