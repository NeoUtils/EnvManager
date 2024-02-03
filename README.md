**english** | [português](README-PT.md)

# EnvManager

Easily control your environment properties.

### Installation

To install, download the installation file available at [releases](https://github.com/Irineu333/EnvManager/releases),
extract, and run `install.sh` (Linux) with administrator permissions. For more detailed information, consult
the [installation instructions](src/dist/INSTRUCTIONS.md).

> **Compatibility:** GNU/Linux, Termux

### How It Works

**EnvManager** is ideal for projects with **multiple environments**, where variables are managed by a **property
file** (named **target**) in the `key=value` format. With **EnvManager**, you can switch between
different environments and perform other manipulations **directly from the terminal**.

### Usage

After installation, **EnvManager** is accessed with the `envm` command, followed by a command and its arguments.

``` shell
$ envm [options] <command> [arguments]
```

> **Other options:** --path=<project path>, --version, --show-config

You can get the instructions for any command with the `--help` option.

``` shell
$ envm --help
```

[More information](docs/english/envm.md)

### Essential Commands

These commands cover the main functionalities for daily use.

#### install

Initialize **EnvManager** in your project directory with the `install` command.

``` shell
$ envm install
```

> **Options:** --target=<target path>, --force

[More details](docs/english/install.md)

#### save

Save the current properties as an environment using `save`, followed by the environment name (named **tag**).

``` shell
$ envm save <tag>
```

> **Options:** --clipboard

[More details](docs/english/save.md)

#### list

List the saved environments with the `list` command.

``` shell
$ envm list
```

To list properties of a specific environment, add the **tag** after the command.

``` shell
$ envm list <tag>
```

> **Options:** --current, --target

[More details](docs/english/list.md)

#### checkout

Switch to a different environment with `checkout`.

``` shell
$ envm checkout <tag>
```

> **Options:** --force

[More details](docs/english/checkout.md)

#### delete

Delete one or more environments with `delete`.

``` shell
$ envm delete <tags>
```

> **Options:** --all

[More details](docs/english/delete.md)

### Other Commands

#### set

Add or modify properties with `set`.

``` shell
$ envm set <properties>
```

> **Options:** --tag=<tag>, --all, --target-only

[More details](docs/english/set.md)

#### remove

Remove specific properties with `remove`.

``` shell
$ envm remove <keys>
```

> **Options:** --tag=<tag>, --all, --target-only

[More details](docs/english/remove.md)

#### rename

Rename an environment with `rename`.

``` shell
$ envm rename <old-tag> <new-tag>
```

#### rollback

Revert changes in the **target**, synchronizing with the current environment, using `rollback`.

``` shell
$ envm rollback
```

---
Translated from [README-PT.md](README-PT.md) using [ChatGPT](https://chat.openai.com/share/9d76f100-2955-4e01-b5d9-5dc10ee9b6de)