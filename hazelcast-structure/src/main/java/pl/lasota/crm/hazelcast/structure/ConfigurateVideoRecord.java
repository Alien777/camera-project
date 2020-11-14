package pl.lasota.crm.hazelcast.structure;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConfigurateVideoRecord implements Serializable {
   private String pathFile;
   private String cameraOriginName;
   private String cameraName;
}
