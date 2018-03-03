package model;

import util.DateUtil;
import util.XMLLocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Marisha on 25/02/2018.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Serializable{
    public static final long serialVersionUID = 1L;
    private Link homepage;
    private List<Position> positions = new ArrayList<>();

    public Organization() {
    }

    public Organization(String name, String url, Position... positions) {
        Objects.requireNonNull(positions, "Positions mustn't me null");
        this.homepage = new Link(name, url);
        this.positions = Arrays.asList(positions);
    }

    public Organization(Link homepage, List<Position> positions) {
        this.homepage = homepage;
        this.positions = positions;
    }

    public Link getHomepage() {
        return homepage;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void addPosition(Position position) {
        positions.add(position);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (homepage != null ? !homepage.equals(that.homepage) : that.homepage != null) return false;
        return positions != null ? positions.equals(that.positions) : that.positions == null;

    }

    @Override
    public int hashCode() {
        int result = homepage != null ? homepage.hashCode() : 0;
        result = 31 * result + (positions != null ? positions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "homepage=" + homepage +
                ", positions=" + positions +
                '}';
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Position implements Serializable {
        public static final long serialVersionUID = 1L;
        @XmlJavaTypeAdapter(XMLLocalDateAdapter.class)
        private LocalDate startDate;
        @XmlJavaTypeAdapter(XMLLocalDateAdapter.class)
        private LocalDate endDate;
        private String title;
        private String description;

        public Position() {
        }

        public Position(LocalDate startDate, LocalDate endDate, String title, String description) {
            Objects.requireNonNull(startDate, "startDate mustn't be null");
            Objects.requireNonNull(endDate, "endDate mustn't be null");
            Objects.requireNonNull(title, "title mustn't be null");
            this.startDate = startDate;
            this.endDate = endDate;
            this.title = title;
            this.description = description != null ? description : "";
        }

        public Position(int startYear, Month startMonth, int endYear, Month endMonth, String title, String description) {
            this(DateUtil.of(startYear, startMonth), DateUtil.of(endYear, endMonth), title, description);
        }

        public Position(int startYear, Month startMonth, String title, String description) {
            this(DateUtil.of(startYear, startMonth), DateUtil.NOW, title, description);
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Position position = (Position) o;

            if (startDate != null ? !startDate.equals(position.startDate) : position.startDate != null) return false;
            if (endDate != null ? !endDate.equals(position.endDate) : position.endDate != null) return false;
            if (title != null ? !title.equals(position.title) : position.title != null) return false;
            return description != null ? description.equals(position.description) : position.description == null;

        }

        @Override
        public int hashCode() {
            int result = startDate != null ? startDate.hashCode() : 0;
            result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
            result = 31 * result + (title != null ? title.hashCode() : 0);
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
