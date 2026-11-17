package projeto.interdiciplinar.ames.model

data class RegistroEnvio(
    val nome: String,
    val email: String,
    val senha: String
)

data class LoginEnvio(
    val email: String,
    val senha: String
)

data class UsuarioResposta(
    val id: String,
    val nome: String,
    val email: String
)

data class LoginResposta(
    val token: String,
    val usuario: UsuarioResposta
)

data class MensagemResposta(
    val mensagem: String?
)

data class AtividadeEnvio(
    val titulo: String,
    val descricao: String = "",
    val categoria: String,
    val dataInicio: String = "",
    val dataFim: String = "",
    val endereco: String = ""
)

data class StatusEnvio(
    val status: String
)