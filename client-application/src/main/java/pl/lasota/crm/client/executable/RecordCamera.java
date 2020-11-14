package pl.lasota.crm.client.executable;


import com.github.sarxos.webcam.WebcamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.lasota.crm.client.ReceiverEvent;
import pl.lasota.crm.client.executable.device.function.Recordable;
import pl.lasota.crm.client.task.TaskExecutor;
import pl.lasota.crm.common.endpoint.clienttomanage.ScreenshotCamera;
import pl.lasota.crm.common.endpoint.clienttomanage.TypeScreenShot;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.task.ScreenshotCameraConfig;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class RecordCamera implements Executable {

    private static final Logger logger = LoggerFactory.getLogger(TaskExecutor.class);
    public static final String FORMAT_IMAGE = "jpeg";
    private final UUID taskId;
    private final ScreenshotCameraConfig screenshotCameraConfig;
    private final Recordable recordable;
    private final ReceiverEvent receiverEvent;
    private final ScheduledExecutorService timer = Executors.newScheduledThreadPool(2);
    private long fps;
    private int width;
    private int height;
    private LocalDateTime startRecord;

    public RecordCamera(UUID taskId, ScreenshotCameraConfig screenshotCameraConfig, Recordable recordable, ReceiverEvent receiverEvent) {
        this.taskId = taskId;
        this.screenshotCameraConfig = screenshotCameraConfig;
        this.recordable = recordable;
        this.receiverEvent = receiverEvent;
    }

    @Override
    public void start() throws WebcamException {
        recordable.open(screenshotCameraConfig.getCameraOriginName());

        for (int i = 0; i < 20; i++) {
            recordable.onScreenShot();
            fps = Math.round(recordable.fps());
        }
        width = recordable.width();
        height = recordable.height();
        startRecord = LocalDateTime.now();
        ScreenshotCamera screenshotCamera = new ScreenshotCamera(taskId, startRecord, startRecord, width, height, fps, TypeScreenShot.START);
        receiverEvent.event(screenshotCamera, ScreenshotCamera.CLIENT_RECORD_VIDEO_TO_MANAGER);
        record();
        logger.info("Start recording camera: {}, fps: {}", taskId, fps);
    }

    @Override
    public void stop() throws WebcamException {
        ScreenshotCamera screenshotCamera = new ScreenshotCamera(taskId, LocalDateTime.now(), startRecord, TypeScreenShot.STOP);
        receiverEvent.event(screenshotCamera, ScreenshotCamera.CLIENT_RECORD_VIDEO_TO_MANAGER);
        recordable.close();
        timer.shutdown();
        logger.info("Stop recording camera: {}", taskId);
    }

    private void record() throws WebcamException {
        timer.scheduleWithFixedDelay(new TimerTask() {
            @Override
            public void run() {

                byte[] convert;
                try {
                    convert = convert(recordable.onScreenShot());
                } catch (IOException e) {
                    ScreenshotCamera screenshotCamera = new ScreenshotCamera(taskId, LocalDateTime.now(), startRecord, TypeScreenShot.ERROR);
                    receiverEvent.event(screenshotCamera, ScreenshotCamera.CLIENT_RECORD_VIDEO_TO_MANAGER);

                    logger.error("Problem with send convert byte for camera {}", taskId);
                    stop();
                    return;
                }
                try {
                    ScreenshotCamera screenshotCamera = new ScreenshotCamera(taskId,
                            convert, LocalDateTime.now(), startRecord, width, height, fps, TypeScreenShot.RECEIVER);
                    receiverEvent.event(screenshotCamera, ScreenshotCamera.CLIENT_RECORD_VIDEO_TO_MANAGER);
                } catch (Exception e) {
                    logger.info("Problem with send message", e);
                }
            }
        }, 0, 20, TimeUnit.MILLISECONDS);

    }

    private byte[] convert(BufferedImage image) throws IOException {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        ImageIO.write(image, FORMAT_IMAGE, bas);
        return bas.toByteArray();
    }

}
