
package pl.lasota.crm.hazelcast.structure.message.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class AvailableDeviceReceiver implements Serializable {
    private List<String> list;

}
