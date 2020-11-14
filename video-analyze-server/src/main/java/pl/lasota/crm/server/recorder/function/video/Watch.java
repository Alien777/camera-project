package pl.lasota.crm.server.recorder.function.video;

public interface Watch {
    void watch(byte[] data, long currentSecond, long second);
}
