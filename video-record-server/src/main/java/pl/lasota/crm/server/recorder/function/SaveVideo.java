package pl.lasota.crm.server.recorder.function;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.function.BiConsumer;

import com.hazelcast.cache.ICache;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pl.lasota.crm.common.endpoint.clienttomanage.ScreenshotCamera;
import pl.lasota.crm.common.endpoint.clienttomanage.TypeScreenShot;
import pl.lasota.crm.common.video.VideoStructure;
import pl.lasota.crm.hazelcast.structure.ConfigurateVideoRecord;
import pl.lasota.crm.hazelcast.structure.StructureName;

@Service
public class SaveVideo {

//    private final ICache<UUID, ConfigurateVideoRecord> cache;

    public SaveVideo() {

    }

    public void process(ScreenshotCamera s) throws IOException {



    }


}
