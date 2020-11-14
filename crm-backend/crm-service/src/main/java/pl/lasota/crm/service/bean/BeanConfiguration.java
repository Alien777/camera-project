package pl.lasota.crm.service.bean;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lasota.tool.email.MailSenderExecutor;
import pl.lasota.tool.email.MailSenderExecutorImpl;
import pl.lasota.tool.email.SimpleProvider;
import pl.lasota.tool.sr.service.security.ProvidingPrivilege;
import pl.lasota.tool.sr.service.security.SimpleProvidingPrivileges;

@Configuration
public class BeanConfiguration {
    @Bean
    public ProvidingPrivilege providingPrivilege() {
        return new SimpleProvidingPrivileges();
    }

    @Bean
    public MailSenderExecutor mailSenderExecutor() {
        return new MailSenderExecutorImpl(new SimpleProvider(), "adam@localhost.pl", "localhost");
    }
}
