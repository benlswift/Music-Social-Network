/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

/**
 *
 * @author bensw
 */
 
import java.io.*;
import java.util.*;
import java.net.*;
 
// Server class
public class ServerChat 
{
 
    // Vector to store active clients
    static Vector<ClientHandler2> ar = new Vector<>();
     
    // counter for clients
    static int i = 0;
 
    public static void main(String[] args) throws IOException 
    {
        ServerSocket ss = new ServerSocket(9078);
         
        // running infinite loop for getting
        // client request
        while (true) 
        {
            // Accept the incoming request
            Socket s = ss.accept();
 
            System.out.println("New client request received : " + s);
             
            // obtain input and output streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
             
            System.out.println("Creating a new handler for this client...");
            String name = dis.readUTF();
            // Create a new handler object for handling this request.
            ClientHandler2 mtch = new ClientHandler2(s,name, dis, dos,true);
 
            // Create a new Thread with this object.
            Thread t = new Thread(mtch);
             
            System.out.println("Adding this client to active client list");
            

            ar.add(mtch);
            // start the thread.
            
            t.start();

            i++;
 
        }
    }
}
 
// ClientHandler class
class ClientHandler2 implements Runnable 
{
    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;
     
    // constructor
    public ClientHandler2(Socket s, String name, DataInputStream dis, DataOutputStream dos, boolean isloggedin) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin=true;
    }
 
    @Override
    public void run() {
 
        while (true) 
        {
            
            try
            {
                
                 
                // break the string into message and recipient part
               // StringTokenizer st = new StringTokenizer(received, "#");
                String recipient = dis.readUTF();
                //recieve user sending & recieving message
                //wait for message
                String MsgToSend = dis.readUTF();
                System.out.println(name + recipient + MsgToSend);
                // search for the recipient in the connected devices list.

                for (ClientHandler2 mc : ServerChat.ar) 
                {//cycle through Server.ar 
                    //look for client name & friends name
                    //send to client and friend!
                    //BOSH!!
                    //System.out.println("name: " +mc.name);
                    
                    // if the recipient is found, write on its
                    // output stream
                    System.out.println(mc.name);
                    if (mc.name.equals(recipient) && mc.isloggedin==true) 
                    {
                        mc.dos.writeUTF(name+" : "+MsgToSend);
                        this.dos.writeUTF(name+" : "+MsgToSend);
                        System.out.println("sent");
                        break;
                    }
                }
            } catch (IOException e) {
                 
                e.printStackTrace();
            }
             
        }
//        try
//        {
//            // closing resources
//            this.dis.close();
//            this.dos.close();
//             
//        }catch(IOException e){
//            e.printStackTrace();
//        }
    }
}