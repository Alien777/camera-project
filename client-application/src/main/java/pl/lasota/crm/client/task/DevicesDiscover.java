package pl.lasota.crm.client.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.lasota.crm.client.executable.device.Device;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class DevicesDiscover {
    private static final Logger logger = LoggerFactory.getLogger(DevicesDiscover.class);
    private final Class<?>[] devices;


    public DevicesDiscover(Class<?>... devices) {
        this.devices = devices;
    }

    public List<String> start() {
        return Arrays.stream(devices).map(a -> {
            Device o = null;
            Constructor<?>[] constructors = a.getConstructors();
            try {
                o = (Device) constructors[0].newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                logger.info("Problem with create constructor");
            }
            return o.availableDevices();
        }).flatMap(List::stream).collect(Collectors.toList());
    }

}
