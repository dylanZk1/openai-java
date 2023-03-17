[原项目](https://github.com/TheoKanning/openai-java/)

![Maven Central](https://img.shields.io/maven-central/v/com.theokanning.openai-gpt3-java/client?color=blue)

> ⚠️ 注意！'client'模块已于0.10.0版本废弃，若从0.10.0 开始继续使用，请换为'service'模块  
> ⚠️OpenAI已弃用所有基于引擎的API. 详情见 [Deprecated Endpoints](https://github.com/TheoKanning/openai-java#deprecated-endpoints) 

# OpenAI-Java
适配 chatGPT3 API 的 java 版代码

共包含如下 2 个核心模块:
- `api` : 以 POJO （可以理解为 javaBean）的形式定义了 chatGPT3 API 中涉及到的参数
- `service` : （最新版本适用）调用 api 模块中定义的参数，并构建 retrofit 以请求并接收 chatGPT

同样，example 模块中定义了请求示例

## 适配到的 chatGPT API
- [GPT模型](https://platform.openai.com/docs/api-reference/models)
- [Completions(文本补全)](https://platform.openai.com/docs/api-reference/completions)
- [Chat Completions(会话补全)](https://platform.openai.com/docs/api-reference/chat/create)
- [Edits(将输入内容原封不动返回)](https://platform.openai.com/docs/api-reference/edits)
- [Embeddings(展示模型生成的每个文本对应的关联值)](https://platform.openai.com/docs/api-reference/embeddings)
- [Files(文件分析)](https://platform.openai.com/docs/api-reference/files)
- [Fine-tunes](https://platform.openai.com/docs/api-reference/fine-tunes)
- [Images](https://platform.openai.com/docs/api-reference/images)
- [Moderations](https://platform.openai.com/docs/api-reference/moderations)

#### 已废除的 API
- [Engines](https://platform.openai.com/docs/api-reference/engines)

## 导入项目

### Gradle版本
`implementation 'com.theokanning.openai-gpt3-java:<api|client|service>:<version>'`

### Maven版本
```xml
   <dependency>
    <groupId>com.theokanning.openai-gpt3-java</groupId>
    <artifactId>{api|client|service}</artifactId>
    <version>version</version>       
   </dependency>
```

## 用法
### 数据类
如想自定义请求client，可自行修改 api 模块中的 POJO字段
建议字段命名方式使用驼峰命名法，以便于适配 chatGPT 官方 API 的命名方式

### Retrofit 客户端
Service模块中定义了 [OpenAiApi](service/src/main/java/com/theokanning/openai/OpenAiApi.java)已适配 Retrofit.  
(see [AuthenticationInterceptor](service/src/main/java/com/theokanning/openai/AuthenticationInterceptor.java))中传入了 secret KEY 字段，
你需要在自己的项目中new一个内部定义的 OpenAiService 类并传入

### OpenAiService
[OpenAiService](service/src/main/java/com/theokanning/openai/service/OpenAiService.java) 封装了简易版的 API 调用类

> ⚠️注意！'client'模块中的 openAiService 已于0.10.0版本废弃，若从0.10.0 开始继续使用，请换为'service'模块  
```java
OpenAiService service = new OpenAiService("你的secret Key");
CompletionRequest completionRequest = CompletionRequest.builder()
        .prompt("Somebody once told me the world is gonna roll me")
        .model("ada")
        .echo(true)
        .build();
service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
```

### 自定义 OpenAiService
若想自定义 OpenAIService，请自定义 Retrofit 并传入相应的构造器
如下为实例代码（别忘了在gradle中添加依赖）

```java
ObjectMapper mapper = defaultObjectMapper();
OkHttpClient client = defaultClient(token, timeout)
        .newBuilder()
        .interceptor(HttpLoggingInterceptor())
        .build();
Retrofit retrofit = defaultRetrofit(client, mapper);

OpenAiApi api = retrofit.create(OpenAiApi.class);
OpenAiService service = new OpenAiService(api);
```

### 添加代理
如下代码实现了向 OkhttpClient 对象中创建Proxy的过程
```java
ObjectMapper mapper = defaultObjectMapper();
Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
OkHttpClient client = defaultClient(token, timeout)
        .newBuilder()
        .proxy(proxy)
        .build();
Retrofit retrofit = defaultRetrofit(client, mapper);
OpenAiApi api = retrofit.create(OpenAiApi.class);
OpenAiService service = new OpenAiService(api);
```



## 运行示例
[示例](example/src/main/java/example/OpenAiApiExample.java) 代码涉及到的token部分均需填入自己的secret Key
```bash
export OPENAI_TOKEN="sk-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
./gradlew example:run
```
你也可以把项目直接导入到 idea 或 eclipse

## 已废除的接口
有几个引擎接口已经废除
比如, 原先为 `v1/engines/{engine_id}/completions`, 现在为 `v1/completions` and specify the model in the `CompletionRequest`.
所有废除的接口都会在代码中以 @deprecated 注解表明

这些被废除的接口会在官方正式关闭后删除对应实现接口，在此之间你可以进行调用，但不保证能正常调用
（本项目未删除 client 模块），但已经删除其依赖，若想添加此模块依赖，请自行修改 gradle 依赖配置

## 协议
本项目遵循 MIT 协议
