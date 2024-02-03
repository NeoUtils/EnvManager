# Comando `envm`

É o ponto central do **EnvManager**`, permitindo o acesso a todos os outros comandos disponíveis.

```shell
$ envm [opções] <comando> [argumentos do comando]
```

## Opções

### `--path` ou `-p`
Especifica o caminho do projeto onde o **EnvManager** está ou será instalado. O diretório atual é usado por padrão.

```shell
$ envm --path=/projects/MyProject <comando>
```

### `--version` ou `-v`
Mostra a versão atual do **EnvManager**.

```shell
$ envm --version
```

### `--show-config` ou `-c`
Exibe as informações do arquivo de configuração do **EnvManager**.

```shell
$ envm --show-config
```
