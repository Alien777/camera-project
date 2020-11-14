package pl.lasota.crm.server.recorder.function;

import io.rsocket.core.Resume;
import io.rsocket.frame.decoder.PayloadDecoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class RecordVideoApi {

    public static void main(String[] args) {
        SpringApplication.run(RecordVideoApi.class, args);
    }

    @Bean
    public RSocketServerCustomizer rSocketResume() {
        return rSocketServer -> rSocketServer.resume( new Resume()).payloadDecoder(PayloadDecoder.ZERO_COPY);
    }
}
