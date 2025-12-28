# Prática com contêineres e sockets TCP/IP em Java

- Projeto de gerenciamento e atualização de dispositivos IoT desenvolvido em Java, utilizando comunicação em rede via TCP e Multicast.

- O sistema é composto por dois módulos principais:

- `Dispositivo IoT`: representa os dispositivos conectados à rede. Cada dispositivo possui uma versão de software e escuta anúncios multicast do gerenciador. Caso receba uma solicitação de atualização e sua versão seja inferior à do gerenciador, realiza a atualização via conexão TCP. Se o dispositivo já estiver em uma versão mais recente, a atualização é ignorada. Quando não é informada uma versão inicial, o dispositivo utiliza o último octeto do seu endereço IP como versão.

- `Gerenciador de Atualizações`: responsável por detectar dispositivos IoT ativos na rede, manter o controle das versões e coordenar o processo de atualização. O gerenciador envia anúncios via multicast e estabelece conexões TCP para atualizar dispositivos quando necessário, além de fornecer uma interface de comandos para o usuário.

## Execução do cenário de teste
O cenário foi montado com 3 dispositivos IoT e 1 gerenciador.

Clonar o projeto e acessar o diretório raíz:
```bash
git clone https://github.com/STD29006-classroom/2025-02-lista-01-luizakuze
cd 2025-02-lista-01-luizakuze
```

Executar os comandos abaixo:
```bash
# Construir as imagens
docker compose build
# Iniciar os 3 dispositivos com versões aleatórias 
docker compose up --scale dispositivo-iot=3 dispositivo-iot
# Em outro terminal, iniciar a interação com o gerenciador
docker compose run --rm gerenciador
```

## Demonstração

### Lista de dispositivos IoT conectados
O gerenciador mantém uma lista atualizada dos dispositivos ativos de acordo com as mensagens via Multicast.

![Lista de dispositivos conectados](imagens/iots-conectados.png)


### Incrementando a versão do gerenciador
O gerenciador inicia em `v1`. A cada ação, a versão é incrementada:

> Exemplo: incrementado 2 vezes e está em `v3`

![Incrementando versão](imagens/incrementando-gerenciador.png)


### Atualizando dispositivos IoT

Existem alguns cenários possíveis durante a atualização:

| Versão do dispositivo IoT | Comparação com gerenciador | Ação tomada |
|---------------------------|---------------------------|--------------|
| >= versão do gerenciador | Atualizado        | Não atualiza |
| < versão do gerenciador e ativo | Desatualizado | Atualiza via TCP |
| < versão do gerenciador e inativo | Desatualizado |  Remove da lista |

Exemplo: o gerenciador está em `v3`, e entre os dispositivos, apenas o `v2` foi atualizado para `v3`.

![Atualizando dispositivo](imagens/atualizando-dispositivo.png)

> Note que o gerenciador enviou atualização *apenas para o dispositivo conectado via TCP e ativo* (à direita).


### Remoção de dispositivo IoT inativo

Se um dispositivo é parado (contêiner encerrado), o gerenciador ainda tenta a atualização via TCP.
Não obtendo resposta, **o dispositivo é automaticamente removido** da lista.

Primeiro parando o contêiner:
![Parando contêiner](imagens/parando-container.png)

O cenário atual é: gerenciador em `v5`, apenas dois dispositivos continuam ativos.

![Cenário antes da atualização](imagens/cenario.png)

Resultado após tentar atualizar todos:

![Removendo dispositivo](imagens/removendo.png)

> O dispositivo desconectado não respondeu e foi corretamente removido.