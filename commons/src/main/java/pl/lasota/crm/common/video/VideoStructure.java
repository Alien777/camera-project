//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package pl.lasota.crm.common.video;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class VideoStructure implements Video {
    private static final int VALUE_SIZE = 18;
    private static final int FRAME_RECORD_SIZE = 12;
    private static final int SECOND_IN_24H = 86400;
    private static VideoStructure instance = null;

    public VideoStructure() {
    }

    public static Video video() {
        if (instance == null) {
            Class var0 = VideoStructure.class;
            synchronized(VideoStructure.class) {
                if (instance == null) {
                    instance = new VideoStructure();
                }
            }
        }

        return instance;
    }

    public void init(Path path, long fps) throws IOException {
        int sizeFrame = (int)(18L + 1036800L * fps);
        ByteBuffer buffer = ByteBuffer.allocate(sizeFrame);
        buffer.putShort((short)((int)fps));
        buffer.putLong(0L);
        buffer.putLong((long)sizeFrame);
        Files.write(path, buffer.array(), new OpenOption[]{StandardOpenOption.CREATE});
    }

    public void append(Path path, byte[] image) throws IOException {
        this.appendFrameInformation(path, image.length);
        Files.write(path, image, new OpenOption[]{StandardOpenOption.APPEND});
    }

    public short fps(Path path) throws IOException {
        SeekableByteChannel s = Files.newByteChannel(path, StandardOpenOption.READ);

        short var5;
        try {
            s.position(0L);
            byte[] b = new byte[2];
            ByteBuffer wrap = ByteBuffer.wrap(b);
            s.read(wrap);
            var5 = wrap.getShort(0);

        } catch (Throwable var7) {
            if (s != null) {
                try {
                    s.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
            }

            throw var7;
        }

        if (s != null) {
            s.close();
        }

        return var5;
    }

    public byte[] read(Path path, long count) throws IOException {
        SeekableByteChannel s = Files.newByteChannel(path, StandardOpenOption.READ);

        byte[] var10;
        try {
            long[] longs = this.readFrameInformation(path, count);
            long l = this.framesMetadataSize(path);
            s.position(l + longs[0]);
            byte[] b = new byte[Math.toIntExact(longs[1])];
            ByteBuffer wrap = ByteBuffer.wrap(b);
            s.read(wrap);
            var10 = wrap.array();
        } catch (Throwable var12) {
            if (s != null) {
                try {
                    s.close();
                } catch (Throwable var11) {
                    var12.addSuppressed(var11);
                }
            }

            throw var12;
        }

        if (s != null) {
            s.close();
        }

        return var10;
    }

    public long frameCount(Path path) throws IOException {
        SeekableByteChannel s = Files.newByteChannel(path, StandardOpenOption.READ);

        long var5;
        try {
            s.position(2L);
            byte[] b = new byte[8];
            ByteBuffer wrap = ByteBuffer.wrap(b);
            s.read(wrap);
            var5 = wrap.getLong(0);
        } catch (Throwable var8) {
            if (s != null) {
                try {
                    s.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
            }

            throw var8;
        }

        if (s != null) {
            s.close();
        }

        return var5;
    }

    private long framesMetadataSize(Path path) throws IOException {
        SeekableByteChannel s = Files.newByteChannel(path, StandardOpenOption.READ);

        long var5;
        try {
            s.position(10L);
            byte[] b = new byte[8];
            ByteBuffer wrap = ByteBuffer.wrap(b);
            s.read(wrap);
            var5 = wrap.getLong(0);
        } catch (Throwable var8) {
            if (s != null) {
                try {
                    s.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
            }

            throw var8;
        }

        if (s != null) {
            s.close();
        }

        return var5;
    }

    private void writeFrameCount(Path path, long count) throws IOException {
        SeekableByteChannel s = Files.newByteChannel(path, StandardOpenOption.WRITE);

        try {
            s.position(2L);
            ByteBuffer countBuffer = ByteBuffer.allocate(8).putLong(count);
            ByteBuffer wrap = ByteBuffer.wrap(countBuffer.array());
            s.write(wrap);
        } catch (Throwable var8) {
            if (s != null) {
                try {
                    s.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
            }

            throw var8;
        }

        if (s != null) {
            s.close();
        }

    }

    private void appendFrameInformation(Path path, Integer sizeByteImage) throws IOException {
        long count = this.frameCount(path);
        long[] longs = this.readFrameInformation(path, Math.max(count - 1L, 0L));
        SeekableByteChannel s = Files.newByteChannel(path, StandardOpenOption.WRITE);

        try {
            s.position(18L + 12L * count);
            ByteBuffer buffer = ByteBuffer.wrap(ByteBuffer.allocate(12).putLong(longs[0] + longs[1]).putInt(sizeByteImage).array());
            s.write(buffer);
        } catch (Throwable var10) {
            if (s != null) {
                try {
                    s.close();
                } catch (Throwable var9) {
                    var10.addSuppressed(var9);
                }
            }

            throw var10;
        }

        if (s != null) {
            s.close();
        }

        long i = count + 1L;
        this.writeFrameCount(path, i);
    }

    private long[] readFrameInformation(Path path, long frameCount) throws IOException {
        long[] ints = new long[2];
        SeekableByteChannel s = Files.newByteChannel(path, StandardOpenOption.READ);

        long[] var8;
        try {
            s.position(18L + 12L * frameCount);
            byte[] b = new byte[8];
            ByteBuffer wrap = ByteBuffer.wrap(b);
            s.read(wrap);
            ints[0] = wrap.getLong(0);
            b = new byte[4];
            wrap = ByteBuffer.wrap(b);
            s.read(wrap);
            ints[1] = (long)wrap.getInt(0);
            var8 = ints;
        } catch (Throwable var10) {
            if (s != null) {
                try {
                    s.close();
                } catch (Throwable var9) {
                    var10.addSuppressed(var9);
                }
            }

            throw var10;
        }

        if (s != null) {
            s.close();
        }

        return var8;
    }
}
