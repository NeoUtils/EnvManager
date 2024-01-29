# Comando `remove`

Utilize `remove` para excluir propriedades especificadas pelas suas chaves.

**Para excluir uma única propriedade:**

```shell
$ envm remove <key> [opções]
```

**Para excluir múltiplas propriedades:**

```shell
$ envm remove <key1> <key2> ... [opções]
```

## Opções

### `--tag` ou `-t`
Especifica o ambiente alvo. Na ausência desta opção, o ambiente atual é utilizado.

```shell
$ envm remove <keys> --tag=<tag do ambiente>
```

### `--all` ou `-a`
Esta opção tem dois comportamentos, dependendo da presença de chaves:

**Com chaves especificadas:** <br>
Remove as chaves fornecidas de todos os ambientes salvos.

  ```shell
  $ envm remove <keys> --all
  ```

**Sem chaves especificadas:** <br>
Exclui todas as propriedades do ambiente atual (ou especificado via `--tag`).

  ```shell
  $ envm remove --all
  ```

### `--target-only` ou `-o`
Restringe a remoção às chaves apenas ao arquivo de propriedades alvo (**target**), sem impactar os ambientes salvos.

```shell
$ envm remove <keys> --target-only
```