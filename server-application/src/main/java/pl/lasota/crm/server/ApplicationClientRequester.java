package pl.lasota.crm.server;

import org.springframework.messaging.rsocket.RSocketRequester;
import pl.lasota.crm.common.endpoint.clienttomanage.ScreenshotCamera;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.LoginStatus;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.task.Endpoint;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.task.Task;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class ApplicationClientRequester {
    private final RSocketRequester requester;

    public ApplicationClientRequester(RSocketRequester requester) {
        this.requester = requester;
    }

    public void stopRecording(UUID taskId, Consumer<Boolean> consumer) {
        requester.route(Task.MANAGER_STOP_TASK_TO_CLIENT)
                .data(taskId)
                .retrieveMono(Boolean.class)
                .subscribe(consumer);
    }

    public void startRecording(Task task, Consumer<Boolean> consumer) {
        requester.route(Task.MANAGER_START_TASK_TO_CLIENT)
                .data(task)
                .retrieveMono(Boolean.class)
                .subscribe(consumer);
    }

    public void availableDevices(Consumer<List> consumer) {
        requester.route(Endpoint.MANAGER_AVAILABLE_DEVICES_TO_CLIENT)
                .retrieveMono(List.class)
                .subscribe(consumer);

    }

    public void watchReal(Consumer<ScreenshotCamera> consumer) {
        requester.route(Endpoint.MANAGER_AVAILABLE_DEVICES_TO_CLIENT)
                .retrieveFlux(ScreenshotCamera.class)
                .subscribe(consumer);

    }

    public void loginClient(UUID clientId) {
        requester.route(LoginStatus.MANAGER_LOGIN_STATUS_TO_CLIENT)
                .data(clientId)
                .send()
                .subscribe();
    }

}
