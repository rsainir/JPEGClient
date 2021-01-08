import javax.imageio.ImageIO;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;


public class JPEGServer {
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private FileOutputStream fos;
    private BufferedImage image = null;

    private BufferedImage images[] = new BufferedImage[5]; //array of the jpegs we want to send to the client
    private Stack<BufferedImage> imageStack = new Stack<BufferedImage>();
    private JPanel panel = null;

    public JPEGServer(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server Started on port " + port);
            System.out.println("Waiting for client to connect...");

            socket = server.accept();
            System.out.println("Client accepted");

            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            String line = "";
            initJpeg(); //fill stack
            while (!line.equals("Over"))
            {
                try
                {
                    line = in.readUTF();
                    if(line.equals("Get Images")){
                        for(int i=0; i< imageStack.capacity(); i++) {
                            sendJPEG();
                        }
                        System.out.println("5 images sent to client!");
                    }

                    System.out.println(line);

                }
                catch(IOException i)
                {
                    System.out.println(i);
                }
            }

            System.out.println("Closing connection");

            socket.close();
            in.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }

    }
    public void initJpeg(){
        try {
            imageStack.push(ImageIO.read(new File("flower.jpeg")));//fill stack
            imageStack.push(ImageIO.read(new File("example.jpeg")));
            imageStack.push(ImageIO.read(new File("mount.jpeg")));
            imageStack.push(ImageIO.read(new File("balls.jpg")));
            imageStack.push(ImageIO.read(new File("planet.jpg")));

        }catch(IOException e){
            System.out.println("File not found");
        }
    }
    public void sendJPEG(){ //method to load the images in
        try {

            this.image = imageStack.pop();

            ByteArrayOutputStream baos = new ByteArrayOutputStream(); //output stream has a limit for number of bytes being sent, cant use files that are too big
            ImageIO.write(image, "jpeg", baos);

            baos.flush();
            byte[] imageInBytes = baos.toByteArray();//now we have image in bytes
            baos.close();

            //send through data buffer
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

            dOut.writeInt(imageInBytes.length); // write length of the message
            dOut.write(imageInBytes);           // write the message

            System.out.println("Sent all Images successfully");

        }
        catch (Exception e){
            System.out.println("Error: JPEG Not Sent by Server!");
        }
    }

    public static void main(String args[]){
        JPEGServer server = new JPEGServer(5000);

        System.out.println("reached");
    }
}
