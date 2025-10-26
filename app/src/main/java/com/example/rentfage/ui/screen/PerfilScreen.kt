package com.example.rentfage.ui.screen

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.rentfage.data.local.Casa
import com.example.rentfage.ui.viewmodel.AuthViewModel
import com.example.rentfage.ui.viewmodel.CasasViewModel
import com.example.rentfage.ui.viewmodel.PerfilViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun createTempImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = File(context.cacheDir, "images").apply { if (!exists()) mkdirs() }
    return File(storageDir, "IMG_${timeStamp}.jpg")
}

private fun getImageUriForFile(context: Context, file: File): Uri {
    val authority = "${context.packageName}.fileprovider"
    return FileProvider.getUriForFile(context, authority, file)
}

@Composable
fun PerfilScreenVm(onLogout: () -> Unit) {
    val vm: PerfilViewModel = viewModel()
    val authVm: AuthViewModel = viewModel()
    // Nos conectamos a las casas para poder acceder a la lista.
    val casasVm: CasasViewModel = viewModel()
    val state by vm.uiState.collectAsState()
    // Obtenemos  la lista de casas desde el CasasViewModel.
    val casasState by casasVm.uiState.collectAsState()

    // logica de la cámara
    val context = LocalContext.current
    var photoUriString by rememberSaveable { mutableStateOf<String?>(null) }
    var pendingCaptureUri by remember { mutableStateOf<Uri?>(null) }
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        // si se tomo la foto correctamente
        if (success) {
            photoUriString = pendingCaptureUri?.toString()
            Toast.makeText(context, "Foto tomada correctamente", Toast.LENGTH_SHORT).show()
        } else {
            pendingCaptureUri = null
            Toast.makeText(context, "No se tomo ninguna foto", Toast.LENGTH_SHORT).show()
        }
    }
    var showDialog by remember { mutableStateOf(false) }


    val onTakePicture = {
        val file = createTempImageFile(context)
        val uri = getImageUriForFile(context, file)
        pendingCaptureUri = uri
        takePictureLauncher.launch(uri)
    }

    val onDeletePicture = {
        photoUriString = null
        showDialog = false
        Toast.makeText(context, "Foto eliminada", Toast.LENGTH_SHORT).show()
    }

    // Le pasamos la lista de casas a la pantalla de abajo.
    PerfilScreen(
        name = state.name,
        email = state.email,
        phone = state.phone,
        initials = state.initials,
        casas = casasState.casas, // Pasamos la lista de casas
        photoUriString = photoUriString,
        showDialog = showDialog,
        onShowDialogChange = { showDialog = it },
        onTakePicture = onTakePicture,
        onDeletePicture = onDeletePicture,
        onLogout = {
            authVm.logout()
            onLogout()
        }
    )
}

