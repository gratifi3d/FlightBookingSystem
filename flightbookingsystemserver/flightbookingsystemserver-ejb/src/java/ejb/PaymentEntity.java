/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Farhan
 */
@Entity(name="PaymentData")
public class PaymentEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String cardType;
    private String cardNumber;
    private String cardHolderName;
    private String paymentTime;

    // Date and time formatting
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    public PaymentEntity() {

    }
    
    public void create(String cardType, String cardNumber, String cardHolderName) {
        GregorianCalendar date = new GregorianCalendar();
        this.setPaymentTime(dateFormat.format(date.getTime()));
        this.setCardType(cardType);
        this.setCardNumber(cardNumber);
        this.setCardHolderName(cardHolderName);
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
        if (!(object instanceof PaymentEntity)) {
            return false;
        }
        PaymentEntity other = (PaymentEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.PaymentEntity[ id=" + id + " ]";
    }

    /**
     * @return the cardType
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * @param cardType the cardType to set
     */
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    /**
     * @return the cardNumber
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * @param cardNumber the cardNumber to set
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * @return the cardHolderName
     */
    public String getCardHolderName() {
        return cardHolderName;
    }

    /**
     * @param cardHolderName the cardHolderName to set
     */
    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    /**
     * @return the paymentTime
     */
    public String getPaymentTime() {
        return paymentTime;
    }

    /**
     * @param paymentTime the paymentTime to set
     */
    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }
    
}
