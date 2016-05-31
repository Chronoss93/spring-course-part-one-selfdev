package ua.epam.spring.hometask.spring.aspects;

import java.util.Map;

/**
 * Created by Igor on 26.05.2016.
 */
public abstract class AbstractGenericCounter {
    <T> void incrementCount(Map<T, Integer> map, T obj) {
        if (!map.containsKey(obj)) {
            map.put(obj, 1);
        } else {
            int newCount = map.get(obj) + 1;
            map.put(obj, newCount);
        }
    }
}
