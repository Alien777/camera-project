package pl.lasota.crm.hazelcast.structure.message;

public interface Configure<M> {

    void onBroker(M m, String from);

    void onBroker(M m, String from, String... to);

    void registryMessageOnlineListener(LocalBroker<M> mLocalBroker);

    void enableLocalBroke(Option option);
}
