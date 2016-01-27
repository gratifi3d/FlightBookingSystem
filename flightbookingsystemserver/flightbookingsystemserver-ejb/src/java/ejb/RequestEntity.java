/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
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
@Entity(name="RequestData")
public class RequestEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String time;
    private String content;
    private String status;
    private String comment;
    @ManyToOne(cascade={CascadeType.ALL})
    private UserEntity userRequest = new UserEntity();
    
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public RequestEntity() {
    }
    
    public void create(String content, String status, String comment) {
        GregorianCalendar date = new GregorianCalendar();
        this.setTime(dateFormat.format(date.getTime()));
        this.setContent(content);
        this.setStatus(status);
        this.setComment(comment);
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
        if (!(object instanceof RequestEntity)) {
            return false;
        }
        RequestEntity other = (RequestEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.RequestEntity[ id=" + id + " ]";
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the userRequest
     */
    public UserEntity getUserRequest() {
        return userRequest;
    }

    /**
     * @param userRequest the userRequest to set
     */
    public void setUserRequest(UserEntity userRequest) {
        this.userRequest = userRequest;
    }
    
}
