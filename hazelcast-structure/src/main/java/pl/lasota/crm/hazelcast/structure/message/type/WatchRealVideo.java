package pl.lasota.crm.hazelcast.structure.message.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchRealVideo implements Serializable {
    private String cameraId;

}
