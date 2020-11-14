package pl.lasota.crm.hazelcast.structure.message;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class LocalBroker<M> implements Broker<M> {

    private final ConcurrentMap<String, User<M>> localUser = new ConcurrentHashMap<>();


    @Override
    public void onBroker(M m, User<M> from) {
        localUser.entrySet().parallelStream()
                .filter(us -> !us.getKey().equals(from.id()))
                .forEach(us -> us.getValue().receiverMessage(m, from.id()));
    }



    @Override
    public void onBroker(M m, User<M> from, String... to) {
        for (String t : to) {
            User<M> user = localUser.get(t);
            if (user == null) {
                break;
            }
            user.receiverMessage(m, from.id());
        }
    }

    @Override
    public void registryUser(User<M> user) {
        localUser.put(user.id(), user);
    }

    @Override
    public void removeUser(User<M> id) {
        User<M> user = localUser.get(id.id());
        localUser.remove(user.id());
    }
}
