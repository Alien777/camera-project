package pl.lasota.crm.client.task;

import pl.lasota.crm.hazelcast.structure.message.managetoclient.task.Task;

import java.util.List;
import java.util.UUID;

public interface Performable {
    void start(Task task);

    void stop(UUID id);

    List<String> availableDevices();

    List<Task> getTasks();
}
