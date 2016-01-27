/*
 * Main.java
 */
package requestmanagementsystemclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.jms.ConnectionFactory;
import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.Queue;
import javax.jms.MessageProducer;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Message;
import javax.jms.MapMessage;
import javax.jms.JMSException;
import javax.annotation.Resource;

public class Main {
    @Resource(mappedName = "jms/TopicConnectionFactory")
    private static ConnectionFactory topicConnectionFactory;
    @Resource(mappedName = "jms/Topic")
    private static Topic topic;
    
    static Object waitUntilDone      = new Object();
    static int    requestOutstanding = 0;

    public Main() {
    }
    
    public static void main(String[] args) {
        Connection topicConnection = null;
        Session session            = null;
        MapMessage message         = null;
        Queue replyQueue           = null;
        MessageProducer producer   = null;
        MessageConsumer consumer   = null;
      
        try {
            String choice = "";
            while(!choice.equals("0")) {
                topicConnection = topicConnectionFactory.createConnection();
                session = topicConnection.createSession(false, 
                                          Session.AUTO_ACKNOWLEDGE);
                replyQueue = session.createTemporaryQueue();
                consumer   = session.createConsumer(replyQueue);
                consumer.setMessageListener(new RequestListener());
                topicConnection.start();

                producer = session.createProducer(topic);

                message  = session.createMapMessage();
                message.setJMSReplyTo(replyQueue);

                System.out.println("\n****************************************");
                System.out.println("Welcome to the Request Management System");
                System.out.println("****************************************");
                
                System.out.println("* Enter 1 to Send a Request");
                System.out.println("* Enter 0 to Exit\n");
                choice = getString("Choice", null);
                
                if(choice.equals("1")) {
                System.out.println("\n*********************");
                System.out.println("Enter Request Details");
                System.out.println("*********************");                
                                String userName = getString("Username", null);  
                String content = getString("Content", null);  
                String status = getString("Status", null);    
                String comment = getString("Comment", null);
                    
                    message.setString("Username", userName);
                    message.setString("Content", content);
                    message.setString("Status", status);
                    message.setString("Comment", comment);

                    System.out.println("RMS Main: Request published");                
                    producer.send(message);
                    requestOutstanding++;

                    System.out.println("Main: Waiting for " + 
                                   requestOutstanding + " message(s)");            

                synchronized (waitUntilDone) {
                    waitUntilDone.wait();
                }      
            }
                else {
                    System.out.println("Exiting Request Management System...");
                }
            }
        } catch (Exception e) {
            System.err.println("RMS Main: Exception: " + 
                               e.toString());
        } finally {
            if (topicConnection != null) {
                try {
                    topicConnection.close();
                } catch (Exception e) {
                    System.out.println("RMS Main: " + 
                            "Close exception: " + e.toString());
                }
            }
        }
    }
    
    public static String getString(String attrName, String oldValue){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String stringValue = null;
        
        try {
            while(true) {
                System.out.print("Enter " + attrName + (oldValue==null?"":"(" + oldValue + ")") + " : ");
                stringValue = br.readLine();
                if(stringValue.length() != 0) {
                    break;
                } else if (stringValue.length() == 0 && oldValue != null) {
                    stringValue = oldValue;
                    break;
                }
                System.out.println("\nInvalid " + attrName + "...\n");
            }
        } catch(Exception ex) {
            System.out.println("\nSystem Error Message: " + ex.getMessage() + "\n");
        }
        return stringValue.trim();
    }

    static class RequestListener implements MessageListener {
        
        public void onMessage(Message message) {
            MapMessage msg = (MapMessage) message;
            
            System.out.println("RMSListener.onMessage(): " +
                               "Processing map messages...");
            try {
                System.out.println("Processing message: " + 
                                   msg.getJMSCorrelationID());
                System.out.println("Content: " + 
                                   msg.getString("Content"));
                System.out.println("Status: " + 
                                   msg.getString("Status"));
                System.out.println("Comment: " + 
                                   msg.getString("Comment"));
                requestOutstanding--;
            } catch (JMSException je) {
                System.out.println("RMSListener.onMessage(): " +
                                   "Exception: " + je.toString());
            }
            if (requestOutstanding == 0) {
                synchronized (waitUntilDone) {
                    waitUntilDone.notify();
                }
            } else {
                System.out.println("RMSListener: Waiting for " + 
                  requestOutstanding + " message(s)");
            }
        }
    }
}
