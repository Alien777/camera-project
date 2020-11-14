package pl.lasota.crm.server;


import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import pl.lasota.crm.hazelcast.structure.IdentifierClientApp;
import pl.lasota.crm.hazelcast.structure.StructureName;
import pl.lasota.crm.hazelcast.structure.message.*;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.WatchRecordingDto;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.task.ScreenshotCameraConfig;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.task.Task;
import pl.lasota.crm.hazelcast.structure.message.type.*;
import pl.lasota.crm.common.Auth;
import pl.lasota.crm.common.MessageDto;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.StartRecordingDto;
import pl.lasota.crm.hazelcast.structure.message.managetoclient.StopRecordingDto;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Slf4j
public class FactoryTasks {
    private final Map<User<Object>, RSocketRequester> clients = new ConcurrentHashMap<>();
    private final OnlineBroker<Object> onlineBroker;
    private final WebClientApplicationClientBroker webClientApplicationClientBroker;

    @Value("${path.save.file}")
    private String path;

    public FactoryTasks() {
        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient();
        ITopic<Message<Object>> m = hazelcastInstance.getTopic(StructureName.clientServerMessageCommend);
        HazelcastBrokerConfigure<Object> online = new HazelcastBrokerConfigure<>(m);
        LocalBroker<Object> localBroker = new LocalBroker<>();
        onlineBroker = new OnlineBroker<>(localBroker, online);
        webClientApplicationClientBroker = new WebClientApplicationClientBroker(path, clients);
    }

    public void startRecordingVideo(MessageDto<StartRecordingDto> message) {

        Auth auth = message.getAuth();
        ScreenshotCameraConfig config = message.getData().getConfig();
        ScreenshotCameraConfig screenshotCameraConfig = new ScreenshotCameraConfig();
        screenshotCameraConfig.setCameraName(config.getCameraName());
        screenshotCameraConfig.setCameraOriginName(config.getCameraOriginName());

        onlineBroker.onBroker(new StartRecording(new Task(UUID.randomUUID(), config)),
                new IdentifierClientApp(auth.getUserId(), auth.getClientId()),
                new IdentifierClientApp(auth.getUserId(), message.getData().getClientId()).toString());
    }

    public void stopRecordingVideo(MessageDto<StopRecordingDto> dtoMessage) {
        Auth auth = dtoMessage.getAuth();
        StopRecordingDto data = dtoMessage.getData();

        onlineBroker.onBroker(new StopRecording(data.getTaskId()),
                new IdentifierClientApp(auth.getUserId(), auth.getClientId()),
                new IdentifierClientApp(auth.getUserId(), data.getClientId()).toString());
    }

    public void addClient(UUID userId, String clientId, RSocketRequester rSocketRequester) {
        IdentifierClientApp identifierClientApp = new IdentifierClientApp(onlineBroker, webClientApplicationClientBroker, userId, clientId);
        clients.put(identifierClientApp, rSocketRequester);
        log.info("Add client:{} for user:{}", clientId, userId);
    }

    public void removeClient(UUID userId, String clientId) {
        IdentifierClientApp identifierClientApp = new IdentifierClientApp(userId, clientId);
        Optional<Map.Entry<User<Object>, RSocketRequester>> first =
                clients.entrySet().stream().filter(id -> id.getKey().equals(identifierClientApp)).findFirst();

        first.ifPresent(u -> {
            u.getKey().logout();
            clients.remove(identifierClientApp);
        });

        log.info("Remove client:{} for user:{}", clientId, userId);
    }

    public void availableClients(MessageDto<String> message) {
        Auth auth = message.getAuth();
        onlineBroker.onBroker(new AvailableClient(), new IdentifierClientApp(auth.getUserId(), auth.getClientId()));
    }

    public void availableDevices(MessageDto<String> message) {
        Auth auth = message.getAuth();
        onlineBroker.onBroker(new AvailableDevice(),
                new IdentifierClientApp(auth.getUserId(), auth.getClientId()),
                new IdentifierClientApp(auth.getUserId(), message.getData()).toString());
    }

    public void watchCamera(MessageDto<WatchRecordingDto> message) {
        Auth auth = message.getAuth();
        onlineBroker.onBroker(new WatchRealVideo(message.getData().getCameraId()),
                new IdentifierClientApp(auth.getUserId(), auth.getClientId()),
                new IdentifierClientApp(auth.getUserId(), message.getData().getClientId()).toString());
    }

}
