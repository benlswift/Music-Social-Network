/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.io.*;
import java.net.*;

/**
 *
 * @author bensw
 */
public class musicServer {
     public static void main(String args[]) throws IOException {
        ServerSocket ss = new ServerSocket(1999); 

         while(true){
                      Socket s = ss.accept(); 
                      System.out.println("Connected to " + s.getInetAddress());
                      DataInputStream dis = new DataInputStream(s.getInputStream()); 
                      DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                      Thread th = new Thread(new musicHandler(dis,dos,s));
                      th.start();
         }

          }
}
class musicHandler implements Runnable{
    DataInputStream dis;
    DataOutputStream dos;
    Socket socket;
    
    public  musicHandler(DataInputStream _dis, DataOutputStream _dos, Socket _socket){
    socket = _socket;
    dis = _dis;
    dos = _dos;
    }    
        public void run(){
            try{
                String in = dis.readUTF();
                if (in.equals("filein")){
                                      String filename = dis.readUTF();
                      FileOutputStream fos = new FileOutputStream("Music files/" + filename+".wav");//get file
                      byte[]buf = new byte[4092];//byte array
                      int n = 0;
                while((n = dis.read(buf)) != -1 && n!=3 ){//while there are bytes in array
                                                            //send them to the client
                        fos.write(buf,0,n);//write the bytes to client
                        fos.flush();//flush out any left overs
                        }
                            fos.close();
                        
                    
                    fos.close();
                }
                
                else if (in.equals("stream")){
                    String filename = dis.readUTF();
                    FileInputStream fis = new FileInputStream("Music files/" +filename);
                                    int n = 0;
                                    String str = "";  
                                    byte[] done = new byte[3];
                                    done = str.getBytes();
                  byte[]buf = new byte[4092];
              while((n =fis.read(buf)) != -1){
                  dos.write(buf,0,n);
                          dos.flush();
                    }
                    dos.write(buf,0,3);
                    
                    dos.flush();
                        dos.close();
                    
                }
                     
        
           
            }catch (IOException ie) { 
                       ie.printStackTrace(); 
                     }
        
        }
}

        
    
    


