package pl.lasota.crm.hazelcast.structure.message;

public interface Broker<M> {

    void onBroker(M m, User<M> from);

    void onBroker(M m, User<M> from, String... to);

    void registryUser(User<M> user);

    void removeUser(User<M> user);

}
