package pl.lasota.crm.hazelcast.structure.message;

public class OnlineBroker<M> implements Broker<M> {

    private final LocalBroker<M> localBroker;
    private final Configure<M> configure;

    public OnlineBroker(LocalBroker<M> localBroker, Configure<M> configure) {
        configure.registryMessageOnlineListener(localBroker);
        this.localBroker = localBroker;
        this.configure = configure;
    }

    @Override
    public void onBroker(M m, User<M> u) {
        configure.enableLocalBroke(() -> localBroker.onBroker(m, u));
        configure.onBroker(m, u.id());
    }

    @Override
    public void onBroker(M m, User<M> from, String... to) {
        configure.enableLocalBroke(() -> localBroker.onBroker(m, from, to));
        configure.onBroker(m, from.id(), to);
    }

    @Override
    public void registryUser(User<M> user) {
        localBroker.registryUser(user);
    }

    @Override
    public void removeUser(User<M> user) {
        localBroker.removeUser(user);
    }
}
