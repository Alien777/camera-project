package pl.lasota.crm.client;

import pl.lasota.crm.common.endpoint.clienttomanage.ScreenshotCamera;

public interface ReceiverEvent {
    void event(ScreenshotCamera o, String message);
}
