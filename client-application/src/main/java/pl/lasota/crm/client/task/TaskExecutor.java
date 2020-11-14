package pl.lasota.crm.client.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.lasota.crm.client.ReceiverEvent;
import pl.lasota.crm.client.executable.Executable;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.task.ScreenshotCameraConfig;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.task.Task;
import pl.lasota.crm.client.executable.RecordCamera;
import pl.lasota.crm.client.executable.device.Camera;
import pl.lasota.crm.client.executable.device.function.Recordable;

public class TaskExecutor {
    private static final Logger logger = LoggerFactory.getLogger(TaskExecutor.class);

    private final ReceiverEvent receiverEvent;

    public TaskExecutor(ReceiverEvent receiverEvent) {
        this.receiverEvent = receiverEvent;
    }

    public Executable start(Task task) {
        return screenshotCamera(task);
    }

    public void stop(Executable executable) {
        logger.info("Stop DETECT MOTION task execute");
        executable.stop();
    }


    private Executable screenshotCamera(Task task) {
        logger.info("Record recordable task execute");
        ScreenshotCameraConfig detectMotionConfig = (ScreenshotCameraConfig) task.getOptions();
        Recordable recordable = new Camera();
        Executable recordCamera = new RecordCamera(task.getTaskId(), detectMotionConfig, recordable, receiverEvent);
        recordCamera.start();
        return recordCamera;
    }

}
