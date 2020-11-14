package pl.lasota.crm.hazelcast.structure.message;

public interface ReceiverMessageEvent<M> {

    void onReceive(M message, User<M> me, String from);
}
