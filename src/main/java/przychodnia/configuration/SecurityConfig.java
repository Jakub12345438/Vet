package przychodnia.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private final CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, CustomAuthenticationSuccessHandler successHandler) {
        this.userDetailsService = userDetailsService;
        this.successHandler = successHandler;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(Customizer.withDefaults())
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/register/**").anonymous()
                                .requestMatchers("/login/**").anonymous()
                                .requestMatchers("/login").anonymous()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/index").permitAll()
                                .requestMatchers("/visit").hasRole("USER")
                                .requestMatchers("/admin").hasRole("ADMIN")
                                .requestMatchers("/vet").hasRole("DOCTOR")
                                .requestMatchers("/edit-visit/{visitID}").hasRole("ADMIN")
                                .requestMatchers("/delete-visit/{visitID}").hasRole("ADMIN")
                                .requestMatchers("/search").hasRole("ADMIN")
                                .requestMatchers("/user-visits").hasRole("USER")
                                .requestMatchers("/edit/{visitID}").hasRole("USER")
                                .requestMatchers("/delete/{visitID}").hasRole("USER")
                                .requestMatchers("/info").permitAll()
                                .requestMatchers("/users").hasRole("ADMIN")
                                .requestMatchers("/add-doctor-role/**", "/remove-doctor-role/**").hasRole("ADMIN")
                                .requestMatchers("/edit-visit-vet/{visitID}").hasRole("DOCTOR")
                                .requestMatchers("/delete-visit-vet/{visitID}").hasRole("DOCTOR")
                                .requestMatchers("/forgot-password/**", "/reset-password/**").anonymous()
                                .requestMatchers("/visits-vet").hasRole("DOCTOR")
                                .requestMatchers("/visit/details/{id}").hasRole("DOCTOR")
                                .requestMatchers("/holidays").hasRole("ADMIN")
                                .requestMatchers("/holidays/add").hasRole("ADMIN")
                                .requestMatchers("/holidays-vets").hasRole("ADMIN")
                                .requestMatchers("/delete-holiday/{holidayID}").hasRole("ADMIN")
                                .requestMatchers("/vet-holidays").hasRole("DOCTOR")
                                .requestMatchers("/api/holidays/{year}").permitAll()
                                .requestMatchers("/api/holidays-with-names/{year}").permitAll()
                                .requestMatchers("/api/user-visits/{userId}/{year}").permitAll()
                                .requestMatchers("/api/vets/busy-times/{vetId}/{date}").permitAll()
                                .requestMatchers("/api/vets/busy-dates/{vetId}").permitAll()
                                .requestMatchers("/api/vets/holidays/{vetId}").permitAll()
                                .anyRequest().permitAll()
                                  ).formLogin(
                                        form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .successHandler(successHandler)
                                                .permitAll()
                ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)  // Zawsze twórz sesję
                        .sessionFixation().migrateSession()  // Protect against session fixation attacks by migrating to a new session ID after login
                        .maximumSessions(1)                  // Limit to 1 session per user
                        .expiredUrl("/login?expired")        // Redirect to login page if session expires
                )
                .logout(
                        logout -> logout
                                .invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessUrl("/login?logout")

                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authenticationConfiguration.getAuthenticationManager();
    }




}
