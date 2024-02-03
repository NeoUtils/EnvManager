# Comando `save`

Use o comando `save` para savar as propriedades do arquivo de propriedades alvo (**target**) no ambiente atual ou em um ambiente especificado após o comando.

**Para salvar no ambiente atual:**

```shell
$ envm save [opções]
```

**Para salvar em um ambiente específico:**

```shell
$ envm save <tag> [opções]
```

## Opções

### `--clipboard` ou `-c`
Captura as propriedades da área de transferência em vez de usar o arquivo **target**.

```shell
$ envm save --clipboard
```