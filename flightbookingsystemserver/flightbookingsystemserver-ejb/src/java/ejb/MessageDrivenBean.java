/*
 * MessageDrivenBean.java
 */
package ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@MessageDriven(mappedName = "jms/Topic", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "MessageDrivenBean"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "MessageDrivenBean")
})

public class MessageDrivenBean implements MessageListener {
    @PersistenceContext()
    EntityManager em;    
    RequestEntity request;
    UserEntity user;
    @Resource(mappedName = "jms/TopicConnectionFactory")
    private ConnectionFactory topicConnectionFactory;
    private Random processingTime = new Random();

    public MessageDrivenBean() {}
    
    public void createRequest(String userName, String content, String status, String comment) {
        System.out.println("createRequest()");
        user = em.find(UserEntity.class, userName);
        if(user != null) {
        request = new RequestEntity();
        request.create(content, status, comment);
        request.setUserRequest(user);
        
        if(user.getRequests() != null) {
            Collection<RequestEntity> reqList = user.getRequests();
            reqList.add(request);
            user.setRequests(reqList);
        }
        else {
            Collection<RequestEntity> reqList = new ArrayList<RequestEntity>();
            reqList.add(request);
            user.setRequests(reqList);
        }      
        
      }
    }
    
    
    public void onMessage(Message inMessage) {
        MapMessage msg = null;
        try {
            if (inMessage instanceof MapMessage) {
                msg = (MapMessage) inMessage;
                Thread.sleep(processingTime.nextInt(5) * 1000);
                setUpEntities(msg);
            } else {
                System.out.println("MessageDrivenBean.onMessage: " + 
                  "Message of wrong type: " + inMessage.getClass().getName());
            }
        } catch (InterruptedException ie) {
            System.out.println("MessageDrivenBean.onMessage: " +
                               "InterruptedException: " + ie.toString());
        } catch (Throwable te) {
            System.out.println("MessageDrivenBean.onMessage: Exception: " + 
              te.toString());
        }
    }

    void setUpEntities(MapMessage msg) {
        int num = 0;        
        String pUserName       = null;
        String pContent       = null; 
        String pStatus        = null; 
        String pComment       = null;
        Connection connection         = null;
        Session session               = null;
        MessageProducer producer      = null;
        MapMessage replyMsg           = null;
        Destination replyDest         = null;
        String replyCorrelationMsgId  = null;
        boolean done                  = false; 
        Session queueSession          = null;
        MessageProducer queueProducer = null;
        TextMessage message           = null;
    
        try {
            pUserName  = msg.getString("Username");
            pContent   = msg.getString("Content");
            pStatus    = msg.getString("Status");
            pComment   = msg.getString("Comment");
            
            System.out.println("MessageDrivenBean.setUpEntities:" +
              " Request received." );

                 try {
                    System.out.println("MessageDrivenBean.setUpEntities: " +
                      "Creating new request." );
                    System.out.println(pUserName);
                    System.out.println(pContent);
                    System.out.println(pStatus);
                    System.out.println(pComment);
                    createRequest(pUserName, pContent, pStatus, pComment);
                } catch (Exception e) {
                    System.out.println("MessageDrivenBean.setUpEntities: " + 
                      "Could not create request");
                }               
        
        } catch (IllegalArgumentException iae) {
            System.out.println("MessageDrivenBean.setUpEntities: " +
                               "No entity found");
        } catch (Exception e) {
            System.out.println("MessageDrivenBean.setUpEntities: " + 
                               "em.find failed without throwing " +
                               "IllegalArgumentException");
        }
        
        try {
            connection      = topicConnectionFactory.createConnection();
        } catch (Exception ex) {
            System.out.println("MessageDrivenBean.setUpEntities: " +
              "Unable to connect to JMS provider: " + ex.toString());
        }
        
        try {
            // Call createReplyMsg to construct the reply.
            replyDest = msg.getJMSReplyTo();
            replyCorrelationMsgId = msg.getJMSMessageID();
            session  = connection.createSession(true, 0);
            producer = session.createProducer(replyDest);
            replyMsg = createReplyMsg(session, replyCorrelationMsgId);
            producer.send(replyMsg);
            System.out.println("MessageDrivenBean.setUpEntities: " +
              "Sent reply message for request");
        } catch (JMSException je) {
            System.out.println("MessageDrivenBean.setUpEntities: " + 
              "JMSException: " + je.toString());
        } catch (Exception e) {
            System.out.println("MessageDrivenBean.setUpEntities: " + 
                          "Exception: " + e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException je) {
                    System.out.println("MessageDrivenBean.setUpEntities: " + 
                      "JMSException: " + je.toString());
                }
                connection = null;
            }
        }
    }

    // The createReplyMsg method composes the reply message
    private MapMessage createReplyMsg(Session session, String msgId) 
        throws JMSException {
        MapMessage replyMsg = session.createMapMessage();
        replyMsg.setString("Content", request.getContent());
        replyMsg.setString("Status", request.getStatus());
        replyMsg.setString("Comment", request.getComment());
        replyMsg.setJMSCorrelationID(msgId);
        return replyMsg;
    }   
}