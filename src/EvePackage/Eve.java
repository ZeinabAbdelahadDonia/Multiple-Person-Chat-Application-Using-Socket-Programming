/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EvePackage;

import chat.Chat;
import chat.Member;
import chat.Message;
import java.net.*;
import java.io.*;
import java.util.*;

public class Eve implements Runnable {

    Socket clientSocket;
    static EveGUI e_GUI;
    static int eveNextNode;

    public Eve(Socket clientSocket) {
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
                    eveNextNode = m.getRightNode();
                } else if (msg.getClass().getSimpleName().equals("Message")) {

                    Message m = (Message) msg;
                    if (m.isPriv() == true) {
                        System.out.println("Eve received private message");
                        if (m.getReceiver().equals("Eve")) {
                            System.out.println("Message belonged to eve");
                            m.decryptMsg();
                            System.out.println("Message decrypted");
                            e_GUI.appendEveScreen("(private) " + m.getSender() + ": " + m.getMsg());

                        } else {
                            System.out.println(m.getMsg()); //test encryption      
                            Socket toNext = new Socket("localhost", eveNextNode);
                            ObjectOutputStream oos2 = new ObjectOutputStream(toNext.getOutputStream());
                            oos2.writeObject(m);
                            System.out.println("Message sent to " + eveNextNode);
                            oos2.close();
                            toNext.close();
                        }
                    } else {
                        e_GUI.appendEveScreen(m.getMsg());
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
            ServerSocket sSocket = new ServerSocket(4444);
            e_GUI = new EveGUI();
            e_GUI.setVisible(true);
            while (true) {
                Socket Sock = sSocket.accept();
                Thread newClient = new Thread(new Eve(Sock));
                newClient.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
