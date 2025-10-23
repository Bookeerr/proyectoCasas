package com.example.rentfage.ui.screen

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.rentfage.ui.viewmodel.AuthViewModel
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
    val state by vm.uiState.collectAsState()

    // --- Toda la lógica de la cámara ahora vive en la función "inteligente" ---
    val context = LocalContext.current
    var photoUriString by rememberSaveable { mutableStateOf<String?>(null) }
    var pendingCaptureUri by remember { mutableStateOf<Uri?>(null) }
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUriString = pendingCaptureUri?.toString()
            Toast.makeText(context, "Foto tomada correctamente", Toast.LENGTH_SHORT).show()
        } else {
            pendingCaptureUri = null
            Toast.makeText(context, "No se tomó ninguna foto", Toast.LENGTH_SHORT).show()
        }
    }
    var showDialog by remember { mutableStateOf(false) }

    // --- Acciones que se pasarán a la UI "tonta" ---
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

    // Se le pasan todos los datos y acciones a la UI "tonta"
    PerfilScreen(
        name = state.name,
        email = state.email,
        phone = state.phone,
        initials = state.initials,
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
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier.size(90.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = initials, style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = name, style = MaterialTheme.typography.headlineSmall)
                    }
                    Divider(modifier = Modifier.padding(horizontal = 20.dp))
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Email, contentDescription = "Email", tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.size(16.dp))
                            Text(text = email, style = MaterialTheme.typography.bodyLarge)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, contentDescription = "Teléfono", tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.size(16.dp))
                            Text(text = phone, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
            item {
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Captura de foto de perfil",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(12.dp))
                        if (photoUriString.isNullOrEmpty()) {
                            Text("No se ha tomado foto", style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(12.dp))
                        } else {
                            AsyncImage(
                                model = Uri.parse(photoUriString),
                                contentDescription = "Foto Tomada",
                                modifier = Modifier.fillMaxWidth().height(150.dp),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                        Button(onClick = onTakePicture) {
                            Text(if (photoUriString.isNullOrEmpty()) "Abrir Cámara" else "Volver a tomar")
                        }
                        if (!photoUriString.isNullOrEmpty()) {
                            Spacer(Modifier.height(12.dp))
                            OutlinedButton(onClick = { onShowDialogChange(true) }) {
                                Text("Eliminar Foto")
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onLogout) {
            Text("Cerrar Sesión")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onShowDialogChange(false) },
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
