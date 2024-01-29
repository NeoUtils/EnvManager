# EnvManager

Easily control your environment properties.

### Instalação

Baixe o arquivo de instalção em releases, extraia e execute o arquivo `install.sh` (Linux) com permissão de
adiminstrador. [Saiba mais](src/dist/INSTRUCTIONS.md).

> **Suporte:** GNU/Linux, Termux

### Como funciona

O **EnvManager** é util para projetos que possuem **vários ambientes** e controlam suas variáveis através de um *
*arquivo de propriedades** (que chamamos de **target**), no formato `CHAVE=VALOR`. O **EnvManager** permite você trocar
facilmente entre os vários ambientes **direto do terminal**, além de outras manipulações, como adicionar e remover
variaveis etc.

### Como usar

Uma vez instalado no seu sistema, o **EnvManager** pode ser chamado do comando `envm`, seguido de um comando e seus
argumentos. Uma vez instalado no seu sistema, use o comando `envm` sem argumentos ou `envm --help` para consultar a lista de comandos.

``` shell
$ envm
```
> **Opções:** --help, --path=\<project path>, --version, --show-config

### Comandos básicos

#### install

Para começar a utilizar o **EnvManager** é necessário inicializa-lo no diretório do projeto, para isso utilize o
comando `install`.

``` shell
$ envm install
```

> **Opções:** --help, --target=\<target path>,--force

[Saiba mais](doc/envm.md).

#### save

Você pode salvar as propriedades atuais como um ambiente atraves do comando `save` seguido do nome do ambiente (que
chamamos de **tag**).

``` shell
# Exemplo
$ envm save development
```

> **Opções:** --help, --clipboard

[Saiba mais](doc/save.md)

#### list

Para listar os ambientes salvos, utilize o comando `list`.

``` shell
$ envm list
```

Para listar as propriedades de um ambiente, especifique a tag após o comando `list`.

``` shell
# Exemplo
$ envm list development
```

> **Opções:** --help, --current, --target

[Saiba mais](doc/list.md)

#### checkout

Para trocar entre os ambientes, utilize o comando `checkout` seguido da tag do ambiente.

``` shell
$ envm checkout <tag>
```

> **Opções:** --help, --force

[Saiba mais](doc/checkout.md)

#### delete

Para deletar um ou mais ambientes, utilize o comando `delete` seguido das tags dos ambientes separadas por espaço.

``` shell
$ envm delete <tags>
```

> **Opções:** --help, --all

[Saiba mais](doc/delete.md)