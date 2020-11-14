package pl.lasota.crm.hazelcast.structure.message.managetoclient;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginStatus {
    public static final String MANAGER_LOGIN_STATUS_TO_CLIENT = "login-status-endpoint";
    private Status status;

    public static LoginStatus ok() {
        return new LoginStatus(Status.OK);
    }


    public static LoginStatus failed() {
        return new LoginStatus(Status.FAILED);
    }

    public LoginStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        OK, FAILED
    }

}
