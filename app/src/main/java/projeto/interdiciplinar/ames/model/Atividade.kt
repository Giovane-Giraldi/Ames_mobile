package projeto.interdiciplinar.ames.model

import java.util.UUID

data class Atividade(
    val id: String = UUID.randomUUID().toString(),
    val titulo: String,
    val descricao: String = "",
    val categoria: String,
    val dataInicio: String = "",
    val dataFim: String = "",
    val endereco: String = "",
    val status: String = "pendente",
    val dataCriacao: Long = System.currentTimeMillis()
)
