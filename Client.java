
/**
 * Priyam Banerjee UTA ID : 1001529497
   Citations : 
   https://www.journaldev.com/741/java-socket-programming-server-client
   https://www.geeksforgeeks.org/introducing-threads-socket-programming-java/
   https://www.dreamincode.net/forums/topic/259777-a-simple-chat-program-with-clientserver-gui-optional/
   https://github.com/puranikamey/-Multi-threaded-Web-Server-and-Web-Client-using-Java-Sockets-
   https://compiler.javatpoint.com/opr/test.jsp?filename=CurrentDateTimeExample2
*/
import java.io.*; 
import java.net.*; 
import java.util.Scanner; 
import java.util.Random; 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
// Client class 
public class Client extends JFrame 
{ 
    private ObjectInputStream sInput;		// to read from the socket
	private ObjectOutputStream sOutput;		// to write on the socket
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
	
	// the server, the port and the username
	private String server, username;
    private int port;
    private SGUI sg;
    public CGUI cg;
    public String received;
    
    public Client(int port){
        //this.port = port;
        this(port,null);
    }

    public Client(int port,CGUI cg){
        this.port = port;
        this.cg =cg;
    }

    /* Below method is to send the String message to Client GUI */
    public void display(String msg) {
		if(this.cg == null)
			System.out.println(msg);      // println in console mode
		else
			cg.msgfromserver(msg + "\n");		// append to the ClientGUI JTextArea (or whatever)
    }
    public void clientStart() throws IOException{
        try
        { 
            Scanner scn = new Scanner(System.in); 
            // getting localhost ip 
            InetAddress ip = InetAddress.getByName("localhost"); 
            // establish the connection with server port 5056 
            Socket s = new Socket(ip, 5056); 
            // obtaining input and out streams 
            dis = new DataInputStream(s.getInputStream()); 
            dos = new DataOutputStream(s.getOutputStream()); 
            // the following loop performs the exchange of 
            // information between client and client handler 
            while (true)  
            { 
                System.out.println(dis.readUTF()); 
                String tosend = scn.nextLine(); 
                int max = 15;
                int min = 5;
                String httpreq = "";
                /* Generating random numbers between 5 and 15 */
			    Random rand = new Random();
                int randm =  rand.nextInt((max - min) + 1) + min;
                double randomn = (double)randm;
                String random_num = Integer.toString(randm);
                /* Generating HTTP Request to be sent to the server */
                httpreq = "GET "+random_num+" "+  "HTTP/1.1 \r\n";     ///http request sent to the server with the filename.
			    httpreq += "Host: localhost:5056 \r\n";
			    httpreq += "Connection: keep-alive \r\n";
			    httpreq += "Cache-Control: max-age=0 \r\n";
			    httpreq += "Accept: text/html \r\n";
			    httpreq += "Accept-Encoding: gzip, deflate, sdch, br \r\n";
			    httpreq += "Accept-Language: en-US,en;q=0.8 \r\n";
                dos.writeUTF(httpreq); // HTTP Request sent
                dos.writeDouble(randomn); // Random Number sent but in (double)format
                  
                // If client sends exit,close this connection  
                // and then break from the while loop 
                if(tosend.equals("Exit")) 
                { 
                    System.out.println("Closing this connection : " + s); 
                    System.out.println("Connection closed"); 
                    dis.close();
                    dos.close();
                    s.close(); 
                    break; 
                } 
                
                  
                // printing date or time as requested by client 
                received = dis.readUTF(); 
                double rand_int = dis.readDouble();
                //msgToClientFromServer(received); // calling msgfromserver() method to display the message from server
                try{
                    
                }catch(Exception e){
                    e.printStackTrace();
                }
                System.out.println(received); 
                /* Below is for printing out the time (in secs) the server waited */
                try {
                    //String msg = (String) sInput.readObject();
                    String msg = received;
					// if console mode print the message and add back the prompt
					if(this.cg == null) {
                        //cg.msgfromserver(msg);
						System.out.println();
					}
					else {
						cg.msgfromserver(msg+"\n");
					}
				}
				catch(Exception e) {
                    display("Server has close the connection: " + e);
				}
                System.out.println("Server waited for "+rand_int+" secs"); 
                
            } 
              
            // closing resources 
            scn.close(); 
            dis.close(); 
            dos.close(); 
            s.close();
            
        }catch(SocketException e){ 
            e.printStackTrace(); 
        }
    }
    public String returnHTTPResponse(){
            return this.received;
    }
    
    public static void main(String[] args) throws IOException  
    {         
        Client cl = new Client(5056);
        cl.clientStart();
    }
    class ListenFromServer extends Thread {

		public void run() {
			while(true) {
				try {
                    //String msg = (String) sInput.readObject();
                    String msg = received;
					// if console mode print the message and add back the prompt
					if(cg == null) {
						System.out.println("Hello Msg : "+msg);
						System.out.print("> ");
					}
					else {
						cg.msgfromserver(msg+"\n");
					}
				}
				catch(Exception e) {
                    display("Server has close the connection: " + e);
                    /*
					if(cg != null) 
                        cg.connectionFailed();
                    */
					break;
				}
				/* can't happen with a String object but need the catch anyhow
				catch(ClassNotFoundException e2) {
                }
                */
			}
		}
	} 
}