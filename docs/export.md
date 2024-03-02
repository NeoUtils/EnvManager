# `export` command

Use the `export` command to export environments to a `.envm` file.

**Export all environments:**

```shell
$ envm export
```

**Export specific tags:**

```shell
$ envm export <tags>
```
> **Note:** You can export multiple environments by separating the tags with a space.

## Options

### `--output`

Specify the output directory of the environments file; if not specified, will be prompt for it during export.

```shell
$ envm export --output=<output-directory>
```