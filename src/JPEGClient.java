import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import java.util.Scanner;
import java.awt.Graphics2D;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;




public class JPEGClient {
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    //private FileTransferProcessor ftp = null;
    private JPanel panel = null;
    private BufferedImage currentImage = null;
    private ArrayList <BufferedImage> images = new ArrayList<>();
    private Stack <byte[]> imagesInBytes = new Stack <byte[]>();
    //private byte[] imageBytes;
    //private int offset=0;

    private JPEGImageDecoder decoder; //JPEGCodec.createJPEGDecoder(bytesIn);

    public JPEGClient(String address, int port){

        try{
            socket=new Socket(address, port);
            System.out.println("Connected");

            in = new DataInputStream(System.in);

            out = new DataOutputStream(socket.getOutputStream());
        }

        catch(UnknownHostException u){
            System.out.println(u);
        }
        catch(IOException i){
            System.out.println(i);
        }

        String line = "";
        //ftp = new FileTransferProcessor(socket);
        // keep reading until "Over" is input
        System.out.println("Type Get Images and then type Convert once for each image and edit the image until all images have been received");
        while (!line.equals("Over"))
        {
            try
            {
                line = in.readLine();
                if(line.equals("Convert")){
                    String ans1;
                    System.out.println("Add text to the image that is being converted? Type yes or no.");
                    ans1 = in.readLine();
                    if(ans1.equals("yes")){
                        System.out.println("Type a caption to add to the image!");
                        String ans2;
                        ans2 = in.readLine();
                        getImage(ans2);
                    }else if(ans1.equals("no")){
                        getImage(""); //empty
                    }else{
                        System.out.println("Invalid entry, printing image without caption.");
                        getImage(""); //empty
                    }
                    if(images.size() == 5) {
                        System.out.println("All images converted and edited, now displaying them. Type close to close the connection!!");
                        for(int i=0; i<images.size(); i++) {
                            paintFromBufferToPanel(images.get(i));
                        }
                        //break;
                    }else {
                        System.out.println(5 - images.size() + " image(s) left to convert, type Convert to convert the next image!");
                    }
                }
                out.writeUTF(line);
                //ftp.receiveFile("example.jpeg");
            }
            catch(IOException i)
            {
                System.out.println(i);
                System.out.println("Image buffer is empty! Exiting.");
            }
        }

        // close the connection
        try
        {
            in.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public void addTextToImage(BufferedImage image, String message){
        Graphics g = image.getGraphics();
        g.setFont(new Font("TimesRoman", Font.BOLD, 20));
        g.setColor(Color.GREEN);
        g.drawString(message, 100, 100);

        g.dispose();

        try {
            ImageIO.write(image, "png", new File("textAdded.png"));
        }catch (Exception e){

        }


        // g.dispose;
    }


    public void paintFromBufferToPanel(BufferedImage _image){
        this.panel = new JPanel();

        JLabel label = new JLabel(new ImageIcon(_image));
        panel.add(label);

        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Decoded Image Displayer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        frame.pack();
        frame.setVisible(true);

        int x = 1000; //milliseconds to show image for, change here if needed
        try {
            Thread.sleep(x);
        }
        catch(Exception e){

        }
        frame.setVisible(false);
    }

    public void getImage(String message){
        try {
            DataInputStream dIn = new DataInputStream(socket.getInputStream());
            int length = dIn.readInt();
            if(length>0){ //receive data from server
                byte[] unCompressedImage = new byte[length];
                dIn.read(unCompressedImage,0,unCompressedImage.length); //read in stuff from dIn
                //offset += unCompressedImage.length;
                //imageBytes = unCompressedImage;
                imagesInBytes.push(unCompressedImage);
            }else{
                throw new Exception("Not found");
            }

            InputStream in = new ByteArrayInputStream(imagesInBytes.pop()); //push byte array into input stream
            decoder = JPEGCodec.createJPEGDecoder(in); //set up decoder


            long startTime = System.nanoTime();
            BufferedImage decodedImage = decoder.decodeAsBufferedImage(); //decode
            long endTime = System.nanoTime();

            System.out.println("Decoding took " + (endTime-startTime) + " nanoseconds");

            System.out.println("Decoded and saved in directory as uncompressed BMP!");
            //BufferedImage recImage = ImageIO.read(in);

            BufferedImage imgToAdd = decodedImage;

            addTextToImage(imgToAdd, message);

            images.add(imgToAdd);

            this.currentImage=imgToAdd;



            ImageIO.write(decodedImage, "bmp", new File("decoded.bmp"));

            /*this.currentImage = ImageIO.read(new File("example.jpeg"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(currentImage, "jpeg", baos);
            baos.flush();
            byte[] imageInBytes = baos.toByteArray();
            baos.close();

            InputStream in = new ByteArrayInputStream(imageInBytes);
            BufferedImage revertedImage = ImageIO.read(in);

            this.currentImage=revertedImage;

            ImageIO.write(revertedImage, "jpg", new File(
                    "reverted.jpg"));
*/
           /* for(int i=0; i<imageInBytes.length; i++){
                System.out.print(imageInBytes[i]);
            }*/
        }
        catch(Exception e){
            System.out.println("Image not found :(");
        }

    }



    public static void main(String[] args) {
        // write your code here
        JPEGClient client = new JPEGClient ("127.0.0.1", 5000);
    }
}
