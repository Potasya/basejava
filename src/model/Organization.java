package model;

import util.DateUtil;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Marisha on 25/02/2018.
 */
public class Organization {
    private final Link homepage;
    private final List<Position> positions;

    public Organization(String name, String url, Position... positions) {
        Objects.requireNonNull(positions, "Positions mustn't me null");
        this.homepage = new Link(name, url);
        this.positions = Arrays.asList(positions);
    }

    public Organization(Link homepage, List<Position> positions) {
        this.homepage = homepage;
        this.positions = positions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!homepage.equals(that.homepage)) return false;
        return positions.equals(that.positions);

    }

    @Override
    public int hashCode() {
        int result = homepage.hashCode();
        result = 31 * result + positions.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "homepage=" + homepage +
                ", positions=" + positions +
                '}';
    }

    public static class Position {
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final String title;
        private final String description;

        public Position(LocalDate startDate, LocalDate endDate, String title, String description) {
            Objects.requireNonNull(startDate, "startDate mustn't be null");
            Objects.requireNonNull(endDate, "endDate mustn't be null");
            Objects.requireNonNull(title, "title mustn't be null");
            this.startDate = startDate;
            this.endDate = endDate;
            this.title = title;
            this.description = description;
        }

        public Position(int startYear, Month startMonth, int endYear, Month endMonth, String title, String description) {
            this(DateUtil.of(startYear, startMonth), DateUtil.of(endYear, endMonth), title, description);
        }

        public Position(int startYear, Month startMonth, String title, String description) {
            this(DateUtil.of(startYear, startMonth), DateUtil.NOW, title, description);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Position that = (Position) o;

            if (!startDate.equals(that.startDate)) return false;
            if (!endDate.equals(that.endDate)) return false;
            if (!title.equals(that.title)) return false;
            return description != null ? description.equals(that.description) : that.description == null;

        }

        @Override
        public int hashCode() {
            int result = startDate.hashCode();
            result = 31 * result + endDate.hashCode();
            result = 31 * result + title.hashCode();
            result = 31 * result + (description != null ? description.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Position{" +
                    "startDate=" + startDate +
                    ", endDate=" + endDate +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
}
