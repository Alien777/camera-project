package pl.lasota.crm.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenshotCameraConfigDto {
    private String cameraOriginName;
    private String cameraName;
    private UUID clientId;
}
