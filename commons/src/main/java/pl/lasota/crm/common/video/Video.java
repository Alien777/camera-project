package pl.lasota.crm.common.video;

import java.io.IOException;
import java.nio.file.Path;

public interface Video {
    void init(Path path, long fps) throws IOException;

    void append(Path path, byte[] data) throws IOException;

    short fps(Path path) throws IOException;

    long frameCount(Path path) throws IOException;

    byte[] read(Path path, long count) throws IOException;
}
