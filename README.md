# EnvManager

Easily control your environment properties.

### Instalação

Para instalar, baixe o arquivo de instalação disponível em [releases](https://github.com/Irineu333/EnvManager/releases),
extraia e execute `install.sh` (Linux) com permissões de administrador. Para informações mais detalhadas consulte
as [instruções de instalação](src/dist/INSTRUCTIONS.md).

> **Compatibilidade:** GNU/Linux, Termux

### Funcionamento

O **EnvManager** é ideal para projetos com **múltiplos ambientes**, onde as variáveis são gerenciadas por um **arquivo
de propriedades** (denominado **target**) no formato `CHAVE=VALOR`. Com o **EnvManager**, você pode alternar entre
diferentes ambientes e realizar outras manipulações **diretamente do terminal**.

### Uso

Após a instalação, o **EnvManager** é acessado pelo comando `envm`, seguido de um comando e seus argumentos.

``` shell
$ envm [options] <command> [arguments]
```

> **Outras opções:** --path=\<caminho do projeto>, --version, --show-config

Você pode obter as instruções de qualquer comando a opção `--help`.

``` shell
$ envm --help
```

[Mais informações](doc/envm.md).

### Comandos Essenciais

Esses comandos cobrem as principais funcionalidades para uso diário.

#### install

Inicialize o **EnvManager** no diretório do seu projeto com o comando `install`.

``` shell
$ envm install
```

> **Opções:** --target=\<caminho do target>, --force

[Mais detalhes](doc/install.md).

#### save

Salve as propriedades atuais como um ambiente usando `save`, seguido do nome do ambiente (denominado **tag**).

``` shell
$ envm save <tag>
```

> **Opções:** --clipboard

[Mais detalhes](doc/save.md)

#### list

Liste os ambientes salvos com o comando `list`.

``` shell
$ envm list
```

Para listar propriedades de um ambiente específico, adicione a **tag** após o comando.

``` shell
$ envm list <tag>
```

> **Opções:** --current, --target

[Mais detalhes](doc/list.md)

#### checkout

Mude para um ambiente diferente com `checkout`.

``` shell
$ envm checkout <tag>
```

> **Opções:** --force

[Mais detalhes](doc/checkout.md)

#### delete

Exclua um ou mais ambientes com `delete`.

``` shell
$ envm delete <tags>
```

> **Opções:** --all

[Mais detalhes](doc/delete.md)

### Outros Comandos

#### set

Adicione ou modifique propriedades com `set`.

``` shell
$ envm set <properties>
```

> **Opções:** --tag=\<tag>, --all, --target-only

[Mais detalhes](doc/set.md)

#### remove

Remova propriedades específicas com `remove`.

``` shell
$ envm remove <keys>
```

> **Opções:** --tag=\<tag>, --all, --target-only

[Mais detalhes](doc/remove.md)

#### rename

Renomeie um ambiente com `rename`.

``` shell
$ envm rename <old-tag> <new-tag>
```

#### rollback

Reverta alterações no **target**, sincronizando com o ambiente atual, usando `rollback`.

``` shell
$ envm rollback
```