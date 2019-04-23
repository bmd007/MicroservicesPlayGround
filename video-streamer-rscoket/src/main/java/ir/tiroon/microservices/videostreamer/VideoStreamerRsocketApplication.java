package ir.tiroon.microservices.videostreamer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class VideoStreamerRsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoStreamerRsocketApplication.class, args);
    }

}

@Controller
class VideoStreamerController {

    @MessageMapping("xxx")
    public Flux<Byte> bytesOfXXX(Mono<String> username) {
        return username.filter(s -> s.equals("bmd"))
                .switchIfEmpty(Mono.error(new RuntimeException("Not Found")))
                .flatMapMany();
    }
}
//**TODO
// Read a video file bytes (all in a byteArray or reactive ly in a byteArray)
// Create a RSocket Responder of Flux<Byte>
// send the file bytes as response in the Responder
// maybe it become needed to use RSocket over WebSocket
//----------------------------------------
// Find js RSocket library
// Find js media byte player that supports RSocket or its integratable with RSocket
// **//