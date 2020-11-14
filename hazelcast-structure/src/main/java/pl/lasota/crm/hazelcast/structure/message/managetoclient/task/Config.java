package pl.lasota.crm.hazelcast.structure.message.managetoclient.task;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = ScreenshotCameraConfig.class, name = "scc")})
public interface Config extends Serializable {
}
