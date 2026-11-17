package projeto.interdiciplinar.ames.repository

import com.google.gson.JsonParser
import projeto.interdiciplinar.ames.data.ApiClient
import projeto.interdiciplinar.ames.data.Sessao
import projeto.interdiciplinar.ames.model.LoginEnvio
import projeto.interdiciplinar.ames.model.MensagemResposta
import projeto.interdiciplinar.ames.model.RegistroEnvio
import retrofit2.Response

object AuthRepository {

    suspend fun registrar(
        nome: String,
        email: String,
        senha: String
    ): MensagemResposta {
        return lerResposta(
            ApiClient.api.registrar(
                RegistroEnvio(
                    nome = nome.trim(),
                    email = email.trim(),
                    senha = senha
                )
            )
        )
    }

    suspend fun entrar(email: String, senha: String) {
        val resposta = lerResposta(
            ApiClient.api.login(
                LoginEnvio(
                    email = email.trim(),
                    senha = senha
                )
            )
        )

        check(resposta.token.isNotBlank()) {
            "A API não retornou um token válido."
        }

        Sessao.iniciar(resposta.token, resposta.usuario)
    }

    fun sair() {
        Sessao.encerrar()
    }

    private fun <T> lerResposta(resposta: Response<T>): T {
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