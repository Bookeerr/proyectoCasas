package com.example.rentfage.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentfage.data.local.storage.UserPreferences
import com.example.rentfage.domain.validation.validateConfirm
import com.example.rentfage.domain.validation.validateEmail
import com.example.rentfage.domain.validation.validateNameLettersOnly
import com.example.rentfage.domain.validation.validatePhoneDigitsOnly
import com.example.rentfage.domain.validation.validateStrongPassword
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// --- ESTADOS DE UI ---
data class LoginUiState(
    val email: String = "",
    val pass: String = "",
    val emailError: String? = null,
    val passError: String? = null,
    val isSubmitting: Boolean = false,
    val canSubmit: Boolean = false,
    val success: Boolean = false,
    val errorMsg: String? = null,
    // Guardaremos el rol del usuario que ha iniciado sesión.
    val loggedInUserRole: String? = null
)

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val pass: String = "",
    val confirm: String = "",
    val nameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
    val passError: String? = null,
    val confirmError: String? = null,
    val isSubmitting: Boolean = false,
    val canSubmit: Boolean = false,
    val success: Boolean = false,
    val errorMsg: String? = null
)

// cada usuario tiene un "rol".
internal data class DemoUser(
    val name: String,
    val email: String,
    val phone: String,
    val pass: String,
    val role: String
)

// ViewModel pueda acceder al contexto para usar UserPreferences.
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    // Creamos una instancia de UserPreferences para usarla aquí dentro.
    private val userPreferences = UserPreferences(application)

    companion object {
        // Añadimos un usuario "Admin" y asignamos roles a todos.
        internal val USERS = mutableListOf(
            DemoUser(name = "Admin", email = "admin@rent.cl", phone = "111222333", pass = "admin123", role = "Admin"),
            DemoUser(name = "Prueba", email = "prueba@duocuc.cl", phone = "123456789", pass = "Prueba123!", role = "User")
        )
        var activeUserEmail: String? = null
    }

    private val _login = MutableStateFlow(LoginUiState())
    val login: StateFlow<LoginUiState> = _login

    private val _register = MutableStateFlow(RegisterUiState())
    val register: StateFlow<RegisterUiState> = _register

    // --- LOGIN ---
    fun onLoginEmailChange(value: String) {
        _login.update { it.copy(email = value, emailError = validateEmail(value)) }
        recomputeLoginCanSubmit()
    }

    fun onLoginPassChange(value: String) {
        _login.update { it.copy(pass = value) }
        recomputeLoginCanSubmit()
    }

    private fun recomputeLoginCanSubmit() {
        val s = _login.value
        val can = s.emailError == null && s.email.isNotBlank() && s.pass.isNotBlank()
        _login.update { it.copy(canSubmit = can) }
    }

    fun submitLogin() {
        val s = _login.value
        if (!s.canSubmit || s.isSubmitting) return
        viewModelScope.launch {
            _login.update { it.copy(isSubmitting = true, errorMsg = null, success = false) }
            delay(500)

            val user = USERS.firstOrNull { it.email.equals(s.email, ignoreCase = true) }
            val ok = user != null && user.pass.equals(s.pass)

            if (ok && user != null) {
                // Si el login es correcto, guardamos su rol y el estado de la sesión.
                activeUserEmail = user.email
                userPreferences.setLoggedIn(true)
                userPreferences.saveUserRole(user.role)
            }

            _login.update {
                it.copy(
                    isSubmitting = false,
                    success = ok,
                    errorMsg = if (!ok) "Credenciales inválidas" else null,
                    loggedInUserRole = if(ok) user?.role else null // Guardamos el rol en el estado.
                )
            }
        }
    }

    fun clearLoginResult() {
        _login.update { it.copy(success = false, errorMsg = null, loggedInUserRole = null) }
    }

    fun logout() {
        viewModelScope.launch {
            // Al cerrar sesión, ahora también limpiamos la memoria de UserPreferences.
            userPreferences.setLoggedIn(false)
            userPreferences.clearUserRole()
            activeUserEmail = null
        }
    }

    // --- REGISTRO ---
    fun onNameChange(value: String) {
        val filtered = value.filter { it.isLetter() || it.isWhitespace() }
        _register.update {
            it.copy(name = filtered, nameError = validateNameLettersOnly(filtered))
        }
        recomputeRegisterCanSubmit()
    }

    fun onRegisterEmailChange(value: String) {
        _register.update { it.copy(email = value, emailError = validateEmail(value)) }
        recomputeRegisterCanSubmit()
    }

    fun onPhoneChange(value: String) {
        val digitsOnly = value.filter { it.isDigit() }
        _register.update {
            it.copy(phone = digitsOnly, phoneError = validatePhoneDigitsOnly(digitsOnly))
        }
        recomputeRegisterCanSubmit()
    }

    fun onRegisterPassChange(value: String) {
        _register.update { it.copy(pass = value, passError = validateStrongPassword(value)) }
        _register.update { it.copy(confirmError = validateConfirm(it.pass, it.confirm)) }
        recomputeRegisterCanSubmit()
    }

    fun onConfirmChange(value: String) {
        _register.update { it.copy(confirm = value, confirmError = validateConfirm(it.pass, value)) }
        recomputeRegisterCanSubmit()
    }

    private fun recomputeRegisterCanSubmit() {
        val s = _register.value
        val noErrors = listOf(s.nameError, s.emailError, s.phoneError, s.passError, s.confirmError).all { it == null }
        val filled = s.name.isNotBlank() && s.email.isNotBlank() && s.phone.isNotBlank() && s.pass.isNotBlank() && s.confirm.isNotBlank()
        _register.update { it.copy(canSubmit = noErrors && filled) }
    }

    fun submitRegister() {
        val s = _register.value
        if (!s.canSubmit || s.isSubmitting) return
        viewModelScope.launch {
            _register.update { it.copy(isSubmitting = true, errorMsg = null, success = false) }
            delay(700)

            val duplicated = USERS.any { it.email.equals(s.email, ignoreCase = true) }

            if (duplicated) {
                _register.update {
                    it.copy(isSubmitting = false, success = false, errorMsg = "El usuario ya existe")
                }
                return@launch
            }

            // cuando se registra un nuevo usuario, se le asigna el rol "User" por defecto.
            USERS.add(
                DemoUser(
                    name = s.name.trim(),
                    email = s.email.trim(),
                    phone = s.phone.trim(),
                    pass = s.pass,
                    role = "User" 
                )
            )

            _register.update {
                it.copy(isSubmitting = false, success = true, errorMsg = null)
            }
        }
    }

    fun clearRegisterResult() {
        _register.update { it.copy(success = false, errorMsg = null) }
    }
}
