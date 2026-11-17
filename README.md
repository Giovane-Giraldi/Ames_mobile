


# AMES Mobile

Projeto desenvolvido para a disciplina/interdisciplinar do curso de **Desenvolvimento de Software Multiplataforma (DSM)** da **FATEC**.

O **AMES Mobile** é uma aplicação Android desenvolvida em **Kotlin** com **Jetpack Compose**, com foco no gerenciamento de atividades. O app permite autenticação de usuários e operações de cadastro, listagem, atualização e exclusão de atividades, utilizando uma arquitetura organizada em camadas.

---

## Sobre o projeto

O objetivo do projeto é reproduzir em versão mobile as principais funcionalidades do sistema **AMES**, originalmente desenvolvido para web, agora utilizando tecnologias modernas do ecossistema Android.

A aplicação segue o padrão **MVVM (Model - View - ViewModel)**, com separação entre interface, lógica de negócio, modelos e acesso a dados.

---

## Funcionalidades

### Autenticação
- Cadastro de usuário
- Login de usuário
- Controle de sessão com token
- Logout

### Atividades
- Cadastro de atividades
- Listagem de atividades
- Alteração de status da atividade
- Exclusão de atividades
- Organização por categoria
- Informações como título, descrição, datas e endereço

### Estrutura do app
- Navegação entre telas
- Consumo de API REST
- Gerenciamento de estado com ViewModel
- Separação por camadas

---

## Tecnologias utilizadas

- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- **MVVM**
- **Retrofit**
- **Gson Converter**
- **OkHttp**
- **Navigation Compose**
- **Kotlin Coroutines**
- **Android ViewModel**
- **API REST**

---

## Estrutura do projeto

Com base na estrutura atual do projeto:

```text
app/
└── src/
    └── main/
        └── java/
            └── projeto/
                └── interdiciplinar/
                    └── ames/
                        ├── data/
                        │   ├── Api.kt
                        │   ├── ApiClient.kt
                        │   └── Sessao.kt
                        │
                        ├── model/
                        │   ├── ApiModelos.kt
                        │   └── Atividade.kt
                        │
                        ├── repository/
                        │   ├── AtividadeApiRepository.kt
                        │   ├── AtividadeRepository.kt
                        │   └── AuthRepository.kt
                        │
                        ├── ui/
                        │   └── theme/
                        │       ├── Color.kt
                        │       ├── Theme.kt
                        │       └── Type.kt
                        │
                        ├── view/
                        │   ├── ActivitiesScreen.kt
                        │   ├── CadastroScreen.kt
                        │   └── LoginScreen.kt
                        │
                        ├── viewmodel/
                        │   ├── AtividadeViewModel.kt
                        │   └── AuthViewmodel.kt
                        │
                        ├── AppAmes.kt
                        └── MainActivity.kt
```

---

## Organização das camadas

### `model`
Responsável por representar os dados da aplicação, como:
- atividade
- login
- registro
- resposta da API
- status
- usuário

### `repository`
Responsável pelo acesso aos dados e pela comunicação com a API ou outras fontes de dados.

### `viewmodel`
Responsável pela lógica da aplicação e pelo gerenciamento de estado da interface.

### `view`
Responsável pelas telas do aplicativo, como:
- Login
- Cadastro
- Lista de atividades

### `data`
Responsável pela configuração da API, cliente HTTP e gerenciamento da sessão do usuário.

---

## Comunicação com API

A aplicação utiliza o **Retrofit** para se comunicar com o backend.

### Endpoints configurados em `Api.kt`

- `POST /usuarios/registrar`
- `POST /usuarios/login`
- `GET /usuarios/me`
- `GET /atividades`
- `POST /atividades`
- `PATCH /atividades/{id}/status`
- `DELETE /atividades/{id}`

---

## Sessão do usuário

A sessão é gerenciada pelo objeto `Sessao`, que armazena:

- Token de autenticação
- Dados do usuário logado

O token é enviado automaticamente nas requisições por meio de um interceptor configurado no `ApiClient.kt`.


---

## Como executar o projeto

1. Abra o projeto no **Android Studio**
2. Aguarde o Gradle sincronizar
3. Verifique se as dependências estão corretas
4. Configure a URL da API, se necessário
5. Inicie o backend
6. Execute o app em um emulador ou dispositivo físico

---

## Limitações atuais

- Dependência de um backend ativo para funcionamento das rotas da API
- A URL da API pode precisar de ajuste dependendo do ambiente
- Algumas funcionalidades podem estar em evolução conforme o desenvolvimento do projeto

---

## Integrantes

- **Giovane Giraldi**
- **Thaisa Vitória**
- **Kauã Carlos**
- **Davi Capelari**

---

## Objetivo acadêmico

Este projeto tem como objetivo aplicar conhecimentos de:

- desenvolvimento mobile com Kotlin
- arquitetura MVVM
- consumo de API REST
- gerenciamento de estado
- organização em camadas
- construção de interfaces com Jetpack Compose

---
