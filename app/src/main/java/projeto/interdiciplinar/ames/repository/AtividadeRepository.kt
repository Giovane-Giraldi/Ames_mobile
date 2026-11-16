package projeto.interdiciplinar.ames.repository

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import projeto.interdiciplinar.ames.model.Atividade

class AtividadeRepository(context: Context) {

    private val preferencias = context.getSharedPreferences(
        "ames_atividades",
        Context.MODE_PRIVATE
    )

    fun listar(): List<Atividade> {
        return try {
            val json = preferencias.getString(
                "atividades_json", "[]"
            ) ?: "[]"

            val array = JSONArray(json)
            val atividades = mutableListOf<Atividade>()

            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)

                atividades.add(
                    Atividade(
                        id = obj.getString("id"),
                        titulo = obj.getString("titulo"),
                        descricao = obj.optString("descricao"),
                        categoria = obj.optString("categoria"),
                        dataInicio = obj.optString("inicio"),
                        dataFim = obj.optString("fim"),
                        endereco = obj.optString("endereco"),
                        status = obj.optString("status", "pendente"),
                        dataCriacao = obj.optLong("criadaEm")
                    )
                )
            }

            atividades.sortedByDescending {
                it.dataCriacao
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun salvar(atividades: List<Atividade>) {
        val array = JSONArray()

        atividades.forEach { atividade ->
            val obj = JSONObject().apply {
                put("id", atividade.id)
                put("titulo", atividade.titulo)
                put("descricao", atividade.descricao)
                put("categoria", atividade.categoria)
                put("inicio", atividade.dataInicio)
                put("fim", atividade.dataFim)
                put("endereco", atividade.endereco)
                put("status", atividade.status)
                put("criadaEm", atividade.dataCriacao)
            }

            array.put(obj)
        }

        preferencias.edit()
            .putString("atividades_json", array.toString())
            .apply()
    }
}
