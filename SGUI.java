
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
import java.awt.*;
import java.awt.event.*;
import javafx.scene.*;

/*
 *  GUI for Server
 */
public class SGUI extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	// the start-stop button variable
	private JButton stopStart , exitBtn;
	// JTextArea for the text areas and the events
	private JTextArea chat, event;
	// The port number
	private JTextField tPortNumber;
	public JTextArea responseToClient;
    // The port number
    public JScrollPane scrPane;
	// server
	private Server server;
	public boolean clientStatus = true;
	
	
	// server constructor that receive the port to listen to for connection as parameter
	SGUI(int port) {
		super("Server");
		server = null;
		// in the NorthPanel the PortNumber the Start-Stop button
		JPanel north = new JPanel();
		north.add(new JLabel("Port number: "));
		tPortNumber = new JTextField(" " + port);
		north.add(tPortNumber);
		// Defining Start Button
		stopStart = new JButton("Start");
		stopStart.addActionListener(this);
		north.add(stopStart);
		add(north, BorderLayout.NORTH);
		
		// The Client Connection Details and Server Event
		JPanel center = new JPanel(new GridLayout(2,1));
		chat = new JTextArea(80,80); // Defining TextArea
		chat.setEditable(false);
		appendRoom("Client Connection.\n");
		center.add(new JScrollPane(chat));
		event = new JTextArea(80,80); // Defining TextArea

		event.setEditable(false);
		appendEvent("Server Event.\n");
		center.add(new JScrollPane(event));	
		add(center);
		setSize(400, 600);
		setVisible(true);
	}	
	
	void newClientConnected(String clientName){
		JPanel midPanel = new JPanel();
        JFrame frame = new JFrame(clientName);
		responseToClient = new JTextArea("Server Response \n",20,58);
		exitBtn = new JButton("Exit");
        responseToClient.setEditable(false);
        //frame.add(new JScrollPane(responseToClient));
		scrPane = new JScrollPane(responseToClient);
		scrPane.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		//scrPane.add(exitBtn);
		midPanel.add(scrPane);
		midPanel.add(exitBtn);
		frame.add(midPanel);
		//frame.add(exitBtn);
		frame.pack();
		exitBtn.addActionListener(this);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}
	void appendtoClientResponse(String str){
		responseToClient.append(str);
	}
    /* The function below appends the Client Request to a string */
    void appendRoom(String str) {
		chat.append(str);
		//chat.setCaretPosition(chat.getText().length() - 1);
    }
    /* The function below appends the Server Events to a string */
	void appendEvent(String str) {
		event.append(str);
		//event.setCaretPosition(chat.getText().length() - 1);	
	}
    public static void main(String[] arg) {
		// start server default port 5056
		new SGUI(5056);
    }	
    public void windowClosing(WindowEvent e) {
		// if Server exist
		if(server != null) {
			try {
                System.out.println("Hi");
				//server.stop();			// ask the server to close the conection
			}
			catch(Exception eClose) {
			}
			server = null;
		}
		// dispose the frame
		dispose();
		System.exit(0);
    }
    /* ActionListener method deciding the action to perform on button click */
    public void actionPerformed(ActionEvent e) {
		// if running we have to stop
		if(e.getSource() == exitBtn){
			clientStatus = false;
			responseToClient.append("Client is shutting down......\n");
			//appendtoClientResponse("Client is shutting down");
			JOptionPane.showMessageDialog(this, "Client has shut down");
		}
		if(e.getSource() == stopStart){
		if(server != null) {
			//server.stop();
			server = null;
			tPortNumber.setEditable(true);
			stopStart.setText("Start");
			return;
		}
      	// OK start the server	
		int port;
		try {
			port = Integer.parseInt(tPortNumber.getText().trim());
		}
		catch(Exception er) {
			//appendEvent("Invalid port number");
			return;
		}
		// ceate a new Server
		server = new Server(port, this);
		// and start it as a thread
		new ServerRunning().start();
		stopStart.setText("Stop");
		tPortNumber.setEditable(false);
		}	
	}
		boolean checkIfClientDisconnected(){
			return clientStatus;
		}
    /* Sub-class which helps in concurrent thread creation */
    class ServerRunning extends Thread {
		public void run() {
            server.start();         // should execute until if fails
			// the server failed
			stopStart.setText("Start");
			tPortNumber.setEditable(true);
			//appendEvent("Server crashed\n");
			server = null;
		}
	}
}