package ua.epam.spring.hometask.repository.impl.jdbctemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ua.epam.spring.hometask.domain.DomainObject;
import ua.epam.spring.hometask.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

/**
 * Created by Igor on 01.06.2016.
 */
public abstract class GenericJdbcTemplateDAO<T extends DomainObject> implements CrudRepository<T> {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected String tableName;
    protected RowMapper<T> rowMapper;

    GenericJdbcTemplateDAO(String tableName, RowMapper rowMapper) {
        this.tableName = tableName;
        this.rowMapper = rowMapper;
    }

    @Override
    public void delete(T obj) {
        String query = "delete from " + tableName + " where id=?";
        jdbcTemplate.update(query, obj.getId());
    }

    @Override
    public T find(Long id) {
        String query = "select * from " + tableName + " where id = ?";
        T object = jdbcTemplate.queryForObject(query, new Object[]{id}, rowMapper);
        return object;
    }

    @Override
    public Collection<T> findAll() {
        String query = "select * from " + tableName;
        List<T> result = jdbcTemplate.query(query, rowMapper);
        return result;
    }

}
