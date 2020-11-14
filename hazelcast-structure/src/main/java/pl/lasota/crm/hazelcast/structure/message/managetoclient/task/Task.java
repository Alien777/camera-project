package pl.lasota.crm.hazelcast.structure.message.managetoclient.task;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "taskId")
@NoArgsConstructor
public class Task implements Serializable {

    public static final String MANAGER_START_TASK_TO_CLIENT = "start-task-endpoint";
    public static final String MANAGER_STOP_TASK_TO_CLIENT = "stop-task-endpoint";

    private UUID taskId;
    private Config options;

    public Task(UUID taskId) {
        this.taskId = taskId;
    }

    public Task(UUID taskId, Config options) {
        this.taskId = taskId;
        this.options = options;
    }
}
