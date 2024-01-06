# Properties

Easily control your environment properties.

## Purpose

Many projects use environment property files and need to manually swap these properties when testing in different environments. This project aims to facilitate the management of these environment properties.

## Proposal

Inspired a bit by Git, I thought of the following commands: [install](#install), [save](#save), [checkout](#checkout), [list](#list), and [remove](#remove).

### Install

First, you need to install the management in the project folder:

```bash
$ properties install
```

This will create a configuration file and a folder to store the environments.

### Save

With this command, you can save an environment with an associated tag, allowing you to later checkout in it.

```bash
$ properties save <tag>
```

### Checkout

This command allows you to checkout in a saved environment.

```bash
$ properties checkout <tag>
```

### List

This command allows you to list the saved environments.

```bash
$ properties list
```

If you specify a tag, the properties associated with it will be displayed.

```bash
$ properties list <tag>
```

### Remove

This command allows you to remove a saved environment.

```bash
$ properties remove <tag>
```