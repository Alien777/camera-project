package pl.lasota.crm.server;

import com.hazelcast.internal.json.Json;
import org.springframework.messaging.rsocket.RSocketRequester;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.LoginStatus;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.task.Endpoint;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.task.Task;

import java.util.*;
import java.util.function.Consumer;


public class WebClientRequester {
    private final RSocketRequester requester;

    public WebClientRequester(RSocketRequester requester) {
        this.requester = requester;
    }

    public void stopRecording(Boolean status) {
        requester.route(Task.MANAGER_STOP_TASK_TO_CLIENT)
                .data(status)
                .send()
                .subscribe();
    }

    public void startRecording(Boolean status) {
        requester.route(Task.MANAGER_START_TASK_TO_CLIENT)
                .data(status)
                .send()
                .subscribe();
    }

    public void availableDevices(List devices) {
        requester.route(Endpoint.MANAGER_AVAILABLE_DEVICES_TO_CLIENT)
                .data(devices)
                .send()
                .subscribe();
    }

    public void availableClient(String clientId) {
        LinkedList<String> objects = new LinkedList<>();
        objects.add(clientId);
        requester.route(Endpoint.MANAGER_CLIENT_DEVICES_TO_CLIENT)
                .data(objects)
                .send()
                .subscribe();
    }
}
