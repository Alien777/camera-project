package pl.lasota.crm.server.recorder.function;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import pl.lasota.crm.common.MessageDto;
import pl.lasota.crm.common.endpoint.clienttomanage.ScreenshotCamera;
import pl.lasota.crm.hazelcast.structure.IdentifierClientApp;
import pl.lasota.crm.hazelcast.structure.message.User;
import pl.lasota.crm.server.recorder.function.video.Frame;
import pl.lasota.crm.server.recorder.function.video.Watcher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

@Controller
public class ReceiverImagesController {
    private final Map<User<Object>, RSocketRequester> clients = new ConcurrentHashMap<>();


    @MessageMapping("watch")
    public Flux<Frame> video(@Payload String f) throws IOException {
        return new Watcher(Paths.get("/home/adam/Documents/videotest")).watch();
    }

    @MessageMapping("watch.real")
    public Flux<Frame> widioWatchTemp(@Payload String f) throws IOException {
        return null;
    }

}
