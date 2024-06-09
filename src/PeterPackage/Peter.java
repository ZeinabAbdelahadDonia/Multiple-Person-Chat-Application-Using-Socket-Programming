/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PeterPackage;

import chat.Chat;
import chat.Member;
import chat.Message;
import java.net.*;
import java.io.*;
import java.util.*;

//Scanner sc = new Scanner(System.in);
public class Peter implements Runnable {

    Socket clientSocket;
    static PeterGUI s_GUI;
    static int peterNextNode;

    public Peter(Socket clientSocket) {
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
                    peterNextNode = m.getRightNode();
                } else if (msg.getClass().getSimpleName().equals("Message")) {

                    Message m = (Message) msg;
                    if (m.isPriv() == true) {
                        System.out.println("Peter received private message");
                        if (m.getReceiver().equals("Peter")) {
                            System.out.println("Message belonged to peter");
                            m.decryptMsg();
                            System.out.println("Message decrypted");
                            s_GUI.appendPeterScreen("(private) " + m.getSender() + ": " + m.getMsg());

                        } else {
                            try {
                                System.out.println(m.getMsg()); //test encryption      
                                Socket toNext = new Socket("localhost", peterNextNode);
                                ObjectOutputStream oos2 = new ObjectOutputStream(toNext.getOutputStream());
                                oos2.writeObject(m);
                                System.out.println("Message sent to : " + peterNextNode);
                                oos2.close();
                                toNext.close();
                            } catch (Exception e) {
                                System.out.println("Exception handling in peter sending private messages");
                            }
                        }
                    } else {
                        s_GUI.appendPeterScreen(m.getMsg());
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
            ServerSocket sSocket = new ServerSocket(2222);
            s_GUI = new PeterGUI();
            s_GUI.setVisible(true);
            while (true) {
                Socket Sock = sSocket.accept();
                Thread newClient = new Thread(new Peter(Sock));
                newClient.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
