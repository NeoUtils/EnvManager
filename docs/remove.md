# `remove` command

Use the `remove` command to delete properties specified by their keys.

**To delete a single property:**

```shell
$ envm remove <key> [options]
```

**To delete multiple properties:**

```shell
$ envm remove <key1> <key2> ... [options]
```

## Options

### `--tag` or `-t`
Specifies the target environment. In the absence of this option, the current environment is used.

```shell
$ envm remove <keys> --tag=<environment tag>
```

### `--all` or `-a`
This option has two behaviors, depending on the presence of keys:

**With specified keys:** <br>
Removes the provided keys from all saved environments.

  ```shell
  $ envm remove <keys> --all
  ```

**Without specified keys:** <br>
Deletes all properties from the current environment (or specified via `--tag`).

  ```shell
  $ envm remove --all
  ```

### `--target-only` or `-o`
Restricts the removal to keys in the target properties file (**target**) only, without affecting the saved environments.

```shell
$ envm remove <keys> --target-only
```