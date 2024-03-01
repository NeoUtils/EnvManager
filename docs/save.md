# `save` command

Use the `save` command to save the properties from the target properties file (**target**) in the current environment or in a specified environment after the command.

**To save in the current environment:**

```shell
$ envm save [options]
```

**To save in a specific environment:**

```shell
$ envm save <tag> [options]
```

## Options

### `--clipboard` or `-c`
Captures the properties from the clipboard instead of using the **target** file.

```shell
$ envm save --clipboard
```