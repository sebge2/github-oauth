package be.sgerard.i18n.configuration;

import be.sgerard.i18n.service.repository.git.DefaultGitRepositoryApi;
import be.sgerard.i18n.service.repository.git.GitRepositoryApiProvider;
import org.springframework.boot.autoconfigure.session.JdbcSessionDataSourceInitializer;
import org.springframework.boot.autoconfigure.session.JdbcSessionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.util.Locale;

/**
 * Main beans configuration.
 *
 * @author Sebastien Gerard
 */
@Configuration
@EnableConfigurationProperties({AppProperties.class, JdbcSessionProperties.class /* TODO should be automatically created*/})
public class MainConfiguration {

    public MainConfiguration() {
    }

    @Bean
    public PasswordEncoder externalUserPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean // TODO should be automatically created
    public JdbcSessionDataSourceInitializer jdbcSessionDataSourceInitializer(DataSource dataSource,
                                                                             ResourceLoader resourceLoader,
                                                                             JdbcSessionProperties properties) {
        return new JdbcSessionDataSourceInitializer(dataSource, resourceLoader, properties);
    }

    @Bean
    public MessageSource messageSource() {
        Locale.setDefault(Locale.ENGLISH);

        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:org/springframework/security/messages");
        messageSource.addBasenames("classpath:i18n/validation");
        messageSource.addBasenames("classpath:i18n/exception");

        return messageSource;
    }

    @Bean
    public GitRepositoryApiProvider gitRepositoryApiProvider() {
        return DefaultGitRepositoryApi::createAPI;
    }
}
