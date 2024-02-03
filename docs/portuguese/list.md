# Comando `list`

Use o comando `list` para exibir os ambientes salvos ou as propriedades de um ambiente específico.

**Para listar todos os ambientes salvos:**

```shell
$ envm list
```

**Para listar as propriedades de um ambiente específico:**

```shell
$ envm list <tag>
```

## Opções

### `--current` ou `-c`
Mostra as propriedades do ambiente atual, facilitando a visualização rápida da configuração em uso.

```shell
$ envm list --current
```

### `--target` ou `-t`
Exibe as propriedades do arquivo de propriedades alvo (**target**).

```shell
$ envm list --target
```