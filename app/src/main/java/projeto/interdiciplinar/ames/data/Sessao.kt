package projeto.interdiciplinar.ames.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import projeto.interdiciplinar.ames.model.UsuarioResposta

object Sessao {
    var token by mutableStateOf<String?>(null)
        private set

    var usuario by mutableStateOf<UsuarioResposta?>(null)
        private set

    fun iniciar(novoToken: String, novoUsuario: UsuarioResposta) {
        usuario = novoUsuario
        token = novoToken
    }

    fun encerrar() {
        token = null
        usuario = null
    }
}