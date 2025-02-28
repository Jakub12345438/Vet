package przychodnia.configuration;

import org.springframework.boot.web.servlet.server.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CookieConfig {

    @Bean
    public Session.Cookie sessionCookie() {
        Session.Cookie cookie = new Session.Cookie();
        cookie.setMaxAge(Duration.ofMinutes(5)); // 5 minut
        return cookie;
    }
}

