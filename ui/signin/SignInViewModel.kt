package io.trading.bot.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.trading.bot.mapper.ErrorMapper
import io.trading.bot.repo.rh.RobinHoodRepo
import io.trading.bot.usecase.device.GetOrCreateDeviceTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SignInViewModel(
    private val errorMapper: ErrorMapper,
    private val getOrCreateDeviceTokenUseCase: GetOrCreateDeviceTokenUseCase,
    private val robinHoodRepo: RobinHoodRepo
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(State())
    val stateFlow = _stateFlow.asStateFlow()

    fun onEmailChanged(email: String) {
        _stateFlow.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _stateFlow.update { it.copy(password = password) }
    }

    fun onOtpChanged(otp: String) {
        _stateFlow.update { it.copy(otp = otp) }
    }

    fun signIn() {
        _stateFlow.update { it.copy(loading = true) }
        viewModelScope.launch {
            robinHoodRepo.signIn(
                stateFlow.value.email,
                stateFlow.value.password,
                getOrCreateDeviceTokenUseCase()
            ).onSuccess { result ->
                _stateFlow.update {
                    it.copy(
                        loading = false,
                        status = result.status,
                        showOtp = result.challengeId.isNotBlank()
                    )
                }
            }.onFailure { throwable ->
                _stateFlow.update { it.copy(loading = false, error = errorMapper(throwable)) }
            }
        }
    }

    fun onOtpSubmit() {
        TODO("Not yet implemented")
    }

    data class State(
        val loading: Boolean = false,
        val error: String = "",
        val status: String = "",
        val showOtp: Boolean = false,
        val email: String = "",
        val otp: String = "",
        val password: String = ""
    )
}