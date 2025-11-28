package com.example.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        val isValid = isValidEmail(email)
        _uiState.value = _uiState.value.copy(
            email = email,
            isEmailValid = isValid,
            isLoginEnabled = isValid && _uiState.value.password.isNotEmpty()
        )
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            isLoginEnabled = _uiState.value.isEmailValid && password.isNotEmpty()
        )
    }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (!isValidEmail(_uiState.value.email) || _uiState.value.password.isEmpty()) {
                return@launch
            }
            _uiState.value = _uiState.value.copy(isLoading = true)
            // Имитация сетевого запроса
            kotlinx.coroutines.delay(1000)
            _uiState.value = _uiState.value.copy(isLoading = false)
            onSuccess()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.]+@[A-Za-z0-9]+\\.[A-Za-z]{2,}\$"
        return email.matches(emailRegex.toRegex())
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val showEmailError: Boolean = false,
    val isEmailValid: Boolean = false,
    val isLoginEnabled: Boolean = false,
    val isLoading: Boolean = false
)