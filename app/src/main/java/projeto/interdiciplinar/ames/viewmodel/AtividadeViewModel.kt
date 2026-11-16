package projeto.interdiciplinar.ames.viewmodel

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import projeto.interdiciplinar.ames.model.Atividade
import projeto.interdiciplinar.ames.repository.AtividadeRepository
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.Locale

class AtividadeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        AtividadeRepository(application)

    var atividades by mutableStateOf(repository.listar())
        private set

    var busca by mutableStateOf("")
        private set

    var categoriaFiltro by mutableStateOf("Todos os tipos")
        private set

    val categorias = listOf(
        "Tarefa doméstica",
        "Compromisso",
        "Medicamento",
        "Outro"
    )

    val total: Int
        get() = atividades.size

    val totalPendentes: Int
        get() = atividades.count {
            it.status == "pendente"
        }

    val totalConcluidas: Int
        get() = atividades.count {
            it.status == "concluida"
        }

    val atividadesFiltradas: List<Atividade>
        get() = atividades.filter { atividade ->

            val termo = normalizar(busca)

            val correspondeTexto =
                normalizar(atividade.titulo).contains(termo) ||
                        normalizar(atividade.descricao).contains(termo)

            val correspondeCategoria =
                categoriaFiltro == "Todos os tipos" ||
                        normalizar(atividade.categoria) ==
                        normalizar(categoriaFiltro)

            correspondeTexto && correspondeCategoria
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

    fun cadastrar(
        titulo: String,
        descricao: String,
        categoria: String,
        inicio: String,
        fim: String,
        endereco: String
    ): String {

        if (titulo.isBlank() || categoria !in categorias) {
            return "Erro: informe o título e uma categoria."
        }

        if (titulo.length > 150 || descricao.length > 500) {
            return "Erro: título ou descrição muito longos."
        }

        if (endereco.length > 255) {
            return "Erro: endereço muito longo."
        }

        val dataInicio = analisarData(inicio)
        val dataFim = analisarData(fim)

        if ((inicio.isNotBlank() && dataInicio == null) ||
            (fim.isNotBlank() && dataFim == null)
        ) {
            return "Erro: data inválida. Use dd/MM/aaaa HH:mm."
        }

        if (dataInicio != null && dataFim != null &&
            dataFim < dataInicio
        ) {
            return "Erro: término anterior ao início."
        }

        val novaAtividade = Atividade(
            titulo = titulo.trim(),
            descricao = descricao.trim(),
            categoria = categoria,
            dataInicio = inicio.trim(),
            dataFim = fim.trim(),
            endereco = endereco.trim()
        )

        salvarLista(atividades + novaAtividade)

        return "Atividade cadastrada com sucesso!"
    }

    fun concluir(id: String) {
        atualizarStatus(id, "concluida")
    }

    fun reabrir(id: String) {
        atualizarStatus(id, "pendente")
    }

    private fun atualizarStatus(
        id: String,
        novoStatus: String
    ) {
        salvarLista(
            atividades.map { atividade ->
                if (atividade.id == id) {
                    atividade.copy(status = novoStatus)
                } else {
                    atividade
                }
            }
        )
    }

    fun excluir(id: String) {
        salvarLista(
            atividades.filterNot {
                it.id == id
            }
        )
    }

    private fun salvarLista(lista: List<Atividade>) {
        atividades = lista.sortedByDescending {
            it.dataCriacao
        }

        repository.salvar(atividades)
    }

    private fun normalizar(texto: String): String {
        return Normalizer.normalize(
            texto.lowercase(Locale.ROOT),
            Normalizer.Form.NFD
        ).replace(Regex("\\p{Mn}+"), "").trim()
    }

    private fun analisarData(texto: String): Long? {
        if (texto.isBlank()) return null

        return try {
            val formato = SimpleDateFormat(
                "dd/MM/yyyy HH:mm",
                Locale("pt", "BR")
            )

            formato.isLenient = false

            formato.parse(texto.trim())?.time
        } catch (e: Exception) {
            null
        }
    }
}