package przychodnia.configuration;

import jakarta.servlet.SessionTrackingMode;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;


@Configuration
public class SessionConfig {

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return servletContext -> {
            // Ustawienie czasu wygaśnięcia sesji na 300 sekund (5 minut)
            servletContext.setSessionTimeout(300);

            // Opcjonalnie: Ustawienie sposobu śledzenia sesji (np. tylko przez cookie, bez URL)
            servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
        };
    }
}
