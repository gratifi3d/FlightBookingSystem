/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Farhan
 */
@Entity(name="ScheduleData")
public class ScheduleEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String departureTime;
    private String arrivalTime;
    private int price;
    private int availableSeats;
    private boolean hasBooking;
    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private FlightEntity flightSchedule = new FlightEntity();

    public ScheduleEntity() {
    }
    
    public void create(String departureTime, String arrivalTime, int price, int availableSeats, boolean hasBooking) {
        this.setDepartureTime(departureTime);
        this.setArrivalTime(arrivalTime);
        this.setPrice(price);
        this.setAvailableSeats(availableSeats);
        this.setHasBooking(hasBooking);
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ScheduleEntity)) {
            return false;
        }
        ScheduleEntity other = (ScheduleEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.ScheduleEntity[ id=" + id + " ]";
    }

    /**
     * @return the departureTime
     */
    public String getDepartureTime() {
        return departureTime;
    }

    /**
     * @param departureTime the departureTime to set
     */
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    /**
     * @return the arrivalTime
     */
    public String getArrivalTime() {
        return arrivalTime;
    }

    /**
     * @param arrivalTime the arrivalTime to set
     */
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * @return the availableSeats
     */
    public int getAvailableSeats() {
        return availableSeats;
    }

    /**
     * @param availableSeats the availableSeats to set
     */
    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    /**
     * @return the hasBooking
     */
    public boolean isHasBooking() {
        return hasBooking;
    }

    /**
     * @param hasBooking the hasBooking to set
     */
    public void setHasBooking(boolean hasBooking) {
        this.hasBooking = hasBooking;
    }

    /**
     * @return the flightSchedule
     */
    public FlightEntity getFlightSchedule() {
        return flightSchedule;
    }

    /**
     * @param flightSchedule the flightSchedule to set
     */
    public void setFlightSchedule(FlightEntity flightSchedule) {
        this.flightSchedule = flightSchedule;
    }
    
}
