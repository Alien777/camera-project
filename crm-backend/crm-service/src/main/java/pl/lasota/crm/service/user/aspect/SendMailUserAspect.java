package pl.lasota.crm.service.user.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import pl.lasota.crm.domain.CreateUserSuccessDomain;
import pl.lasota.tool.email.MailSenderExecutor;

import javax.mail.MessagingException;

@Aspect
@Configuration
public class SendMailUserAspect {

    private MailSenderExecutor mailSenderExecutor;

    public SendMailUserAspect(MailSenderExecutor mailSenderExecutor) {
        this.mailSenderExecutor = mailSenderExecutor;
    }

    @AfterReturning(pointcut = "execution(* pl.lasota.crm.service.user.IUserService.create(..))", returning = "retVal")
    public void sendMailForCreatedUser(JoinPoint joinPoint, CreateUserSuccessDomain retVal) throws MessagingException {
        mailSenderExecutor.send(retVal.getEmail(), "Witam " + retVal.getName(), "<a href=http://localhost:3000/activate/" + retVal.getActivator() + ">Aktywuj konto</a>");
    }

}
