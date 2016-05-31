package ua.epam.spring.hometask.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.service.AuditoriumService;
import ua.epam.spring.hometask.spring.qualifiers.EventArea;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Igor on 12.05.2016.
 */
@Service
public class AuditoriumServiceImpl implements AuditoriumService {

    @Autowired
    @EventArea
    private List<Auditorium> auditoriums = new ArrayList<>();

    @Nonnull
    @Override
    public Collection<Auditorium> getAll() {
        return auditoriums;
    }

    @Nullable
    @Override
    public Auditorium getByName(@Nonnull String name) {
        return auditoriums.stream()
                .filter(auditorium -> auditorium.getName().equals(name))
                .findFirst()
                .orElseThrow(NullPointerException::new);
    }

    @Override
    public void addAuditorium(Auditorium auditorium) {
        auditoriums.add(auditorium);
    }


}
