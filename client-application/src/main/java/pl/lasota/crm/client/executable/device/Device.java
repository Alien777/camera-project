package pl.lasota.crm.client.executable.device;

import java.util.List;

public interface Device {
    void open(String path);

    void close();

    List<String> availableDevices();
}
