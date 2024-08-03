package io.trading.bot.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.trading.bot.mapper.ErrorMapper
import io.trading.bot.model.AccessToken
import io.trading.bot.model.SignInResult
import io.trading.bot.repo.rh.RobinHoodRepo
import io.trading.bot.usecase.auth.HandleAccessTokenUseCase
import io.trading.bot.usecase.auth.RefreshAccessTokenUseCase
import io.trading.bot.usecase.device.GetOrCreateDeviceTokenUseCase
import io.trading.bot.utils.flatten
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SignInViewModel(
    private val errorMapper: ErrorMapper,
    private val getOrCreateDeviceTokenUseCase: GetOrCreateDeviceTokenUseCase,
    private val robinHoodRepo: RobinHoodRepo,
    private val handleAccessTokenUseCase: HandleAccessTokenUseCase,
    private val refreshAccessTokenUseCase: RefreshAccessTokenUseCase
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
                email = stateFlow.value.email,
                password = stateFlow.value.password,
                deviceToken = getOrCreateDeviceTokenUseCase(),
            ).onSuccess { result ->
                when (result) {
                    is SignInResult.OtpChallenge -> {
                        _stateFlow.update {
                            it.copy(
                                loading = false,
                                status = "${result.details}\n${result.expiresAt}",
                                showOtp = result.challengeId.isNotBlank(),
                                challengeId = result.challengeId
                            )
                        }
                    }

                    is SignInResult.Token -> Result.success(result.accessToken).processToken()
                }
            }.onFailure { throwable ->
                _stateFlow.update { it.copy(loading = false, error = errorMapper(throwable)) }
            }
        }
    }

    fun onOtpSubmit() {
        _stateFlow.update { it.copy(loading = true) }
        viewModelScope.launch {
            robinHoodRepo.challenge(stateFlow.value.challengeId, stateFlow.value.otp)
                .flatten {
                    robinHoodRepo.signInWithOtp(
                        email = stateFlow.value.email,
                        password = stateFlow.value.password,
                        deviceToken = getOrCreateDeviceTokenUseCase(),
                        challengeId = stateFlow.value.challengeId
                    )
                }
                .processToken()
        }
    }

    private suspend fun Result<AccessToken>.processToken() {
        this.map { handleAccessTokenUseCase.invoke(it) }
            .onSuccess {
                _stateFlow.update { it.copy(loading = false, status = "Token was obtained") }
            }.onFailure { throwable ->
                _stateFlow.update { it.copy(loading = false, error = errorMapper(throwable)) }
            }
    }

    fun onRefreshToken() {
        _stateFlow.update { it.copy(loading = true) }
        viewModelScope.launch {
            refreshAccessTokenUseCase()
                .onSuccess {
                    _stateFlow.update { it.copy(loading = false, status = "Token was refreshed") }
                }
                .onFailure { throwable ->
                    _stateFlow.update { it.copy(loading = false, error = errorMapper(throwable)) }
                }
        }
    }

    data class State(
        val loading: Boolean = false,
        val error: String = "",
        val status: String = "",
        val showOtp: Boolean = false,
        val email: String = "",
        val otp: String = "",
        val password: String = "",
        val challengeId: String = ""
    )
}