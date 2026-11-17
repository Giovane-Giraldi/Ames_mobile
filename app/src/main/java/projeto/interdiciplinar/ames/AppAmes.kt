package projeto.interdiciplinar.ames

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import projeto.interdiciplinar.ames.data.Sessao
import projeto.interdiciplinar.ames.view.ActivitiesScreen
import projeto.interdiciplinar.ames.view.CadastroScreen
import projeto.interdiciplinar.ames.view.LoginScreen
import projeto.interdiciplinar.ames.viewmodel.AtividadeViewModel
import projeto.interdiciplinar.ames.viewmodel.AuthViewModel

@Composable
fun AppAmes() {
    val auth: AuthViewModel = viewModel()
    val atividades: AtividadeViewModel = viewModel()

    val token = Sessao.token

    if (token == null) {
        val nav = rememberNavController()

        NavHost(
            navController = nav,
            startDestination = "login"
        ) {
            composable("login") {
                LoginScreen(
                    vm = auth,
                    cadastrar = { nav.navigate("cadastro") }
                )
            }

            composable("cadastro") {
                CadastroScreen(
                    vm = auth,
                    voltar = { nav.popBackStack() }
                )
            }
        }
    } else {
        key(token) {
            LaunchedEffect(token) {
                atividades.limpar()
                atividades.listar()
            }

            DisposableEffect(token) {
                onDispose {
                    atividades.limpar()
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Olá, ${Sessao.usuario?.nome.orEmpty()}!",
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 12.dp)
                    )

                    TextButton(
                        onClick = {
                            atividades.limpar()
                            auth.sair()
                        }
                    ) {
                        Text("Sair")
                    }
                }

                if (atividades.ocupado) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                OutlinedButton(
                    onClick = { atividades.listar() },
                    enabled = !atividades.ocupado,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text("Atualizar atividades")
                }

                Box(modifier = Modifier.weight(1f)) {
                    ActivitiesScreen(vm = atividades)
                }
            }
        }
    }
}