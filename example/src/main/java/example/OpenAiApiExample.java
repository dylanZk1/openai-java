package example;

import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.image.CreateImageRequest;

import java.net.InetSocketAddress;
import java.net.Proxy;

class OpenAiApiExample {
    public static void main(String... args) {
        /*
          get OPENAI_TOKEN from environment variables or any other ways
         */
        String token = System.getenv("OPENAI_TOKEN");

        String yourIP = ""; // proxy ip
        int Port = 7890;  // proxy port

        Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(yourIP, Port));
        OpenAiService service = new OpenAiService(token);
        // OpenAiService service1 = new OpenAiService(token,proxy);   // your proxy setting

        System.out.println("\nCreating completion...");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("ada")
                .prompt("Somebody once told me the world is gonna roll me")
                .echo(true)
                .user("testing")
                .n(3)
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);

        System.out.println("\nCreating Image...");
        CreateImageRequest request = CreateImageRequest.builder()
                .prompt("A cow breakdancing with a turtle")
                .build();

        System.out.println("\nImage is located at:");
        System.out.println(service.createImage(request).getData().get(0).getUrl());
    }
}
