package pl.lasota.crm.client.executable.device.function;

import pl.lasota.crm.client.executable.device.Device;

import java.awt.image.BufferedImage;

public interface Recordable extends Device {
    BufferedImage onScreenShot();

    double fps();

    int width();

    int height();
}
