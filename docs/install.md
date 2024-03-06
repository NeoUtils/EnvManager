# `install` command

Use the `install` command to initialize **EnvManager** in a directory. Necessary to use the other commands.

```shell
$ envm install [options]
```

## Options

### `--target=` or `-t`

Sets the path of the target properties file (`target`). If not specified, **EnvManager** will prompt for it during installation.

```shell
$ envm install --target=environment.properties
```

or

```shell
$ envm install -t environment.properties
```

### `--force` or `-f`

Forces the installation of **EnvManager**, even if a configuration file is already present in the directory. This option is useful for solving installation issues.

```shell
$ envm install --force
```
