package pl.lasota.crm.server.recorder.function.video;

import pl.lasota.crm.common.video.Video;
import pl.lasota.crm.common.video.VideoStructure;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Watcher {
    private static final int SECOND_TO_MILLISECOND = 1000;
    private final Video video = VideoStructure.video();
    private final Path path;
    private final short fps;

    private final ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);


    public Watcher(Path path) throws IOException {
        this.path = path;
        this.fps = this.video.fps(path);
    }


    public Flux<Frame> watch() {

        return Flux.interval(Duration.ofMillis(1000 / this.fps))
                .map(a -> {
                    try {
                        return new Frame(this.video.read(this.path, video.frameCount(path) - 1));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                });

    }

}
