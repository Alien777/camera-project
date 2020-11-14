package pl.lasota.crm.hazelcast.structure.message.managetoclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.task.ScreenshotCameraConfig;


import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StartRecordingDto {
    private String clientId;
    private ScreenshotCameraConfig config;
}
