package pl.lasota.crm.hazelcast;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import pl.lasota.crm.hazelcast.structure.StructureName;

public class HazelcastServer {
    public static void main(String[] args) {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        hazelcastInstance.getTopic(StructureName.clientServerMessageCommend);
    }
}
