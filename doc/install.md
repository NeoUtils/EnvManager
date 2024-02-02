# Comando `install`

Use o comando `install` para inicializar o **EnvManager** em um diretório. Necessário para usar os demais comandos.

```shell
$ envm install [opções]
```

## Opções

### `--target` ou `-t`

Define o caminho do arquivo de propriedades alvo (`target`). Se não especificado, o **EnvManager** solicitará durante a instalação.

```shell
$ envm install --target=environment.properties
```

### `--force` ou `-f`

Força a instalação do **EnvManager**, mesmo que um arquivo de configuração já esteja presente no diretório. Essa opção é útil para resolver problemas na instalação.

```shell
$ envm install --force
```