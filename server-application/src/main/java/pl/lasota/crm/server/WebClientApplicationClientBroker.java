package pl.lasota.crm.server;

import org.springframework.messaging.rsocket.RSocketRequester;
import pl.lasota.crm.hazelcast.structure.IdentifierClientApp;
import pl.lasota.crm.hazelcast.structure.message.ReceiverMessageEvent;
import pl.lasota.crm.hazelcast.structure.message.User;
import pl.lasota.crm.hazelcast.structure.message.type.*;

import java.util.Map;

public class WebClientApplicationClientBroker implements ReceiverMessageEvent<Object> {

    private String path;
    private Map<User<Object>, RSocketRequester> clients;

    public WebClientApplicationClientBroker(String path, Map<User<Object>, RSocketRequester> clients) {
        this.path = path;
        this.clients = clients;
    }

    @Override
    public void onReceive(Object message, User<Object> me, String from) {
        RSocketRequester rSocketRequester = clients.get(me);
        if (rSocketRequester == null) {
            return;
        }

        availableDevice(message, me, from, rSocketRequester);
        startRecording(message, me, from, rSocketRequester);
        stopRecording(message, me, from, rSocketRequester);
        availableClient(message, me, from, rSocketRequester);
        watchingReal(message, me, from, rSocketRequester);
    }

    private void availableDevice(Object message, User<Object> me, String from, RSocketRequester rSocketRequester) {
        if (message instanceof AvailableDevice) {
            new ApplicationClientRequester(rSocketRequester).availableDevices(list -> me.sendMessage(new AvailableDeviceReceiver(list), from));
        }

        if (message instanceof AvailableDeviceReceiver) {
            AvailableDeviceReceiver m = (AvailableDeviceReceiver) message;
            new WebClientRequester(rSocketRequester).availableDevices(m.getList());
        }
    }

    private void startRecording(Object message, User<Object> me, String from, RSocketRequester rSocketRequester) {
        if (message instanceof StartRecording) {
            StartRecording m = (StartRecording) message;
            new ApplicationClientRequester(rSocketRequester).startRecording(m.getTask(), status ->
                    me.sendMessage(new StartRecordingReceiver(status), from));
        }

        if (message instanceof StartRecordingReceiver) {
            StartRecordingReceiver m = (StartRecordingReceiver) message;
            new WebClientRequester(rSocketRequester).startRecording(m.getIsOk());
        }
    }

    private void stopRecording(Object message, User<Object> me, String from, RSocketRequester rSocketRequester) {
        if (message instanceof StopRecording) {
            StopRecording m = (StopRecording) message;
            new ApplicationClientRequester(rSocketRequester).stopRecording(m.getTaskId(), status ->
                    me.sendMessage(new StopRecordingReceiver(status), from));
        }

        if (message instanceof StopRecordingReceiver) {
            StopRecordingReceiver m = (StopRecordingReceiver) message;
            new WebClientRequester(rSocketRequester).stopRecording(m.isOk());
        }
    }

    private void availableClient(Object message, User<Object> me, String from, RSocketRequester rSocketRequester) {
        IdentifierClientApp identifierClientApp = new IdentifierClientApp(me.id());
        if (message instanceof AvailableClient && new IdentifierClientApp(me.id()).isSameUser(from)) {
            me.sendMessage(new AvailableClientReceiver(identifierClientApp.getClientId()), from);
        }

        if (message instanceof AvailableClientReceiver) {
            AvailableClientReceiver m = (AvailableClientReceiver) message;
            new WebClientRequester(rSocketRequester).availableClient(m.getClientId());
        }
    }

    private void watchingReal(Object message, User<Object> me, String from, RSocketRequester rSocketRequester) {
        if (message instanceof WatchRealVideo) {
            new ApplicationClientRequester(rSocketRequester).watchReal(list ->
                    me.sendMessage(new WatchRealVideoReceiver(list.getBytes(),((WatchRealVideo) message).getCameraId()), from));
        }

        if (message instanceof WatchRealVideoReceiver) {
//            AvailableDeviceReceiver m = (WatchRealVideoReceiver) message;
//            new WebClientRequester(rSocketRequester).a(m.getList());
        }
    }
}
