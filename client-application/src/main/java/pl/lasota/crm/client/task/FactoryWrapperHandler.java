package pl.lasota.crm.client.task;

import lombok.extern.slf4j.Slf4j;
import pl.lasota.crm.client.executable.Executable;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class FactoryWrapperHandler implements Performable {
    private final ConcurrentHashMap<Task, Executable> executables = new ConcurrentHashMap<>();

    private final TaskExecutor taskExecutor;
    private final DevicesDiscover devicesDiscover;

    public FactoryWrapperHandler(TaskExecutor taskExecutor, DevicesDiscover devicesDiscover) {
        this.taskExecutor = taskExecutor;
        this.devicesDiscover = devicesDiscover;
    }

    @Override
    public void start(Task task) {
        try {
            executables.put(task, taskExecutor.start(task));
        } catch (RuntimeException e) {
            log.info("Problem with start task: {}", task.getTaskId(), e);
        }
    }

    @Override
    public void stop(UUID id) {
        try {
            Executable executable = executables.get(new Task(id));
            taskExecutor.stop(executable);
        } catch (RuntimeException e) {
            log.info("Problem with start task: {} ", id, e);
        }
    }

    @Override
    public List<String> availableDevices() {
        return devicesDiscover.start();
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(executables.keySet());
    }
}
