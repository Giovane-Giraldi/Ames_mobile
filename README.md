# AMES — Gerenciamento de Atividades

Projeto desenvolvido como parte das atividades acadêmicas do curso de **Desenvolvimento de Software Multiplataforma (DSM)** da **FATEC**.

## Sobre o projeto

O **AMES** é uma aplicação voltada à organização e ao gerenciamento de atividades diárias, permitindo que o usuário cadastre, acompanhe e conclua suas tarefas de maneira simples e prática.

Esta versão foi desenvolvida em **Kotlin**, utilizando **Jetpack Compose** e a arquitetura **MVVM (Model–View–ViewModel)**, com base nas funcionalidades originalmente implementadas em PHP.

## Funcionalidades

- Cadastro de atividades com título, descrição e categoria.
- Definição das datas e horários de início e término.
- Cadastro de endereço.
- Listagem de atividades pendentes e concluídas.
- Conclusão, reabertura e exclusão de atividades.
- Pesquisa por título ou descrição.
- Filtro por categoria.
- Contadores de atividades.
- Validação dos dados informados.
- Armazenamento local das atividades.

## Tecnologias utilizadas

- **Kotlin:** linguagem de programação.
- **Jetpack Compose:** desenvolvimento da interface.
- **Material 3:** componentes visuais.
- **MVVM:** organização da aplicação em camadas.
- **SharedPreferences:** armazenamento local.
- **JSON:** serialização dos dados.

## Estrutura do projeto

```text
com.example.ames/
│
├── model/
│   └── Atividade.kt
│
├── repository/
│   └── AtividadeRepository.kt
│
├── viewmodel/
│   └── AtividadeViewModel.kt
│
├── view/
│   └── ActivitiesScreen.kt
│
└── MainActivity.kt
```

## Arquitetura MVVM

O projeto utiliza uma divisão de responsabilidades:

- **Model:** representa os dados das atividades.
- **View:** apresenta a interface e recebe as interações do usuário.
- **ViewModel:** gerencia os estados e as regras de negócio.
- **Repository:** realiza o armazenamento e a recuperação dos dados.

Essa organização facilita a manutenção, a reutilização de código e a implementação de novas funcionalidades.

## Como executar

1. Abra o projeto no Android Studio.
2. Configure as dependências do Jetpack Compose, Material 3 e ViewModel.
3. Organize os arquivos Kotlin nos pacotes correspondentes.
4. Execute o aplicativo em um emulador Android ou dispositivo físico.

## Limitações

- Os dados são armazenados localmente no dispositivo.
- Não há integração com o banco MySQL do projeto PHP original.
- A conversão de endereços em coordenadas geográficas não está implementada.
- O módulo de lembretes não faz parte desta versão.

## Integrantes

| Nome | Curso | Instituição |
|---|---|---|
| Giovane | DSM | FATEC |
| Thaisa | DSM | FATEC |
| Kaua | DSM | FATEC |
| Davi | DSM | FATEC |

## Objetivo acadêmico

Aplicar os conhecimentos de desenvolvimento mobile com Kotlin, organização de código por camadas e gerenciamento de estados, reproduzindo as principais funcionalidades de um sistema de atividades originalmente desenvolvido para a web.

---

**AMES — Projeto Interdisciplinar | FATEC — DSM**
