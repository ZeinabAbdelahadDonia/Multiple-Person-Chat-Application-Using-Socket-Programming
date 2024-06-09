/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MarkPackage;

import chat.Chat;
import chat.Member;
import chat.Message;
import java.net.*;
import java.io.*;
import java.util.*;

//Scanner sc = new Scanner(System.in);
public class Mark implements Runnable {

    Socket clientSocket;
    static MarkGUI s_GUI;
    static int markNextNode;

    public Mark(Socket clientSocket) {
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
                    markNextNode = m.getRightNode();
                } else if (msg.getClass().getSimpleName().equals("Message")) {

                    Message m = (Message) msg;
                    if (m.isPriv() == true) {
                        System.out.println("Mark received private message");
                        if (m.getReceiver().equals("Mark")) {
                            System.out.println("Message belongs to Mark");
                            m.decryptMsg();
                            System.out.println("Message decrypted");
                            s_GUI.appendMarkScreen("(private) " + m.getSender() + ": " + m.getMsg());

                        } else {
                            try {
                                System.out.println(m.getMsg()); //test encryption      
                                Socket toNext = new Socket("localhost", markNextNode);
                                ObjectOutputStream oos2 = new ObjectOutputStream(toNext.getOutputStream());
                                oos2.writeObject(m);
                                System.out.println("Message sent to " + markNextNode);
                                oos2.close();
                                toNext.close();
                            } catch (Exception e) {
                                System.out.println("Exception handling in mark sending private message");
                            }
                        }
                    } else {
                        s_GUI.appendMarkScreen(m.getMsg());
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
            ServerSocket sSocket = new ServerSocket(3333);
            s_GUI = new MarkGUI();
            s_GUI.setVisible(true);
            while (true) {
                Socket Sock = sSocket.accept();
                Thread newClient = new Thread(new Mark(Sock));
                newClient.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
