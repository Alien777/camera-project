package pl.lasota.crm.hazelcast.structure.message.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.task.Task;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StartRecording implements Serializable {
   private Task task;
}
