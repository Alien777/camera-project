package pl.lasota.crm.client;

import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.lasota.crm.client.executable.device.Camera;
import pl.lasota.crm.client.task.DevicesDiscover;
import pl.lasota.crm.client.task.FactoryWrapperHandler;
import pl.lasota.crm.client.task.TaskExecutor;
import pl.lasota.crm.common.Auth;
import pl.lasota.crm.common.MessageDto;
import pl.lasota.crm.common.endpoint.clienttomanage.Credential;
import reactor.util.retry.Retry;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;


@SpringBootApplication
@Slf4j
@EnableScheduling
public class ClientApi {

    private final ReceiverController receiverController;

    private final RSocketStrategies rSocketStrategies;

    @Value("${clientName}")
    private String clientName;

    @Value("${userId}")
    private UUID user;

    @Value("${password}")
    private String password;

    @Value("${ip.receiver}")
    private String apiIp;

    @Value("${port.receiver}")
    private int apiPort;

    @Value("${ip.sender}")
    private String videoIp;

    @Value("${port.sender}")
    private int videoPort;

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ClientApi.class, args);
        Thread.currentThread().join();
    }

    public ClientApi(ReceiverController receiverController, RSocketStrategies rSocketStrategies) {
        this.receiverController = receiverController;
        this.rSocketStrategies = rSocketStrategies;
    }

    public void apiRequest(ReceiverController receiverController) {
        Auth auth = new Auth();
        auth.setUserId(user);
        auth.setClientId(clientName);
        MessageDto<String> stringMessageDto = new MessageDto<>("APPLICATION", auth);
        Objects.requireNonNull(RSocketRequester.builder()
                .rsocketConnector(connector ->
                        connector.acceptor(RSocketMessageHandler.responder(rSocketStrategies, receiverController))
                                .payloadDecoder(PayloadDecoder.ZERO_COPY)
                                .reconnect(Retry.fixedDelay(100, Duration.ofSeconds(2)))
                )
                .rsocketStrategies(rSocketStrategies)
                .setupRoute(Credential.CLIENT_LOGIN_TO_MANAGER)
                .setupData(stringMessageDto)
                .connect(WebsocketClientTransport.create(apiIp, apiPort))
                .subscribe(rSocketRequester -> rSocketRequester.route("is.health")
                        .retrieveFlux(Long.class)
                        .doOnError(throwable -> apiRequest(receiverController)).subscribe()));

    }

    @PostConstruct
    public void init() {
        apiRequest(receiverController);
//        RSocketRequester rSocketRequester = RSocketRequester.builder()
//                .rsocketStrategies(rSocketStrategies)
//                .setupRoute(Credential.CLIENT_LOGIN_TO_MANAGER)
//                .setupData(new Credential(clientName, user, password, clientId))
//                .connect(WebsocketClientTransport.create(videoIp, videoPort)).block();
//
        TaskExecutor taskExecutor = new TaskExecutor((o, message) -> {
            o.setUserId(user);
//            rSocketRequester.route(message).data(o).send().subscribe();
        });

        FactoryWrapperHandler factoryWrapperHandler = new FactoryWrapperHandler(taskExecutor, new DevicesDiscover(Camera.class));
        receiverController.setPerformable(factoryWrapperHandler);
    }
}
