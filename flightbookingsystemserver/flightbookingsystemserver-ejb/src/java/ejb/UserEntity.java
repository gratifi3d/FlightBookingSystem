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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Farhan
 */
@Entity(name="UserData")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String userName;
    private String password;
    private String contactNumber;
    private String emailAddress;
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="userBooking")
    private Collection<BookingEntity> bookings = new ArrayList<BookingEntity>();
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="userRequest")
    private Collection<RequestEntity> requests = new ArrayList<RequestEntity>();
    
    // Creates new instance of UserEntity
    public UserEntity() {
    }
    
    public void create(String userName, String password, String contactNumber, String emailAddress) throws NoSuchAlgorithmException {
        this.setUserName(userName);
        this.setPassword(password);
        this.setContactNumber(contactNumber);
        this.setEmailAddress(emailAddress);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userName != null ? userName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserEntity)) {
            return false;
        }
        UserEntity other = (UserEntity) object;
        if ((this.userName == null && other.userName != null) || (this.userName != null && !this.userName.equals(other.userName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.UserEntity[ userName=" + userName + " ]";
    }
    
    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     * @throws java.security.NoSuchAlgorithmException
     */
    public void setPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
 
        byte byteData[] = md.digest();
 
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
 
        this.password = sb.toString();
    }

    /**
     * @return the contactNumber
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * @param contactNumber the contactNumber to set
     */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * @return the bookings
     */
    public Collection<BookingEntity> getBookings() {
        return bookings;
    }

    /**
     * @param bookings the bookings to set
     */
    public void setBookings(Collection<BookingEntity> bookings) {
        this.bookings = bookings;
    }

    /**
     * @return the requests
     */
    public Collection<RequestEntity> getRequests() {
        return requests;
    }

    /**
     * @param requests the requests to set
     */
    public void setRequests(Collection<RequestEntity> requests) {
        this.requests = requests;
    }
    
}
