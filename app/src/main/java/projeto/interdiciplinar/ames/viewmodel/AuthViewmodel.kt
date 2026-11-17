package projeto.interdiciplinar.ames.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import projeto.interdiciplinar.ames.repository.AuthRepository

class AuthViewModel : ViewModel() {

    var ocupado by mutableStateOf(false)
        private set

    var mensagem by mutableStateOf<String?>(null)
        private set

    var cadastroConcluido by mutableStateOf(false)
        private set

    fun entrar(email: String, senha: String) {
        if (ocupado) return

        if (email.isBlank() || senha.isEmpty()) {
            mensagem = "Informe email e senha."
            return
        }

        executar {
            AuthRepository.entrar(email, senha)
        }
    }

    fun cadastrar(nome: String, email: String, senha: String) {
        if (ocupado) return

        if (nome.isBlank() || email.isBlank() || senha.isEmpty()) {
            mensagem = "Preencha todos os campos."
            return
        }

        if (senha.length < 8) {
            mensagem = "A senha deve ter pelo menos 8 caracteres."
            return
        }

        cadastroConcluido = false

        executar {
            AuthRepository.registrar(nome, email, senha)
            cadastroConcluido = true
        }
    }

    fun consumirCadastro() {
        cadastroConcluido = false
        mensagem = null
    }

    fun limparMensagem() {
        mensagem = null
    }

    fun sair() {
        if (ocupado) return

        AuthRepository.sair()
        mensagem = null
        cadastroConcluido = false
    }

    private fun executar(acao: suspend () -> Unit) {
        if (ocupado) return

        ocupado = true
        mensagem = null

        viewModelScope.launch {
            try {
                acao()
            } catch (erro: CancellationException) {
                throw erro
            } catch (erro: IOException) {
                mensagem = "Não foi possível conectar à API. Verifique a conexão."
            } catch (erro: Exception) {
                mensagem = erro.message ?: "Não foi possível concluir a operação."
            } finally {
                ocupado = false
            }
        }
    }
}