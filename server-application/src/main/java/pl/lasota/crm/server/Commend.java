package pl.lasota.crm.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import pl.lasota.crm.common.endpoint.clienttomanage.Credential;
import pl.lasota.crm.common.MessageDto;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.LoginStatus;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.StartRecordingDto;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.StopRecordingDto;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.WatchRecordingDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Consumer;

@Controller
@Slf4j
@CrossOrigin
public class Commend {

    private final FactoryTasks factoryTasks;

    public Commend(FactoryTasks factoryTasks) {
        this.factoryTasks = factoryTasks;
    }


    @ConnectMapping(Credential.CLIENT_LOGIN_TO_MANAGER)
    public void connectClientOther(RSocketRequester requester, @Payload MessageDto<String> client) {
        requester.rsocket()
                .onClose()
                .doFirst(() -> factoryTasks.addClient(client.getAuth().getUserId(), client.getAuth().getClientId(), requester))
                .doFinally(f -> factoryTasks.removeClient(client.getAuth().getUserId(), client.getAuth().getClientId()))
                .subscribe();

        requester.route(LoginStatus.MANAGER_LOGIN_STATUS_TO_CLIENT)
                .data(LoginStatus.ok())
                .send()
                .subscribe();
    }

    @MessageMapping(Credential.CLIENT_LOGIN_TO_MANAGER)
    public void connectClientWeb(RSocketRequester requester, @Payload MessageDto<String> client) {


        requester.rsocket()
                .onClose()
                .doFirst(() -> factoryTasks.addClient(client.getAuth().getUserId(), client.getAuth().getClientId().toString(), requester))
                .doFinally(f -> factoryTasks.removeClient(client.getAuth().getUserId(), client.getAuth().getClientId()))
                .subscribe();
        new ApplicationClientRequester(requester).loginClient(UUID.fromString(client.getAuth().getClientId()));

    }


    @MessageMapping("is.health")
    public Flux<Long> login() {
        return Flux.interval(Duration.ofSeconds(1));
    }

    @MessageMapping("api-plugin.available.clients")
    public void availableClients(@Payload MessageDto<String> message) {
        factoryTasks.availableClients(message);
    }


    @MessageMapping("api-plugin.available.devices")
    public void availableDevices(@Payload MessageDto<String> messageDto) {
        factoryTasks.availableDevices(messageDto);
    }


    @MessageMapping("api-plugin.watch.video")
    public void watchRealVideo(MessageDto<WatchRecordingDto> dtoMessage) {
        factoryTasks.watchCamera(dtoMessage);
    }

    @MessageMapping("api-plugin.stop.record")
    public void stopRecordingVideo(MessageDto<StopRecordingDto> dtoMessage) {
        factoryTasks.stopRecordingVideo(dtoMessage);
    }

    @MessageMapping(value = "api-plugin.start.record")
    public void start(@Payload MessageDto<StartRecordingDto> messageDto) {
        factoryTasks.startRecordingVideo(messageDto);
    }

}


