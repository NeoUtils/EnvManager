# `delete` command

Use the `delete` command to remove one or more environments.

**Deleting a single environment:**

``` shell
$ envm delete <tag>
```

**Deleting multiple environments:**

``` shell
$ envm delete <tag1> <tag2> ... [flags]
```

## Flags

### `--all` or `-a`

Deletes all environments.

``` shell
$ envm delete --all
```
