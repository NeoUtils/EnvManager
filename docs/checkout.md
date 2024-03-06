# `checkout` command

Use the `checkout` command to switch between environments.

```shell
$ envm checkout <environment>
```

It can also be used to create an empty environment using the `--force` option.

```shell
$ envm checkout <environment> --force
```

## Flags

### `--force` or `-f`

Forces the checkout, creating an environment if it does not exist.

```shell
$ envm checkout <environment> --force
```
