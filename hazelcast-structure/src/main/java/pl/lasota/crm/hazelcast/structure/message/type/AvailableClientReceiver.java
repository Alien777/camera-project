package pl.lasota.crm.hazelcast.structure.message.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AvailableClientReceiver implements Serializable {
    private String clientId;

}
