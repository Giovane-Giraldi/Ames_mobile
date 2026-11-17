package projeto.interdiciplinar.ames.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import projeto.interdiciplinar.ames.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    vm: AuthViewModel,
    cadastrar: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        vm.limparMensagem()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "AMES",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F317B)
        )

        Text(
            text = "Organize seu dia, uma atividade por vez.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(32.dp))

        Text(
            text = "Entrar",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            enabled = !vm.ocupado,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            singleLine = true,
            enabled = !vm.ocupado,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier.fillMaxWidth()
        )

        vm.mensagem?.let { mensagem ->
            Spacer(Modifier.height(12.dp))

            Text(
                text = mensagem,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { vm.entrar(email, senha) },
            enabled = !vm.ocupado,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0F317B)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (vm.ocupado) "Entrando..." else "Entrar")
        }

        if (vm.ocupado) {
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }

        TextButton(
            onClick = cadastrar,
            enabled = !vm.ocupado
        ) {
            Text("Não tenho conta — cadastrar")
        }
    }
}