  
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
import java.text.*; 
import java.util.*; 
import java.net.*; 
import java.text.SimpleDateFormat;
import java.util.Date;
import sun.awt.*;
  
// Server class 
public class Server
{ 
    
    //private static int uniqueId;
	private ArrayList<ClientHandler> al; // Array List for listing out all unique clients
    private SGUI sg; // SGUI variable (will be used for referencing between data variables and Swing Widget)
    private CGUI cg;
    private int port;

    //Contructor
    public Server(int port) {
        //this.port = port;
        this(port, null);
    }
    //Constructor for SGUI
    public Server(int port, SGUI sg) {
		// GUI or not
        this.sg = sg;
        // the port
		this.port = port;
		// ArrayList for the Client list
		al = new ArrayList<ClientHandler>();
    }
    /* Method for starting the Server Socket */
    public void start() {
        try{
        ServerSocket ss = new ServerSocket(5056);
        
        display(".............................................\n" + "Server Started.. waiting for Request\n");
        System.out.println(".............................................\n" + "Server Started.. waiting for Request\n");
        while (true)  
        { 
            Socket s = null; 
            try 
            { 
                // socket object to receive incoming client requests 
                s = ss.accept(); 
                String clientname = "Client_";
                clientname += s.getPort(); // Unique name of the client
                System.out.println("A new client is connected : " + clientname); 
                sg.newClientConnected(clientname);
                // obtaining input and out streams 
                DataInputStream dis = new DataInputStream(s.getInputStream()); 
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());   
                System.out.println("Assigning new thread for  client : "+s); 
                display("A new client is connected : " + clientname);
                display("Assigning new thread for  client : "+s);
                //dos.writeUTF(str);
                // create a new thread object 
                Thread t = new ClientHandler(s, dis, dos, sg, cg); 
  
                // Invoking the start() method 
                t.start(); 
                  
            } 
            catch (Exception e){ 
                //s.close(); 
                ss.close();
                e.printStackTrace(); 
                } 
            } 
        }
        catch(IOException e){
            String msg = "Exception on new Server socket" + e + "\n";
            System.out.println(msg);
        } 
    }
    /* Method to stop the server */
    public void stop() {
		// connect to myself as Client to exit statement 
		// Socket socket = serverSocket.accept();
		try {
            
			//new Socket("localhost", 5056);
		}
		catch(Exception e) {
			
		}
	}
    /* Method to display the Client Connections to  */
    private void display(String msg) {
		if(sg == null)
            System.out.println();
		else
			sg.appendRoom(msg+"\n");
    }
    private void displayHTTPResponseToClient(String msg){
        if(cg == null)
            System.out.println("cg IS NULL");
        else
            cg.msgfromserver(msg+"\n");
    }

    public static void main(String[] args) throws IOException  
    { 
        int port = 5056;
        Server server = new Server(port);
        server.start();  // starting the server
    }
 
  
// ClientHandler class. This handles the concurrent clients
class ClientHandler extends Thread
{ 
    
    private SGUI sg;
    private CGUI cg;
    final DataInputStream dis; 
    final DataOutputStream dos; 
    final Socket s; 
    // Constructor 
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos,SGUI sg,CGUI cg)  
    { 
        this.s = s; 
        this.dis = dis; 
        this.dos = dos; 
        this.sg=sg;
        this.cg=cg;
    } 
    private void displayServerEvent(String msg) {
		if(sg == null)
			System.out.println();
        else
        {
            sg.appendEvent(msg+"\n");
        }
    }
    
    private void displayResponseToClient(String msg){
        if(this.sg == null)
            System.out.println("cg : "+cg);
        else
            sg.appendtoClientResponse(msg);
            //cg.msgfromserver(msg+"\n");
            //this.sg.appendEvent(msg+"\n");
        
    }
    
  
    @Override
    public void run()  
    { 
        String received; 
        String toreturn; 
        double rand_int;
        while (true)  
        { 
            try { 
                
                /* So the implementation is instead of going on forever we are
                 asking user what he wants. 'Exit' to terminate connection */
                dos.writeUTF("Press Enter to continue \n"+ 
                            "Type 'Exit' to terminate connection."); 
                received = dis.readUTF(); // receive the answer from client 
                rand_int = dis.readDouble();
                boolean chkClientStatus = sg.checkIfClientDisconnected();
                System.out.println("Client connected ? "+chkClientStatus);
                if(received.equals("Exit") || chkClientStatus == false) 
                {  
                    System.out.println("Client " + this.s + " sends exit..."); 
                    System.out.println("Closing this connection."); 
                    //this.s.close(); 
                    System.out.println("Connection closed");
                    sg.appendtoClientResponse("Client exited successfully"); 
                    break; 
                } 
                long sleeptime = (long)rand_int*1000;
                /* Below is the code for putting the thread to sleep */
                //try{Thread.sleep(sleeptime);}catch(InterruptedException e){System.out.println(e);}
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                /* Generating the HTTP Response */
                System.out.println("HTTP Request to Server\n"+received);
                displayServerEvent("HTTP Request to Server\n"+received); // Displays the HTTP Request in Server Event Pane
                String httpresponse = "";
                httpresponse += "HTTP /1.1 200 OK \r\n";  //http header message
                httpresponse += "SERVER: WEBSERVER_PRIYAM9497\r\n";
                httpresponse += "User-Agent : Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36\r\n";
                httpresponse += "Content : " + received.substring(3,6) + "\r\n";
				httpresponse += "Content-Type : text/html \r\n";
				httpresponse += "Content-Length: "+ received.substring(3,6).length() +"\r\n";
                httpresponse += "Connection: keep-alive \r\n";
                httpresponse += "Date : "+ formatter.format(date) + "\r\n";
                try{Thread.sleep(sleeptime);}catch(InterruptedException e){System.out.println(e);}
                displayResponseToClient("HTTP Response from Server\n"+httpresponse);
                dos.writeUTF("HTTP Response from Server\n"+httpresponse);
                dos.writeDouble(rand_int);
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
        try
        { 
            // closing resources 
            this.dis.close(); 
            this.dos.close(); 
            s.close();
            
        }catch(IOException e){ 
            e.printStackTrace(); 
        }
    } 
}
}