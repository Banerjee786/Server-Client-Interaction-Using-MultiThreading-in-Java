
/**
 * Priyam Banerjee UTA ID : 1001529497
   Citations : 
   https://www.journaldev.com/741/java-socket-programming-server-client
   https://www.geeksforgeeks.org/introducing-threads-socket-programming-java/
   https://www.dreamincode.net/forums/topic/259777-a-simple-chat-program-with-clientserver-gui-optional/
   https://github.com/puranikamey/-Multi-threaded-Web-Server-and-Web-Client-using-Java-Sockets-
   https://compiler.javatpoint.com/opr/test.jsp?filename=CurrentDateTimeExample2
   https://docs.oracle.com/javase/7/docs/api/javax/swing/JFrame.html
   https://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
   https://www.youtube.com/watch?v=LzhNsd9ut_4
*/
import javax.swing.*;
import javafx.scene.*;
import java.awt.*;
import java.awt.event.*;

/*
 *  GUI for Client
 */
public class CGUI extends JFrame{
	//public JFrame frame;
	public JTextArea resp, responseToClient, event;
    // The port number
    public JScrollPane scrPane;
	public JTextField tPortNumber;
    //public Client clt;
	// server constructor that receive the port to listen to for connection as parameter
	CGUI(int port) {
        JPanel midPanel = new JPanel();
        JFrame frame = new JFrame("Client");
        responseToClient = new JTextArea("Server Response \n",20,58);
        responseToClient.setEditable(false);
        //frame.add(new JScrollPane(responseToClient));
        scrPane = new JScrollPane(responseToClient);
        scrPane.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        midPanel.add(scrPane);
        frame.add(midPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        //msgfromserver("Server Response"+"\n"); 
    }	
    /*
    class ActionClientStart implements ActionListener{
    public void actionPerformed(ActionEvent e){
        
        JFrame frame2 = new JFrame("Started Client");
        frame2.setVisible(true);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setSize(400, 600);
        responseToClient = new JTextArea(200,400);
        responseToClient.setEditable(true);
        frame2.add(new JScrollPane(responseToClient));
        //appendResponse(); 
        msgfromserver("Server Response \n");
        }
    }
    */
    public void msgfromserver(String str) {
        responseToClient.append(str+"\n");
    }
    /* Below is the method for appending message from server */
    public static void main(String[] arg) {
            new CGUI(5056);
    }
}