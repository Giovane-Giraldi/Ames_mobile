package projeto.interdiciplinar.ames.data

import projeto.interdiciplinar.ames.model.*
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @POST("usuarios/registrar")
    suspend fun registrar(
        @Body dados: RegistroEnvio
    ): Response<MensagemResposta>

    @POST("usuarios/login")
    suspend fun login(
        @Body dados: LoginEnvio
    ): Response<LoginResposta>

    @GET("usuarios/me")
    suspend fun consultarUsuario(): Response<UsuarioResposta>

    @GET("atividades")
    suspend fun listarAtividades(): Response<List<Atividade>>

    @POST("atividades")
    suspend fun criarAtividade(
        @Body dados: AtividadeEnvio
    ): Response<Atividade>

    @PATCH("atividades/{id}/status")
    suspend fun atualizarStatus(
        @Path("id") id: String,
        @Body dados: StatusEnvio
    ): Response<Atividade>

    @DELETE("atividades/{id}")
    suspend fun excluirAtividade(
        @Path("id") id: String
    ): Response<MensagemResposta>
}