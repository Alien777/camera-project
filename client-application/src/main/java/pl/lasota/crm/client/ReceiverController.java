package pl.lasota.crm.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;
import pl.lasota.crm.client.task.Performable;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.LoginStatus;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.task.Endpoint;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.task.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
public class ReceiverController {

    private Performable performable;

    public void setPerformable(Performable performable) {
        this.performable = performable;
    }

    @MessageMapping(LoginStatus.MANAGER_LOGIN_STATUS_TO_CLIENT)
    public void loginStatus(@Payload LoginStatus status) {
        log.info("Client is connected {}", status);

    }

    @MessageMapping(Task.MANAGER_START_TASK_TO_CLIENT)
    public void start(@Payload Task task) {
        performable.start(task);
    }

    @MessageMapping("watch.real")
    public void watch(@Payload Task task) {
        performable.start(task);
    }

    @MessageMapping(Task.MANAGER_STOP_TASK_TO_CLIENT)
    public void stop(@Payload UUID id) {
        performable.stop(id);
    }

    @MessageMapping(Endpoint.MANAGER_AVAILABLE_DEVICES_TO_CLIENT)
    public Mono<List<String>> availableDevice() {
        return Mono.just(performable.availableDevices());
    }

}
