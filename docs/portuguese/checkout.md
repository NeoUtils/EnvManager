# Comando `checkout`

Use o comando `checkout` para alternar entre os ambientes.

```shell
$ envm checkout <ambiente>
```

Também pode ser usado para para criar um ambientes vazio utilizando a opção `-force`.

```shell
$ envm checkout <ambiente> -force
```

## Opção

### `-force` ou `-f`

Força o checkout, criando um ambiente se não existir.

```shell
$ envm checkout <ambiente> -force
```