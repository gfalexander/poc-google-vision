## O que é
O Cloud Vision é uma ferramenta OCR desenvolvida pela Google que utiliza a [Vertex AI](https://cloud.google.com/vertex-ai?hl=pt-br) (ferramenta de escalonamento de Machine Learning) para criar apps/apis que coletam dados/insigths de imagens ou videos.
## Custo
| Recurso                                         | Primeiras 1000 unidades/mês | 1.001 - 5.000.000 unidades/mês                   | 5.000.001 unidades/mês ou um número maior                                                        |
|:----------------------------------------------- | --------------------------- | ------------------------------------------------ | ------------------------------------------------------------------------------------------------ |
| Detecção de rótulos                             | Grátis                      | US$ 1,50                                         | US$ 1,00                                                                                         |
| Detecção de texto                               | Grátis                      | US$ 1,50                                         | US$ 0,60                                                                                         |
| Detecção de texto de documentos                 | Grátis                      | US$ 1,50                                         | US$ 0,60                                                                                         |
| Detecção do SafeSearch (conteúdo explícito)     | Grátis                      | Gratuito com Detecção de rótulos ou US$ 1,50     | Gratuito com Detecção de rótulos ou US$ 0,60                                                     |
| Detecção facial                                 | Grátis                      | US$ 1,50                                         | US$ 0,60                                                                                         |
| Detecção facial: reconhecimento de celebridades | Grátis                      | US$ 1,50                                         | US$ 0,60                                                                                         |
| Detecção de pontos de referência                | Grátis                      | US$ 1,50                                         | US$ 0,60                                                                                         |
| Detecção de logotipos                           | Grátis                      | US$ 1,50                                         | US$ 0,60                                                                                         |
| Propriedades de imagens                         | Grátis                      | US$ 1,50                                         | US$ 0,60                                                                                         |
| Dicas de corte                                  | Grátis                      | Gratuito com propriedades de imagens ou US$ 1,50 | Gratuito com propriedades de imagens ou US$ 0,60                                                 |
| Detecção na Web                                 | Grátis                      | US$ 3,50                                         | [Entre em contato com o Google para mais informações](https://cloud.google.com/contact?hl=pt-br) |
| Localização de objetos                          | Grátis                      | US$ 2,25                                         | US$ 1,50                                                                                         |

## Configurando GVision
### Plataforma
Inicialmente na plataforma é necessário criar um projeto, os projetos possuem acesso a todas os recursos da google que por padrão vêm desativados, nesse caso iremos habilitar o Google Vision AI. Antes disso é necessário atribuir uma conta de faturamento para o projeto, assim que este passo for concluído podemos habilitar o serviço.
Após ser habilitado será necessária a criação de uma conta de serviço para autenticação do cliente que irá utiliza-lo, para âmbito de testes iremos utilizar o nível de proprietário, mas futuramente isso deverá ser revisado e alterado para um permissionamento mais adequado ao projeto e a conta.
Ao final da criação irá aparecer uma mensagem solicitando a escolha do tipo de arquivo que será criado com as informações da chave, por ser um projeto recente iremos com a recomendação da Google (JSON). Após o download da chave iremos referenciar a chave baixada no env do sistema operacional com um dos seguintes comandos:
*Para linux ou MacOS*
export GOOGLE_APPLICATION_CREDENTIALS="`KEY_PATH`"
*Para Windows com powershell*
$env:GOOGLE_APPLICATION_CREDENTIALS="`KEY_PATH`"
Para windows com prompt de comando
set GOOGLE_APPLICATION_CREDENTIALS=`KEY_PATH`
**Substitua `KEY_PATH` pelo caminho do arquivo JSON que contém a chave da conta de serviço**.

### Projeto
Nosso projeto será configurado em Java 11 com maven, então no pom irá conter a seguinte depêndencia:
```
<dependencyManagement>
	<dependencies>
	    <dependency>
	          <groupId>com.google.cloud</groupId>
	          <artifactId>libraries-bom</artifactId>
	          <version>26.1.3</version>     
	          <type>pom</type>      
	          <scope>import</scope>
	    </dependency>  
	</dependencies>
</dependencyManagement>

<dependencies>
	<dependency>
	<groupId>com.google.cloud</groupId>    
	<artifactId>google-cloud-vision</artifactId>  
	</dependency>
</dependencies>
```

