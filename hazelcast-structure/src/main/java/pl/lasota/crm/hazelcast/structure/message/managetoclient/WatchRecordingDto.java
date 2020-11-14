package pl.lasota.crm.hazelcast.structure.message.managetoclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WatchRecordingDto {
    private String clientId;
    private UUID taskId;
    private String cameraId;
}
