package projeto.interdiciplinar.ames.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import projeto.interdiciplinar.ames.model.Atividade
import projeto.interdiciplinar.ames.viewmodel.AtividadeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val AzulEscuro = Color(0xFF0F317B)
private val Verde = Color(0xFF138A63)
private val Laranja = Color(0xFFE28A16)
private val Vermelho = Color(0xFFC83D4D)

@Composable
fun ActivitiesScreen(
    vm: AtividadeViewModel = viewModel()
) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var descricao by rememberSaveable { mutableStateOf("") }
    var categoria by rememberSaveable { mutableStateOf("Tarefa doméstica") }
    var inicio by rememberSaveable { mutableStateOf("") }
    var fim by rememberSaveable { mutableStateOf("") }
    var endereco by rememberSaveable { mutableStateOf("") }
    var mensagem by rememberSaveable { mutableStateOf("") }

    var excluirId by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(vm.mensagem) {
        mensagem = vm.mensagem?.let {
            if (vm.erro) "Erro: $it" else it
        } ?: ""
    }

    LaunchedEffect(vm.cadastroConcluido) {
        if (vm.cadastroConcluido) {
            titulo = ""
            descricao = ""
            inicio = ""
            fim = ""
            endereco = ""

            vm.consumirCadastro()
        }
    }


    if (excluirId != null) {
        AlertDialog(
            onDismissRequest = { excluirId = null },
            title = { Text("Excluir atividade?") },
            text = { Text("Esta ação não poderá ser desfeita.") },
            confirmButton = {
                TextButton(onClick = {
                    excluirId?.let { vm.excluir(it) }
                    excluirId = null
                }) {
                    Text("Excluir", color = Vermelho)
                }
            },
            dismissButton = {
                TextButton(onClick = { excluirId = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        item {
            Surface(
                color = AzulEscuro,
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(Modifier.fillMaxWidth().padding(20.dp)) {
                    Text(
                        "AMES • Organização diária",
                        color = Color.Cyan
                    )
                    Text(
                        "Minhas atividades",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "Cadastre, acompanhe e conclua suas atividades.",
                        color = Color.White
                    )
                }
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Resumo("Total", vm.total, AzulEscuro, Modifier.weight(1f))
                Resumo("Pendentes", vm.totalPendentes, Laranja, Modifier.weight(1f))
                Resumo("Concluídas", vm.totalConcluidas, Verde, Modifier.weight(1f))
            }
        }

        // FORMULÁRIO
        item {
            Card {
                Column(
                    Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "Cadastrar atividade",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = titulo,
                        onValueChange = { titulo = it.take(150) },
                        label = { Text("Título *") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = descricao,
                        onValueChange = { descricao = it.take(500) },
                        label = { Text("Descrição") },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )

                    SeletorCategoria(
                        valor = categoria,
                        opcoes = vm.categorias,
                        onSelecionar = { categoria = it },
                        titulo = "Tipo de atividade"
                    )

                    OutlinedTextField(
                        value = inicio,
                        onValueChange = { inicio = it },
                        label = { Text("Início: dd/MM/aaaa HH:mm") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = fim,
                        onValueChange = { fim = it },
                        label = { Text("Término: dd/MM/aaaa HH:mm") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = endereco,
                        onValueChange = { endereco = it.take(255) },
                        label = { Text("Endereço") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (mensagem.isNotEmpty()) {
                        Text(
                            mensagem,
                            color = if (mensagem.startsWith("Erro")) {
                                Vermelho
                            } else {
                                Verde
                            }
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = {
                            titulo = ""
                            descricao = ""
                            categoria = vm.categorias.first()
                            inicio = ""
                            fim = ""
                            endereco = ""
                            mensagem = ""
                        }) {
                            Text("Limpar")
                        }

                        Button(
                            onClick = {
                                vm.cadastrar(
                                    titulo, descricao, categoria,
                                    inicio, fim, endereco
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AzulEscuro
                            )
                        ) {
                            Text("Cadastrar")
                        }
                    }
                }
            }
        }

        // FILTROS
        item {
            Text(
                "Acompanhar atividades",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = vm.busca,
                onValueChange = vm::atualizarBusca,
                label = { Text("Buscar atividade") },
                modifier = Modifier.fillMaxWidth()
            )

            SeletorCategoria(
                valor = vm.categoriaFiltro,
                opcoes = listOf("Todos os tipos") + vm.categorias,
                onSelecionar = vm::atualizarFiltro,
                titulo = "Filtrar categoria"
            )
        }

        item {
            Text(
                "Pendentes (${vm.totalPendentes})",
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (vm.pendentes.isEmpty()) {
            item { Text("Nenhuma atividade pendente.") }
        }

        items(vm.pendentes, key = { it.id }) { atividade ->
            CartaoAtividade(
                atividade = atividade,
                onStatus = {
                    vm.concluir(atividade.id)
                },
                onExcluir = {
                    excluirId = atividade.id
                }
            )
        }

        item {
            Text(
                "Concluídas (${vm.totalConcluidas})",
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (vm.concluidas.isEmpty()) {
            item { Text("Nenhuma atividade concluída.") }
        }

        items(vm.concluidas, key = { it.id }) { atividade ->
            CartaoAtividade(
                atividade = atividade,
                onStatus = {
                    vm.reabrir(atividade.id)
                },
                onExcluir = {
                    excluirId = atividade.id
                }
            )
        }
    }
}

// COMPONENTE DE SELEÇÃO
@Composable
private fun SeletorCategoria(
    valor: String,
    opcoes: List<String>,
    onSelecionar: (String) -> Unit,
    titulo: String
) {
    var expandido by remember { mutableStateOf(false) }

    Column {
        Text(titulo)

        Box {
            OutlinedButton(onClick = { expandido = true }) {
                Text(valor)
            }

            DropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                opcoes.forEach { opcao ->
                    DropdownMenuItem(
                        text = { Text(opcao) },
                        onClick = {
                            onSelecionar(opcao)
                            expandido = false
                        }
                    )
                }
            }
        }
    }
}

// COMPONENTE DOS CONTADORES
@Composable
private fun Resumo(
    titulo: String,
    quantidade: Int,
    cor: Color,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(Modifier.padding(10.dp)) {
            Text(
                titulo,
                style = MaterialTheme.typography.labelSmall,
                color = cor
            )
            Text(
                quantidade.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = cor
            )
        }
    }
}

// COMPONENTE DOS CARTÕES DE ATIVIDADE
@Composable
private fun CartaoAtividade(
    atividade: Atividade,
    onStatus: () -> Unit,
    onExcluir: () -> Unit
) {
    val concluida = atividade.status == "concluida"

    Card(
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (concluida) Verde else Laranja
        )
    ) {
        Column(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                atividade.categoria,
                color = AzulEscuro,
                fontWeight = FontWeight.Bold
            )

            Text(
                atividade.titulo,
                style = MaterialTheme.typography.titleMedium,
                textDecoration = if (concluida)
                    TextDecoration.LineThrough
                else
                    TextDecoration.None
            )

            if (atividade.descricao.isNotBlank()) {
                Text(atividade.descricao)
            }

            val formato = SimpleDateFormat(
                "dd/MM/yyyy HH:mm",
                Locale("pt", "BR")
            )

            Text("Criada em: ${formato.format(Date(atividade.dataCriacao))}")
            Text("Início: ${atividade.dataInicio.ifBlank { "Não informado" }}")
            Text("Término: ${atividade.dataFim.ifBlank { "Não informado" }}")

            if (atividade.endereco.isNotBlank()) {
                Text("Local: ${atividade.endereco}")
            }

            Text(
                if (concluida) "Concluída" else "Pendente",
                color = if (concluida) Verde else Laranja
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onStatus,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulEscuro
                    )
                ) {
                    Text(if (concluida) "Reabrir" else "Concluir")
                }

                OutlinedButton(onClick = onExcluir) {
                    Text("Excluir", color = Vermelho)
                }
            }
        }
    }
}
