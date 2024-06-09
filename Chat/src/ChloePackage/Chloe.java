/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChloePackage;

import chat.Chat;
import chat.Member;
import chat.Message;
import java.net.*;
import java.io.*;
import java.util.*;

public class Chloe implements Runnable {

    Socket clientSocket;
    static ChloeGUI s_GUI;
    static int chloeNextNode;

    public Chloe(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());

            Object msg = "";
            while (!(msg = input.readObject()).toString().toLowerCase().equals("leave")) {
                if (msg.getClass().getSimpleName().equals("Member")) {
                    Member m = (Member) msg;
                    chloeNextNode = m.getRightNode();
                } else if (msg.getClass().getSimpleName().equals("Message")) {

                    Message m = (Message) msg;
                    if (m.isPriv() == true) {
                        System.out.println("Chloe received private message");
                        if (m.getReceiver().equals("Chloe")) {
                            System.out.println("Message belonged to chloe");
                            m.decryptMsg();
                            System.out.println("Message decrypted");
                            s_GUI.appendChloeScreen("(private) " + m.getSender() + ": " + m.getMsg());

                        } else {
                            System.out.println(m.getMsg()); //test encryption      
                            Socket toNext = new Socket("localhost", chloeNextNode);
                            ObjectOutputStream oos2 = new ObjectOutputStream(toNext.getOutputStream());
                            oos2.writeObject(m);
                            System.out.println("Message sent to " + chloeNextNode);
                            oos2.close();
                            toNext.close();
                        }
                    } else {
                        s_GUI.appendChloeScreen(m.getMsg());
                    }

                }
            }

            input.close();
            clientSocket.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket sSocket = new ServerSocket(5555);
            s_GUI = new ChloeGUI();
            s_GUI.setVisible(true);

            while (true) {
                Socket Sock = sSocket.accept();
                Thread newClient = new Thread(new Chloe(Sock));
                newClient.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
