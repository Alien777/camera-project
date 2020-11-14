package pl.lasota.crm.client.executable.device;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionListener;
import pl.lasota.crm.client.executable.device.function.Recordable;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;


public class Camera implements Device, Recordable {

    private Webcam webcam;

    @Override
    public void open(String name) throws IllegalArgumentException, WebcamException {
        webcam = Webcam.getWebcamByName(name);
        webcam.open();
    }

    @Override
    public BufferedImage onScreenShot() {
        return webcam.getImage();
    }

    @Override
    public double fps() {

        return webcam.getFPS();
    }

    @Override
    public int width() {
        return webcam.getViewSize().width;
    }

    @Override
    public int height() {
        return webcam.getViewSize().height;
    }


    @Override
    public void close() throws WebcamException {
        webcam.close();
    }

    @Override
    public List<String> availableDevices() {
        return Webcam.getWebcams().stream().map(Webcam::getName).collect(Collectors.toList());
    }
}
