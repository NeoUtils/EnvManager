# `set` Command

Use to add or modify properties with `set`. You must provide the properties in the `key=value` format, separated by space.

**To set a single property:**

```shell
$ envm set KEY1=VALUE [options]
```

**To set multiple properties simultaneously:**

```shell
$ envm set KEY1=VALUE1 KEY2=VALUE2 ... [options]
```

## Options

### `--tag=` or `-t`

Allows specifying the target environment for the properties. In the absence of this option, the current environment is used.

```shell
$ envm set <properties> --tag=<environment-tag>
```

or

```shell
$ envm set <properties> -t <environment-tag>
```

### `--all` or `-a`

Applies the provided properties to all saved environments, making it easier to synchronize settings across different contexts.

```shell
$ envm set <properties> --all
```

### `--target-only` or `-o`

Limits the addition or modification of properties to the target properties file (**target**), without affecting the settings of saved environments. This is useful for temporary modifications.

```shell
$ envm set <properties> --target-only
```
