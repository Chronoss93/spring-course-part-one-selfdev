import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.junit.Assert.assertEquals;

/**
 * Created by Igor on 31.05.2016.
 */
public class DaoSetupIntegrationTest {
    private EmbeddedDatabase db;

    @Before
    public void setUp() {
        //db = new EmbeddedDatabaseBuilder().addDefaultScripts().build();
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.DERBY)
                .addScript("db/sql/create-db.sql")
                .addScript("db/sql/insert-data.sql")
                .build();
    }

    @Test
    public void testFindByname() {
        JdbcTemplate template = new JdbcTemplate(db);
        int count = template.queryForObject("Select count(id) from user1", Integer.class);
        assertEquals(2, count);
    }

    @After
    public void tearDown() {
        db.shutdown();
    }


}
