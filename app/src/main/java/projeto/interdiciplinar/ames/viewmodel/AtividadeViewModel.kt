package projeto.interdiciplinar.ames.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.io.IOException
import java.text.Normalizer
import java.util.Locale
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import projeto.interdiciplinar.ames.data.Sessao
import projeto.interdiciplinar.ames.model.Atividade
import projeto.interdiciplinar.ames.model.AtividadeEnvio
import projeto.interdiciplinar.ames.repository.AtividadeApiRepository

class AtividadeViewModel : ViewModel() {

    var atividades by mutableStateOf<List<Atividade>>(emptyList())
        private set

    var ocupado by mutableStateOf(false)
        private set

    var mensagem by mutableStateOf<String?>(null)
        private set

    var erro by mutableStateOf(false)
        private set

    var cadastroConcluido by mutableStateOf(false)
        private set

    var busca by mutableStateOf("")
        private set

    var categoriaFiltro by mutableStateOf("Todos os tipos")
        private set

    private var trabalho: Job? = null
    private var geracao = 0

    val categorias = listOf(
        "Tarefa doméstica",
        "Compromisso",
        "Medicamento",
        "Outro"
    )

    val total: Int
        get() = atividades.size

    val totalPendentes: Int
        get() = atividades.count { it.status == "pendente" }

    val totalConcluidas: Int
        get() = atividades.count { it.status == "concluida" }

    val atividadesFiltradas: List<Atividade>
        get() {
            val termo = normalizar(busca)

            return atividades.filter { atividade ->
                val correspondeTexto =
                    normalizar(atividade.titulo).contains(termo) ||
                            normalizar(atividade.descricao).contains(termo)

                val correspondeCategoria =
                    categoriaFiltro == "Todos os tipos" ||
                            atividade.categoria == categoriaFiltro

                correspondeTexto && correspondeCategoria
            }
        }

    val pendentes: List<Atividade>
        get() = atividadesFiltradas.filter {
            it.status == "pendente"
        }

    val concluidas: List<Atividade>
        get() = atividadesFiltradas.filter {
            it.status == "concluida"
        }

    fun atualizarBusca(texto: String) {
        busca = texto
    }

    fun atualizarFiltro(categoria: String) {
        categoriaFiltro = categoria
    }

    fun listar() {
        executar {
            atividades = AtividadeApiRepository.listar()
        }
    }

    fun cadastrar(
        titulo: String,
        descricao: String,
        categoria: String,
        inicio: String,
        fim: String,
        endereco: String
    ) {
        if (ocupado) return

        cadastroConcluido = false

        if (titulo.isBlank() || categoria !in categorias) {
            erro = true
            mensagem = "Informe o título e uma categoria válida."
            return
        }

        executar {
            val nova = AtividadeApiRepository.cadastrar(
                AtividadeEnvio(
                    titulo = titulo.trim(),
                    descricao = descricao.trim(),
                    categoria = categoria,
                    dataInicio = inicio.trim(),
                    dataFim = fim.trim(),
                    endereco = endereco.trim()
                )
            )

            atividades = (atividades + nova)
                .sortedByDescending { it.dataCriacao }

            mensagem = "Atividade cadastrada com sucesso!"
            cadastroConcluido = true
        }
    }

    fun concluir(id: String) {
        atualizarStatus(id, "concluida")
    }

    fun reabrir(id: String) {
        atualizarStatus(id, "pendente")
    }

    private fun atualizarStatus(id: String, status: String) {
        executar {
            val atualizada =
                AtividadeApiRepository.atualizarStatus(id, status)

            atividades = atividades.map {
                if (it.id == id) atualizada else it
            }

            mensagem = if (status == "concluida") {
                "Atividade concluída!"
            } else {
                "Atividade reaberta!"
            }
        }
    }

    fun excluir(id: String) {
        executar {
            AtividadeApiRepository.excluir(id)

            atividades = atividades.filterNot { it.id == id }
            mensagem = "Atividade excluída com sucesso!"
        }
    }

    fun consumirCadastro() {
        cadastroConcluido = false
    }

    fun limparMensagem() {
        mensagem = null
        erro = false
    }

    fun limpar() {
        geracao++
        trabalho?.cancel()
        trabalho = null

        atividades = emptyList()
        ocupado = false
        mensagem = null
        erro = false
        cadastroConcluido = false
        busca = ""
        categoriaFiltro = "Todos os tipos"
    }

    private fun executar(acao: suspend () -> Unit) {
        if (ocupado) return

        val geracaoAtual = geracao

        ocupado = true
        mensagem = null
        erro = false

        trabalho = viewModelScope.launch {
            try {
                acao()
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                if (geracaoAtual == geracao) {
                    erro = true
                    mensagem = "Não foi possível conectar à API."
                }
            } catch (e: Exception) {
                if (geracaoAtual == geracao) {
                    if (Sessao.token == null) {
                        atividades = emptyList()
                    }

                    erro = true
                    mensagem = e.message
                        ?: "Não foi possível concluir a operação."
                }
            } finally {
                if (geracaoAtual == geracao) {
                    ocupado = false
                }
            }
        }
    }

    private fun normalizar(texto: String): String {
        return Normalizer.normalize(
            texto.lowercase(Locale.ROOT),
            Normalizer.Form.NFD
        ).replace(Regex("\\p{Mn}+"), "").trim()
    }
}