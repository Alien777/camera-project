package pl.lasota.crm.server.recorder.function;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import pl.lasota.crm.common.endpoint.clienttomanage.ScreenshotCamera;

import java.io.IOException;

@Controller
public class ReceiverImagesController {

    private final SaveVideo saveVideo;

    public ReceiverImagesController(SaveVideo saveVideo) {
        this.saveVideo = saveVideo;
    }


    @MessageMapping({"record-video"})
    public void record(@Payload ScreenshotCamera screenshotCamera) throws IOException {
        this.saveVideo.process(screenshotCamera);
    }

}
