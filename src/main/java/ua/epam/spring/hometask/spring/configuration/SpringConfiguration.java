package ua.epam.spring.hometask.spring.configuration;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.spring.qualifiers.EventArea;

import javax.sql.DataSource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Igor on 26.05.2016.
 */
@Configuration
@ComponentScan("ua.epam.spring.hometask.*")
@PropertySource({"classpath:auditoriums.properties",
        "classpath:dbmetadata.properties"})
@EnableAspectJAutoProxy
public class SpringConfiguration {

    private static final String SEPARATOR = ",";
    @Autowired
    private Environment env;
    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @EventArea
    public Auditorium getRedRoom() {
        String name = env.getProperty("auditorium1.name");
        String seatsNumberString = env.getProperty("auditorium1.seatsnumber");
        Long seatsNumberNumber = Long.valueOf(seatsNumberString);
        String vipSeatsString = env.getProperty("auditorium1.vipseats");
        Set<Long> vipSeatNumbers = convertStringToArray(vipSeatsString);

        return new Auditorium(name, seatsNumberNumber, vipSeatNumbers);
    }

    private Set<Long> convertStringToArray(String vipSeats) {
        List<String> stringList = Lists.newArrayList(Splitter.on(SEPARATOR).split(vipSeats));
        return stringList.stream().map(Long::parseLong).collect(Collectors.toSet());
    }

    @Bean
    @EventArea
    public Auditorium getYellowRoom() {
        String name = env.getProperty("auditorium2.name");
        String seatsNumberString = env.getProperty("auditorium2.seatsnumber");
        Long seatsNumberNumber = Long.valueOf(seatsNumberString);
        String vipSeatsString = env.getProperty("auditorium2.vipseats");
        Set<Long> vipSeatNumbers = convertStringToArray(vipSeatsString);

        return new Auditorium(name, seatsNumberNumber, vipSeatNumbers);
    }

    @Configuration
    static class DatabaseConfig {
        @Bean
        public DataSource dataSource() {
            EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
            EmbeddedDatabase db = builder
                    .setType(EmbeddedDatabaseType.DERBY)
                    .addScript("db/sql/create-db.sql")
//                    .addScript("db/sql/insert-data.sql")
                    .build();
            return db;
        }
    }
}
