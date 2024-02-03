# Comando `set`

Use para adicionar ou modificar propriedades `set`. Você deve fornecer as propriedades no formato `chave=valor`, separadas por espaço.

**Para definir uma única propriedade:**

```shell
$ envm set KEY1=VALUE [opções]
```

**Para definir múltiplas propriedades simultaneamente:**

```shell
$ envm set KEY1=VALUE1 KEY2=VALUE2 ... [opções]
```

## Opções

### `--tag` ou `-t`
Permite especificar o ambiente alvo para as propriedades.  Na ausência desta opção, o ambiente atual é utilizado.

```shell
$ envm set <properties> --tag=<tag do ambiente>
```

### `--all` ou `-a`
Aplica as propriedades fornecidas a todos os ambientes salvos, facilitando a sincronização de configurações entre diferentes contextos.

```shell
$ envm set <properties> --all
```

### `--target-only` ou `-o`
Limita a adição ou modificação de propriedades ao arquivo de propriedades alvo (**target**), sem afetar as configurações dos ambientes salvos. Isso é útil para modificações temporárias.

```shell
$ envm set <properties> --target-only
```