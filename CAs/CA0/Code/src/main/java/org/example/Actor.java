package org.example;

import java.util.ArrayList;
import java.util.List;

public class Actor {
    private final String name;
    private final List<Starring> filmRoles;

    public Actor(String name) {
        this.name = name;
        this.filmRoles = new ArrayList<>();
    }

    public void addRole(String movieName, Date startDate, Date endDate) {
        Starring newRole = new Starring(movieName, startDate, endDate);
        if (!filmRoles.contains(newRole)) {
            filmRoles.add(newRole);
        }
    }

    public long getTotalDaysWorked() {
        return filmRoles.stream()
                .mapToLong(Starring::getDurationInDays)
                .sum();
    }

    @Override
    public String toString() {
        return name;
    }
}
