/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Farhan
 */
@Entity(name="PassengerData")
public class PassengerEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String passportNumber;
    private String name;
    private String gender;
    private String dateOfBirth;
    
    public PassengerEntity() {
    }
    
    public void create(String passportNumber, String name, String gender, String dateOfBirth) {
        this.setPassportNumber(passportNumber);
        this.setName(name);
        this.setGender(gender);
        this.setDateOfBirth(dateOfBirth);
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (passportNumber != null ? passportNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the passportNumber fields are not set
        if (!(object instanceof PassengerEntity)) {
            return false;
        }
        PassengerEntity other = (PassengerEntity) object;
        if ((this.passportNumber == null && other.passportNumber != null) || (this.passportNumber != null && !this.passportNumber.equals(other.passportNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.PassengerEntity[ id=" + passportNumber + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the dateOfBirth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth the dateOfBirth to set
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
}
