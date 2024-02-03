# `envm` command

It's the central point of **EnvManager**, allowing access to all other available commands.

```shell
$ envm [options] <command> [command arguments]
```

## Options

### `--path` or `-p`
Specifies the project path where **EnvManager** is or will be installed. The current directory is used by default.

```shell
$ envm --path=/projects/MyProject <command>
```

### `--version` or `-v`
Shows the current version of **EnvManager**.

```shell
$ envm --version
```

### `--show-config` or `-c`
Displays the configuration file information of **EnvManager**.

```shell
$ envm --show-config
```
