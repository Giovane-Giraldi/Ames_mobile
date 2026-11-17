package projeto.interdiciplinar.ames.repository

import com.google.gson.JsonParser
import projeto.interdiciplinar.ames.data.ApiClient
import projeto.interdiciplinar.ames.data.Sessao
import projeto.interdiciplinar.ames.model.Atividade
import projeto.interdiciplinar.ames.model.AtividadeEnvio
import projeto.interdiciplinar.ames.model.MensagemResposta
import projeto.interdiciplinar.ames.model.StatusEnvio
import retrofit2.Response

object AtividadeApiRepository {

    suspend fun listar(): List<Atividade> =
        executar { ApiClient.api.listarAtividades() }

    suspend fun cadastrar(dados: AtividadeEnvio): Atividade =
        executar { ApiClient.api.criarAtividade(dados) }

    suspend fun atualizarStatus(
        id: String,
        status: String
    ): Atividade =
        executar {
            ApiClient.api.atualizarStatus(id, StatusEnvio(status))
        }

    suspend fun excluir(id: String): MensagemResposta =
        executar { ApiClient.api.excluirAtividade(id) }

    private suspend fun <T> executar(
        chamada: suspend () -> Response<T>
    ): T {
        val tokenInicial = Sessao.token
            ?: error("Faça login para continuar.")

        val resposta = chamada()

        // Não entrega dados de uma sessão anterior após trocar de conta.
        if (Sessao.token != tokenInicial) {
            error("A sessão mudou. Entre novamente.")
        }

        if (resposta.code() == 401) {
            Sessao.encerrar()
            error("Sessão expirada. Faça login novamente.")
        }

        if (!resposta.isSuccessful) {
            val texto = resposta.errorBody()?.string()

            val mensagem = runCatching {
                JsonParser.parseString(texto)
                    .asJsonObject
                    .get("mensagem")
                    ?.asString
            }.getOrNull()
                ?: "Erro HTTP ${resposta.code()}"

            error(mensagem)
        }

        return resposta.body()
            ?: error("A API retornou uma resposta vazia.")
    }
}