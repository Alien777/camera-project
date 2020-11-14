package pl.lasota.crm.common.endpoint.clienttomanage;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@ToString(exclude = "bytes")
public class ScreenshotCamera {

    public static final String CLIENT_RECORD_VIDEO_TO_MANAGER = "record-video";

    private UUID taskId;
    private UUID userId;

    private byte[] bytes;
    private LocalDateTime dataTime;
    private LocalDateTime startRecord;
    private int width;
    private int height;
    private long fps;
    private TypeScreenShot typeScreenShot;


    public ScreenshotCamera(UUID taskId, LocalDateTime dataTime, LocalDateTime startRecord, TypeScreenShot typeScreenShot) {
        this.taskId = taskId;
        this.dataTime = dataTime;
        this.typeScreenShot = typeScreenShot;
    }

    public ScreenshotCamera(UUID taskId,
                            byte[] bytes,
                            LocalDateTime dataTime,
                            LocalDateTime startRecord,
                            int width,
                            int height,
                            long fps,
                            TypeScreenShot typeScreenShot) {
        this.taskId = taskId;
        this.bytes = bytes;
        this.dataTime = dataTime;
        this.startRecord = startRecord;
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.typeScreenShot = typeScreenShot;
    }

    public ScreenshotCamera(UUID taskId,
                            LocalDateTime dataTime,
                            LocalDateTime startRecord,
                            int width,
                            int height,
                            long fps,
                            TypeScreenShot typeScreenShot) {
        this.taskId = taskId;
        this.dataTime = dataTime;
        this.startRecord = startRecord;
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.typeScreenShot = typeScreenShot;
    }
}
