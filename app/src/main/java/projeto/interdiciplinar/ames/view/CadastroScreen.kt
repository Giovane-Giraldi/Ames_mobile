package projeto.interdiciplinar.ames.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import projeto.interdiciplinar.ames.viewmodel.AuthViewModel

@Composable
fun CadastroScreen(
    vm: AuthViewModel,
    voltar: () -> Unit
) {
    var nome by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmacao by remember { mutableStateOf("") }
    var erroLocal by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        vm.limparMensagem()
    }

    LaunchedEffect(vm.cadastroConcluido) {
        if (vm.cadastroConcluido) {
            vm.consumirCadastro()
            voltar()
        }
    }

    BackHandler(enabled = vm.ocupado) {
        // Aguarda o término do cadastro antes de sair.
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Criar conta",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF0F317B)
        )

        Text("Cadastre-se para organizar suas atividades no AMES.")

        OutlinedTextField(
            value = nome,
            onValueChange = {
                nome = it
                erroLocal = null
            },
            label = { Text("Nome") },
            singleLine = true,
            enabled = !vm.ocupado,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                erroLocal = null
            },
            label = { Text("Email") },
            singleLine = true,
            enabled = !vm.ocupado,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = senha,
            onValueChange = {
                senha = it
                erroLocal = null
            },
            label = { Text("Senha") },
            supportingText = { Text("Pelo menos 8 caracteres") },
            singleLine = true,
            enabled = !vm.ocupado,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = confirmacao,
            onValueChange = {
                confirmacao = it
                erroLocal = null
            },
            label = { Text("Confirmar senha") },
            singleLine = true,
            enabled = !vm.ocupado,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier.fillMaxWidth()
        )

        (erroLocal ?: vm.mensagem)?.let { mensagem ->
            Text(
                text = mensagem,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                vm.limparMensagem()
                erroLocal = null

                if (senha != confirmacao) {
                    erroLocal = "As senhas não coincidem."
                } else {
                    vm.cadastrar(nome, email, senha)
                }
            },
            enabled = !vm.ocupado,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0F317B)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (vm.ocupado) "Cadastrando..." else "Criar conta")
        }

        if (vm.ocupado) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }

        TextButton(
            onClick = voltar,
            enabled = !vm.ocupado,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar para o login")
        }
    }
}