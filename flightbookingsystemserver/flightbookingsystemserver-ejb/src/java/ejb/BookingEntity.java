/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Farhan
 */
@Entity(name="BookingData")
public class BookingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String bookingTime;
    private int totalAmount;
    @ManyToOne(cascade={CascadeType.ALL})
    private UserEntity userBooking = new UserEntity();
    @ManyToMany(cascade={CascadeType.ALL})
    private Collection<ScheduleEntity> schedules = new ArrayList<ScheduleEntity>();
    @ManyToMany(cascade={CascadeType.ALL})
    private Collection<PassengerEntity> passengers = new ArrayList<PassengerEntity>();
    @OneToOne(cascade={CascadeType.ALL})
    private PaymentEntity bookingPayment = new PaymentEntity();

    
    // Date and time formatting
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
    public BookingEntity() {
    }
    
    public void create(int totalAmount) {
        GregorianCalendar date = new GregorianCalendar();
	this.setBookingTime(dateFormat.format(date.getTime()));
        this.setTotalAmount(totalAmount);
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
        if (!(object instanceof BookingEntity)) {
            return false;
        }
        BookingEntity other = (BookingEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.BookingEntity[ id=" + id + " ]";
    }

    /**
     * @return the bookingTime
     */
    public String getBookingTime() {
        return bookingTime;
    }

    /**
     * @param bookingTime the bookingTime to set
     */
    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    /**
     * @return the totalAmount
     */
    public int getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return the userBooking
     */
    public UserEntity getUserBooking() {
        return userBooking;
    }

    /**
     * @param userBooking the userBooking to set
     */
    public void setUserBooking(UserEntity userBooking) {
        this.userBooking = userBooking;
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

    /**
     * @return the passengers
     */
    public Collection<PassengerEntity> getPassengers() {
        return passengers;
    }

    /**
     * @param passengers the passengers to set
     */
    public void setPassengers(Collection<PassengerEntity> passengers) {
        this.passengers = passengers;
    }

    /**
     * @return the bookingPayment
     */
    public PaymentEntity getBookingPayment() {
        return bookingPayment;
    }

    /**
     * @param bookingPayment the bookingPayment to set
     */
    public void setBookingPayment(PaymentEntity bookingPayment) {
        this.bookingPayment = bookingPayment;
    }
    
}
