package com.example.rentfage.ui.screen

// Asegúrate de tener esta importación para tu clase de preferencias
import com.example.rentfage.data.local.storage.UserPreferences

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rentfage.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreenVm(
    onLoginOkNavigateHome: () -> Unit,
    onGoRegister: () -> Unit
) {

    // --- INICIO DE LA MODIFICACIÓN ---

    // 1. Obtenemos el contexto y creamos la instancia de UserPreferences
    val context = LocalContext.current
    val userPrefrs = remember { UserPreferences(context) }

    val vm: AuthViewModel = viewModel()
    val state by vm.login.collectAsStateWithLifecycle()

    // 2. Reemplazamos el 'if' por 'LaunchedEffect' para poder usar funciones suspend
    LaunchedEffect(state.success) {
        if (state.success) {
            // 3. Guardamos el estado de la sesión en DataStore
            userPrefrs.setLoggedIn(true)

            // El resto de la lógica que ya tenías
            vm.clearLoginResult()
            onLoginOkNavigateHome()
        }
    }

    // --- FIN DE LA MODIFICACIÓN ---

    LoginScreen(
        email = state.email,
        pass = state.pass,
        emailError = state.emailError,
        passError = state.passError,
        canSubmit = state.canSubmit,
        isSubmitting = state.isSubmitting,
        errorMsg = state.errorMsg,
        onEmailChange = vm::onLoginEmailChange,
        onPassChange = vm::onLoginPassChange,
        onSubmit = vm::submitLogin,
        onGoRegister = onGoRegister
    )
}

@Composable
private fun LoginScreen(
    email: String,
    pass: String,
    emailError: String?,
    passError: String?,
    canSubmit: Boolean,
    isSubmitting: Boolean,
    errorMsg: String?,
    onEmailChange: (String) -> Unit,
    onPassChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onGoRegister: () -> Unit
) {
    // Se cambia el color de fondo a blanco.
    val bg = Color.White
    var showPass by remember { mutableStateOf(false) }

    val buttonAlpha by animateFloatAsState(
        targetValue = if (isSubmitting) 0.6f else 1f,
        label = "alphaLoginButton"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(12.dp))

            Text(
                text = "Bienvenido a Rentfage la mejor app de ventas de casas",
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Email") },
                singleLine = true,
                isError = emailError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth()
            )
            AnimatedVisibility(
                visible = emailError != null,
                enter = slideInVertically { it },
                exit = fadeOut()
            ) {
                Text(emailError ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = pass,
                onValueChange = onPassChange,
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPass = !showPass }) {
                        Icon(
                            imageVector = if (showPass) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (showPass) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                isError = passError != null,
                modifier = Modifier.fillMaxWidth()
            )
            AnimatedVisibility(
                visible = passError != null,
                enter = slideInVertically { it },
                exit = fadeOut()
            ) {
                Text(passError ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onSubmit,
                enabled = canSubmit && !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(buttonAlpha)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Validando...")
                } else {
                    Text("Entrar")
                }
            }

            if (errorMsg != null) {
                Spacer(Modifier.height(8.dp))
                Text(errorMsg, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(onClick = onGoRegister, modifier = Modifier.fillMaxWidth()) {
                Text("Crear cuenta")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        email = "",
        pass = "",
        emailError = null,
        passError = null,
        canSubmit = false,
        isSubmitting = false,
        errorMsg = null,
        onEmailChange = {},
        onPassChange = {},
        onSubmit = {},
        onGoRegister = {}
    )
}