@Composable
private fun PerfilScreen(
    name: String, email: String, phone: String, initials: String,
    casas: List<Casa>, // Recibimos la lista de casas
    photoUriString: String?, showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit, onTakePicture: () -> Unit, onDeletePicture: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            item {
                // HorizontalPager es un componente que permite deslizar elementos horizontalmente.
                val pagerState = rememberPagerState(pageCount = { casas.size }) // Creamos un estado para el pager, diciéndole cuántas páginas (casas) tiene.
                HorizontalPager(
                    state = pagerState, // Le pasamos el estado que acabamos de crear.
                    modifier = Modifier
                        .fillMaxWidth() // Ocupa todo el ancho.
                        .height(250.dp) // Le damos altura
                ) { page -> // Este bloque de código se ejecutará una vez por por cada casa.

                    // Obtenemos la casa que corresponde a la página actual.
                    val casa = casas[page]
                    // Usamos el  Image para mostrar la foto de esa casa.
                    Image(
                        painter = painterResource(id = casa.imageResId), // Le decimos qué imagen dibujar.
                        contentDescription = "Imagen de la casa ${casa.address}", // Descripcion
                        contentScale = ContentScale.Crop, // Hacemos que la imagen se recorte para llenar el espacio.
                        modifier = Modifier.fillMaxSize() // La imagen ocupa todo el tamaño del Pager.
                    )
                }
            }

            item {
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        //parte de pefil donde se ven las iniciales
                        Box(
                            modifier = Modifier.size(90.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            //responsable de poner las iniciales
                            Text(text = initials, style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        Spacer(modifier = Modifier.height(16.dp)) // sirve para dar espacio
                        //muestra nombre del usuario
                        Text(text = name, style = MaterialTheme.typography.headlineSmall)
                    }
                    Divider(modifier = Modifier.padding(horizontal = 20.dp)) //linea que separa
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) { //añade marguen 
                        // permite poner cosas al lado de otro (el icono del correo) y lo demas es para que este alineado
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            //dibuja el icono del correo
                            Icon(Icons.Default.Email, contentDescription = "Email", tint = MaterialTheme.colorScheme.primary)
                            //espacio entre el icono y el texto
                            Spacer(modifier = Modifier.size(16.dp))
                           //muestra el texto del correo del usuario
                            Text(text = email, style = MaterialTheme.typography.bodyLarge)
                        }
                        // espacio entre el correo y el telefono
                        Spacer(modifier = Modifier.height(12.dp))
                        // permite poner cosas al lado de otro (el icono del telefono)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                           //dibuja el icono del telefono
                            Icon(Icons.Default.Phone, contentDescription = "Teléfono", tint = MaterialTheme.colorScheme.primary)
                           //espacio entre el icono y el texto
                            Spacer(modifier = Modifier.size(16.dp))
                           //muestra el texto del telefono del usuario
                            Text(text = phone, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
            
            item {
                // parte de la camara
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    // contenido de la camara
                    Column(
                        modifier = Modifier.padding(16.dp), //margen
                        horizontalAlignment = Alignment.CenterHorizontally //alineacion
                    ) {
                        // titulo de la camara
                        Text(
                            text = "Captura de foto de perfil",
                            style = MaterialTheme.typography.titleMedium, //estilo del texto
                            textAlign = TextAlign.Center //alineacion del texto
                        )
                        Spacer(Modifier.height(12.dp)) //espacio entre el titulo y la foto
                        //muestra la foto si hay
                        if (photoUriString.isNullOrEmpty()) {
                            Text("No se ha tomado foto", style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(12.dp))  //añade espacio
                        } else {
                            AsyncImage( //para cargar la imagen
                                model = Uri.parse(photoUriString), //uri de la imagen, que es la imagen q debe mostrar
                                contentDescription = "Foto Tomada", //descripcion de la imagen
                                modifier = Modifier.fillMaxWidth().height(150.dp), //tamaño de la imagen
                                contentScale = ContentScale.Crop //como se debe mostrar la imagen
                            )
                            Spacer(Modifier.height(12.dp)) //añade espacio
                        }
                        Button(onClick = onTakePicture) { //boton para tomar la foto
                            //muestra el texto del boton
                            Text(if (photoUriString.isNullOrEmpty()) "Abrir Cámara" else "Volver a tomar")
                        }
                        //
                        if (!photoUriString.isNullOrEmpty()) { //si hay foto se muestra el boton de eliminar
                            Spacer(Modifier.height(12.dp)) //añade espacio
                            //boton para eliminar la foto
                            OutlinedButton(onClick = { onShowDialogChange(true) }) {
                                Text("Eliminar Foto")
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp)) //añade espacio
        Button(onClick = onLogout) { //boton para cerrar sesion
            Text("Cerrar Sesión")
        }
    }

    // por si quiere eliminar la foto para eliminar la foto
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onShowDialogChange(false) }, //
            title = { Text("Confirmación") },
            text = { Text("¿Desea eliminar la foto?") },
            confirmButton = {
                TextButton(onClick = onDeletePicture) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { onShowDialogChange(false) }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
