# Projeto Final Mobile (Android)

Este repositório contém um aplicativo Android nativo desenvolvido em Java/Kotlin (Gradle), com a proposta principal de gerenciar componentes cadastrados em um banco local (SQLite) e apresentar uma interface simples para inclusão, listagem e remoção de itens.

## Proposta Principal do Aplicativo

- Permitir ao usuário cadastrar componentes (por exemplo: nome, descrição, quantidade, preço).
- Persistir os dados localmente via SQLite usando classes DAO.
- Listar os componentes cadastrados em tela, com atualização ao inserir/excluir.
- Excluir componentes selecionados quando necessário.

A estrutura do projeto pode ser vista em `app/src/main/java/com/example/projeto_final/` com as classes:
- `BancoDados.java`: gerenciamento da base SQLite.
- `Componente.java`: modelo da entidade de componente.
- `ComponenteDAO.java`: operações de CRUD.
- `MainActivity.java`: camada de interface e interação com o usuário.

## Regras de Negócio

As regras de negócio estão descritas abaixo. Caso prefira, elas podem ser movidas para um arquivo DOCX posteriormente.

- Cadastro de componente:
  - Campos obrigatórios: nome, quantidade.
  - Campos opcionais: descrição, preço.
  - Quantidade deve ser um número inteiro maior ou igual a 0.
  - Preço (se informado) deve ser um número decimal maior ou igual a 0.
  - Não permitir salvar quando o nome estiver vazio ou somente com espaços.

- Unicidade e consistência:
  - O nome identifica logicamente o componente. Duplicidades de nome devem ser evitadas; se um componente com o mesmo nome existir, o app deve alertar o usuário e permitir editar ou cancelar.

- Atualização de cadastro:
  - Ao atualizar um componente, validar novamente os campos obrigatórios.
  - Atualizações não podem resultar em valores negativos para quantidade ou preço.

- Exclusão de componente:
  - Deve exigir confirmação do usuário (diálogo) antes de excluir.
  - Exclusão deve ser permanente na base local.

- Listagem e ordenação:
  - Itens listados devem refletir o estado atual do banco.
  - Ordenação padrão por nome (ascendente). Opcional: ordenação por data de criação.

- Tratamento de erros:
  - Operações de banco devem tratar exceções (por exemplo, falhas de I/O) e exibir mensagens amigáveis.
  - Ao falhar uma operação (inserir/atualizar/excluir), não alterar o estado anterior e informar o usuário.

## Build e Execução

Pré-requisitos:
- Android Studio (versão compatível com Gradle do projeto).
- JDK 17 (ou conforme definido em `gradle.properties`).

Passos:
1. Abrir o projeto no Android Studio via `File > Open` e selecionar a pasta `Projeto_Final`.
2. Sincronizar Gradle quando solicitado.
3. Executar em um emulador ou dispositivo físico via botão "Run".

Via Gradle (PowerShell):
```
./gradlew.bat assembleDebug
```
O APK será gerado em `app/build/outputs/apk/debug/`.

## Estrutura de Pastas

- `app/src/main/java/com/example/projeto_final/`: código-fonte Java.
- `app/src/main/res/`: recursos (layouts, drawables, strings, temas).
- `app/src/main/AndroidManifest.xml`: manifesto do aplicativo.
- `gradle/` e `build.gradle.kts`: configuração de build.

## Próximos Passos

- Criar um documento `Regras_de_Negocio.docx` com estas regras (opcional) e adicioná-lo ao repositório.
- Adicionar testes instrumentados para validar CRUD no SQLite.
- Melhorar a UI/UX com validações inline e feedback.

## Licença

Este projeto é acadêmico. Caso necessário, adicione uma licença específica.