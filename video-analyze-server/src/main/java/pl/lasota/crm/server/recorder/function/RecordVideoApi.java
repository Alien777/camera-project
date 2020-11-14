package pl.lasota.crm.server.recorder.function;

import io.rsocket.RSocketFactory.ServerRSocketFactory;
import io.rsocket.core.Resume;
import io.rsocket.frame.decoder.PayloadDecoder;
import org.opencv.core.Core;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.boot.rsocket.server.ServerRSocketFactoryProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class RecordVideoApi {
    public RecordVideoApi() {
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        SpringApplication.run(RecordVideoApi.class, args);
    }

    @Bean
    public RSocketServerCustomizer rSocketResume() {
        return rSocketServer -> rSocketServer.resume( new Resume()).payloadDecoder(PayloadDecoder.ZERO_COPY);
    }
}
