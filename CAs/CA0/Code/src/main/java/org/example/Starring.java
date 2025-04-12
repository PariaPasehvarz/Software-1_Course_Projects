package org.example;

public class Starring {
    private final String movieName;
    private final Date startDate;
    private final Date endDate;

    public Starring(String movieName, Date startDate, Date endDate) {
        if (movieName == null || movieName.isEmpty()) {
            throw new IllegalArgumentException("Movie name cannot be empty");
        }
        if (!startDate.isValidDate() || !endDate.isValidDate()) {
            throw new IllegalArgumentException("start date or end date is invalid");
        }
        this.movieName = movieName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean overlapsWith(Starring other) {
        return (this.startDate.compareTo(other.endDate) <= 0) && (this.endDate.compareTo(other.startDate) >= 0);
    }

    public int getDurationInDays() {
        return startDate.daysBetween(endDate);
    }
}
