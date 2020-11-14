package pl.lasota.crm.hazelcast.structure;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.lasota.crm.hazelcast.structure.message.Broker;
import pl.lasota.crm.hazelcast.structure.message.ReceiverMessageEvent;
import pl.lasota.crm.hazelcast.structure.message.User;

import java.util.UUID;

@Getter
@EqualsAndHashCode(of = {"userId", "clientId"}, callSuper = false)
public class IdentifierClientApp extends User<Object> {
    private final UUID userId;
    private final String clientId;



    public IdentifierClientApp(UUID userId, String clientId) {
        super(userId.toString() +"_"+ clientId);
        this.userId = userId;
        this.clientId = clientId;

    }


    public IdentifierClientApp(Broker<Object> broker, ReceiverMessageEvent<Object> receiverListener, UUID userId, String clientId) {
        super(broker, receiverListener, userId.toString() + "_" + clientId);
        this.userId = userId;
        this.clientId = clientId;
    }

    public IdentifierClientApp(String id) {
        super(id);
        String[] s = id.split("_");
        this.userId = UUID.fromString(s[0]);
        this.clientId = s[1];

    }

    public Boolean isSameUser(String id)
    {
      return super.id().split("_")[0].equals(id.split("_")[0]);
    }

    @Override
    public String toString() {
        return userId.toString() + "_" + clientId;
    }
}
