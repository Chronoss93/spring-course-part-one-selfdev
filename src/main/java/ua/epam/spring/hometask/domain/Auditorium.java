package ua.epam.spring.hometask.domain;

import lombok.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @author Yuriy_Tkach
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Auditorium {

    @Getter @Setter
    private String name;

    @Getter @Setter
    private long numberOfSeats;

    @Getter @Setter
    private Set<Long> vipSeats = Collections.emptySet();
    /**
     * Counts how many vip seats are there in supplied <code>seats</code>
     *
     * @param seats Seats to process
     * @return number of vip seats in request
     */
    public long countVipSeats(Collection<Long> seats) {
        return seats.stream().filter(seat -> vipSeats.contains(seat)).count();
    }

    public boolean isVipSeat(Long seat) {
        return vipSeats.contains(seat);
    }


    public Set<Long> getAllSeats() {
        return LongStream.range(1, numberOfSeats + 1).boxed().collect(Collectors.toSet());
    }
}
