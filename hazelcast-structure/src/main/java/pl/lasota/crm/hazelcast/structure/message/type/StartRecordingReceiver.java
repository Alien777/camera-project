package pl.lasota.crm.hazelcast.structure.message.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StartRecordingReceiver implements Serializable {
    private Boolean isOk;
}
