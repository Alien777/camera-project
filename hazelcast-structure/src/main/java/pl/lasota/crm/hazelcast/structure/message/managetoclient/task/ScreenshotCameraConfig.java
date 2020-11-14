package pl.lasota.crm.hazelcast.structure.message.managetoclient.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreenshotCameraConfig implements Config {
    private String cameraOriginName;
    private String cameraName;
}
