# `list` command

Use the `list` command to display saved environments or the properties of a specific environment.

**To list all saved environments:**

```shell
$ envm list
```

**To list the properties of a specific environment:**

```shell
$ envm list <tag>
```

## Options

### `--current` or `-c`
Displays the properties of the current environment, making it easier to quickly view the configuration in use.

```shell
$ envm list --current
```

### `--target` or `-t`
Displays the properties of the target properties file (**target**).

```shell
$ envm list --target
```