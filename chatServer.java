/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author bensw
 */
public class chatServer {
    static Vector<ClientHandler> ar = new Vector<>();
    public static void main(String[] args) throws IOException {
        ServerSocket chatServer = new ServerSocket(9080);
        
        while (true) {
            int i = 0;
            System.out.println("Waiting...");

            Socket client = chatServer.accept();

            System.out.println("Client " + client.getInetAddress() + " connected!");
            DataInputStream dis = new DataInputStream(client.getInputStream());
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            ClientHandler mtch = new ClientHandler(client, "client" + i, dis,dos,true);
            Thread t = new Thread(mtch);
            InetAddress addr = client.getInetAddress();
            ar.add(mtch);
            
            //assign each client to a thread
            Thread th = new Thread(new chat(client,addr));
            i++;
            t.start();
            //th.start();

         }
        
    
}
}
class ClientHandler implements Runnable 
{
    
    Scanner scn = new Scanner(System.in);
     String name;
    Socket c;
    boolean isloggedin;
    DataInputStream dis;
    DataOutputStream dos;
     
    // constructor
    public ClientHandler(Socket c,String name,DataInputStream dis, DataOutputStream dos, boolean isloggedin ){
            this.isloggedin=true;//set to false when inactive
            this.dis = dis;
            this.dos = dos;
            this.name = name;
            }
 public void run() {
        while (true) 
        {
            try{
                String in = dis.readUTF();
                if (in.equals("msg")){
                String recipient = "client1";
                String msg = dis.readUTF();
 
                // search for the recipient in the connected devices list.
                // ar is the vector storing client of active users
                for (ClientHandler mc : chatServer.ar) 
                {
                    // if the recipient is found, write on its
                    // output stream
                    //if (mc.name.equals(recipient) && mc.isloggedin==true) 
                    //{
                        System.out.println(name+" : "+msg);
                        mc.dos.writeUTF(name+" : "+msg);
                       // break;
                    //}
                }
                }

            }catch (IOException e){}
            }
        }
}

class chat implements Runnable {
    InetAddress IP;
    Socket client;

 public chat(Socket _client, InetAddress _IP){
    client = _client;
    IP = _IP;
 }
  public void run(){
      try{
      while(true){
            String in = new String();

            DataInputStream inFromClient = new DataInputStream(client.getInputStream());
            DataOutputStream outToClient = new DataOutputStream(client.getOutputStream());  
            FileReader reqFile1 = new FileReader("requests.txt"); 
            BufferedReader reqIn = new BufferedReader(reqFile1); 
//            FileReader friendFile1 = new FileReader("friends.txt"); 
//            BufferedReader friendIn = new BufferedReader(friendFile1);
//            int countFriends = 0;
//            String inLine = new String();
//            String user = inFromClient.readUTF();
//            while ((inLine = reqIn.readLine()) != null)
//                        {
//                            countFriends++;
//                            if (inLine.length() == 0)
//                            {
//                                countFriends--;
//                            }
//                        }
//                        reqFile1.close();
//                        reqIn.close();
//                        String inLine2 = new String();
//                        FileReader friendFile2 = new FileReader("friends.txt"); 
//                        BufferedReader friendIn2 = new BufferedReader(friendFile2); 
//                        for (int i = 0; i < countFriends/2;i++)
//                        {
//                            inLine = friendIn2.readLine();//friend1
//                            inLine2 = friendIn2.readLine();//friend2
//                            System.out.println("line: " + inLine + " line2 " + inLine2);
//                            if (inLine.equals(user)){
//                                outToClient.writeUTF(inLine2);
//                            }
//                            else if(inLine2.equals(user)){
//                                outToClient.writeUTF(inLine);
//                            }
//                        } 
            while (true){
                in = inFromClient.readUTF();
                System.out.println(in);
                switch (in) {
 
                    case "req":
                        String requestee = inFromClient.readUTF();
                        String requested = inFromClient.readUTF();
                        FileWriter reqFile = new FileWriter("requests.txt",true);
                        PrintWriter reqOut = new PrintWriter(reqFile,true);
                        reqOut.println(requestee);
                        reqOut.println(requested);
                        break;
                    case "accept":
                        String friend1 = inFromClient.readUTF();
                        String friend2 = inFromClient.readUTF();
                        FileWriter friendFile = new FileWriter("friends.txt",true);
                        PrintWriter friendOut = new PrintWriter(friendFile,true);
                        friendOut.println(friend1);
                        friendOut.println(friend2);
                        friendFile.close();
                        outToClient.writeUTF(friend1 + " is now friends with " + friend2);
                        FileWriter reqFile3= new FileWriter("requests.txt",true);//remove requests from file
                        PrintWriter reqOut3 = new PrintWriter(reqFile3,true);
                        break;
                    case "seeReq":
                        int count = 0;
                        String line = new String();
                        String username = inFromClient.readUTF();
                        while ((line = reqIn.readLine()) != null)
                        {
                            count++;
                            if (line.length() == 0)
                            {
                                count--;
                            }
                        }
                        reqFile1.close();
                        reqIn.close();
                        String line2 = new String();
                        FileReader reqFile2 = new FileReader("requests.txt"); 
                        BufferedReader reqIn2 = new BufferedReader(reqFile2); 
                        for (int i = 0; i < count/2;i++)
                        {
                            line = reqIn2.readLine();//requestee
                            line2 = reqIn2.readLine();//requested
                            System.out.println("line: " + line + " line2 " + line2);
                            if (line2.equals(username)){
                                System.out.println("Requestee: " + line);
                                outToClient.writeUTF(line);
                            }
                        }       break;
                    default:
                        break;
                }
            }
    }
  }catch(IOException e){}
}
}
