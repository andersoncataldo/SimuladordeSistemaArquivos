# Simulador de Sistema de Arquivos

## Resumo
Este projeto consiste em um simulador acadêmico de sistema de arquivos desenvolvido em Java. Ele permite a criação, remoção, renomeação e cópia de arquivos e diretórios através de um shell interativo, implementando um mecanismo de journaling (log de transações) para garantir a integridade das operações.

## Introdução
O gerenciamento de arquivos é uma das funções primordiais de um Sistema Operacional (SO), sendo responsável por organizar como os dados são armazenados e recuperados. A base dos SOs modernos depende de sistemas de arquivos robustos que possam lidar com falhas de energia ou travamentos do sistema sem corromper a estrutura de dados, o que torna o estudo de mecanismos como o journaling essencial.

## Objetivo
O objetivo geral deste simulador é demonstrar o funcionamento lógico de um sistema de arquivos hierárquico e a implementação prática de um sistema de journaling (Write-Ahead Logging). O foco é garantir que toda operação de modificação seja registrada em um log físico antes de ser aplicada na estrutura em memória, simulando a proteção contra corrupção de dados.

## Metodologia
O simulador foi implementado utilizando a linguagem Java, seguindo os princípios de Orientação a Objetos. A arquitetura divide-se em classes de modelo (representando arquivos e pastas) e classes de serviço (responsáveis pela lógica do sistema e pelo journaling). O sistema recebe comandos através de um console interativo (Shell), processa as chamadas de métodos e persiste o histórico de operações em um arquivo de log real no disco.

---

## Parte 1: Introdução ao Sistema de Arquivos com Journaling

### Descrição do sistema de arquivos
Um sistema de arquivos é o método e a estrutura de dados que um sistema operacional utiliza para controlar como os dados são armazenados e recuperados. Sem ele, a informação colocada em um meio de armazenamento seria um grande corpo de dados sem maneira de saber onde uma informação termina e a próxima começa.

### Journaling
O propósito do Journaling é manter a consistência do sistema de arquivos em caso de falhas sistêmicas. 
* **Funcionamento:** Antes de realizar uma mudança na estrutura de arquivos (como criar um arquivo), o sistema escreve a intenção da mudança em um "jornal" (log). Após o registro bem-sucedido no log, a operação é executada no sistema de arquivos real.
* **Tipos:**
    * **Write-Ahead Logging (WAL):** As alterações são escritas no log antes de serem aplicadas.
    * **Log-structured File Systems:** O próprio sistema de arquivos é estruturado como um log contínuo.

---

## Parte 2: Arquitetura do Simulador

### Estrutura de Dados
O simulador utiliza uma estrutura de árvore para representar o sistema de arquivos:
* **Directory:** Representa os nós internos e a raiz, contendo listas de subdiretórios e arquivos.
* **File:** Representa as folhas da árvore.
* **FileSystemSimulator:** Gerencia o estado atual (diretório de trabalho) e coordena as operações.

### Journaling e Persistência (Modo Avançado)
O log foi implementado através da classe `Journal`, que utiliza a API de I/O do Java para gravar cada operação em um arquivo chamado `journal.log` no disco físico. 
* **Persistência Real:** Diferente de simuladores que perdem dados ao fechar, este projeto implementa um mecanismo de **Recovery**.
* **Mecanismo de Recuperação:** Ao iniciar, o `FileSystemSimulator` lê o arquivo `journal.log`, interpreta as operações passadas e reconstrói toda a árvore de diretórios e arquivos em memória. Isso garante que o estado do sistema seja preservado entre execuções, simulando fielmente um sistema de arquivos real baseado em log.

---

## Parte 3: Implementação em Java

* **FileSystemSimulator:** Centraliza a lógica de negócio. Implementa métodos como `mkdir`, `rm`, `cp` e `rename`. Possui o método `recover()` que garante a persistência através do log.
* **File:** Classe simples que armazena metadados básicos do arquivo, como nome e referência ao diretório pai.
* **Directory:** Gerencia as coleções de arquivos e subdiretórios, permitindo a navegação e busca de itens por nome.
* **Journal:** Classe utilitária responsável pela persistência física do log. Permite tanto a escrita (append) quanto a leitura para fins de recuperação.

---

## Parte 4: Instalação e Funcionamento

### Recursos Usados
* Java Development Kit (JDK) 8 ou superior.
* IDE (IntelliJ IDEA, Eclipse) ou apenas terminal.

### Como Compilar
No terminal, dentro da pasta `src`, execute:
```bash
javac Main.java model/*.java service/*.java
```

### Como Iniciar o Modo Shell
Após a compilação, execute:
```bash
java Main
```

### Persistência
Para testar a persistência:
1. Abra o simulador e crie alguns arquivos e pastas.
2. Saia do simulador com `exit`.
3. Abra o simulador novamente. Note que o sistema exibirá "Recuperando estado..." e seus arquivos estarão lá.
4. Para resetar o simulador, basta apagar o arquivo `journal.log`.

---

## Resultados Esperados
O simulador fornece insights valiosos sobre a camada de abstração que os Sistemas Operacionais oferecem sobre o hardware. Através da implementação do Journaling com recuperação automática, o projeto demonstra como a integridade de dados é mantida mesmo após o encerramento do sistema. Este projeto comprova que um sistema de arquivos é, em sua essência, uma estrutura de dados persistente gerenciada por transações lógicas.
