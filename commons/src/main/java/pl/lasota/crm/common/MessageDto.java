package pl.lasota.crm.common;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto<T> {
    private T data;
    private Auth auth;
}
