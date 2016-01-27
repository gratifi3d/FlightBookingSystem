/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Farhan
 */
@Entity(name="FlightData")
public class FlightEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String flightNumber;
    private String departureCity;
    private String arrivalCity;
    private String aircraftType;
    private int totalSeats;
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="flightSchedule")
    private Collection<ScheduleEntity> schedules = new ArrayList<ScheduleEntity>();

    public FlightEntity() {
    }
    
    public void create(String flightNumber, String departureCity, String arrivalCity, String aircraftType, int totalSeats) {
        this.setFlightNumber(flightNumber);
        this.setDepartureCity(departureCity);
        this.setArrivalCity(arrivalCity);
        this.setAircraftType(aircraftType);
        this.setTotalSeats(totalSeats);
    }
    
    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightNumber != null ? flightNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightNumber fields are not set
        if (!(object instanceof FlightEntity)) {
            return false;
        }
        FlightEntity other = (FlightEntity) object;
        if ((this.flightNumber == null && other.flightNumber != null) || (this.flightNumber != null && !this.flightNumber.equals(other.flightNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.FlightEntity[ id=" + flightNumber + " ]";
    }

    /**
     * @return the departureCity
     */
    public String getDepartureCity() {
        return departureCity;
    }

    /**
     * @param departureCity the departureCity to set
     */
    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    /**
     * @return the arrivalCity
     */
    public String getArrivalCity() {
        return arrivalCity;
    }

    /**
     * @param arrivalCity the arrivalCity to set
     */
    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    /**
     * @return the aircraftType
     */
    public String getAircraftType() {
        return aircraftType;
    }

    /**
     * @param aircraftType the aircraftType to set
     */
    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    /**
     * @return the totalSeats
     */
    public int getTotalSeats() {
        return totalSeats;
    }

    /**
     * @param totalSeats the totalSeats to set
     */
    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    /**
     * @return the schedules
     */
    public Collection<ScheduleEntity> getSchedules() {
        return schedules;
    }

    /**
     * @param schedules the schedules to set
     */
    public void setSchedules(Collection<ScheduleEntity> schedules) {
        this.schedules = schedules;
    }
    
}
