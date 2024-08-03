package io.trading.bot.ui.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.trading.bot.ui.common.Screen
import io.trading.bot.ui.common.ScreenProvider
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
@Named("sign-in")
data class SignInScreenProvider(
    override val builder: NavGraphBuilder.() -> Unit = { composable<SignInScreen> { SignInScreenContent() } }
) : ScreenProvider {
    @Serializable
    data object SignInScreen : Screen
}

@Composable
fun SignInScreenContent(viewModel: SignInViewModel = koinViewModel()) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            TextField(
                value = state.email,
                onValueChange = viewModel::onEmailChanged,
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            TextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.password,
                onValueChange = viewModel::onPasswordChanged,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.None
                ),
                label = { Text("Password") }
            )
            Button(viewModel::signIn, modifier = Modifier.padding(top = 16.dp)) {
                Text("Sign In")
            }

            TextField(
                value = state.otp,
                onValueChange = viewModel::onOtpChanged,
                modifier = Modifier.padding(top = 16.dp),
                label = { Text("OTP") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.None
                ),
            )
            Button(viewModel::onOtpSubmit, modifier = Modifier.padding(top = 16.dp)) {
                Text("Send Otp")
            }

            Button(viewModel::onRefreshToken, modifier = Modifier.padding(top = 16.dp)) {
                Text("Refresh Token")
            }

            Text(state.status, modifier = Modifier.padding(top = 16.dp))

            if (state.loading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
            if (state.error.isNotBlank()) {
                Text(
                    text = state.error,
                    modifier = Modifier
                        .padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
@Preview
fun SingInScreenPreview() {
    SignInScreenContent()
}