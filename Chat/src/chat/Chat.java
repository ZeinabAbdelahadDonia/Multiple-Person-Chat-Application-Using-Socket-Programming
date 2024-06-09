/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package chat;

import java.net.*;
import java.io.*;
import java.util.*;
import chat.Member;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Chat implements Runnable {

    static ChatGUI cor_GUI;
    public static ArrayList<Member> members = new ArrayList<Member>();
    public static int corNextNode;
    Socket clientSocket;

    public Chat(Socket client) {
        this.clientSocket = client;
    }

    @Override
    public void run() {

        try {

            ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());

            Message msg;
            while (!(msg = (Message) input.readObject()).getSender().equals("leave")) {//sender equals leave if want to leave
                if (msg.isPriv() == false) {// msg is public
                    if (msg.getMsg().equals("join")) {
                        Member m = (Member) input.readObject();
                        System.out.println(m);
                        if (members.isEmpty()) {
                            members.add(m);
                            cor_GUI.appendJoinLogScreen(m.getName() + " joined the chat");
                            cor_GUI.updateMemberNum(members.size());

                        } else {
                            for (Member i : members) {
                                if (i.getPortNum() == m.getPortNum()) {//already exists
                                    continue;
                                }
                            }
                            members.get(members.size() - 1).setRightNode(m.getPortNum());//attach joined node to end of list
                            m.setRightNode(members.get(0).getPortNum());//sho??
                            members.add(m);
                            cor_GUI.updateMemberNum(members.size());

                            for (Member i : members) {//to update next nodes of clients
                                try {
                                    Socket sendNextNodes = new Socket("localhost", i.getPortNum());
                                    ObjectOutputStream oos = new ObjectOutputStream(sendNextNodes.getOutputStream());
                                    oos.writeObject(i);
                                    oos.close();
                                    sendNextNodes.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            for (Member i : members) {// send to all that someone joined

                                try {
                                    Socket temp = new Socket("localhost", i.getPortNum());

                                    ObjectOutputStream o = new ObjectOutputStream(temp.getOutputStream());
                                    Message mes = new Message("all", "Coordinator", m.getName() + " joined the chat", false);
                                    o.writeObject(mes);
                                    o.close();
                                    temp.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            cor_GUI.appendJoinLogScreen(m.getName() + " joined the chat");

                        }

                    } else {// normal public message
                        System.out.println(msg.getMsg());
                        Member m = (Member) input.readObject();
                        for (Member i : members) {
                            if (m.getName().equals(i.getName())) {
                                for (Member j : members) {
                                    if (m.getPortNum() == j.getPortNum()) {
                                        continue;
                                    } else {
                                        try {
                                            Socket tempSocket = new Socket("localhost", j.getPortNum());
                                            ObjectOutputStream o = new ObjectOutputStream(tempSocket.getOutputStream());
                                            Message mes = new Message("all", m.getName(), m.getName() + ": " + msg.getMsg(), false);
                                            o.writeObject(mes);
                                            o.close();
                                            tempSocket.close();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                        cor_GUI.appendLogScreen(m.getName() + ": " + msg.getMsg());
                    }

                } //else { // msg is priv
//                    System.out.println("Coordinator recieved a private message");
//                    if (msg.getReceiver().equals("Coordinator")) {
//                        msg.decryptMsg();
//                        cor_GUI.appendLogScreen("(private) " + msg.getSender() + ": " + msg.getMsg());
//
//                    } else {
//                        try {
//                            System.out.println("Test encryption: " + msg.getMsg());//test encryption
//                            corNextNode = members.get(0).getRightNode();
//                            Socket toNext = new Socket("localhost", corNextNode);
//                            System.out.println("Message sent to " + corNextNode);
//                            ObjectOutputStream oos2 = new ObjectOutputStream(toNext.getOutputStream());
//                            oos2.writeObject(msg);
//                            oos2.close();
//                            toNext.close();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
              //  }

            }
            Object s = input.readObject();
            for (Member m : members) {
                if (m.getName().equals(s.toString())) {
                    if (members.size() > 2) {//at least 3
                        int index = members.indexOf(m);
                        if (index == 0) {
                            Member before = members.get(members.size() - 1);
                            before.setRightNode(m.getRightNode());

                        } else {
                            Member before = members.get(index - 1);
                            before.setRightNode(m.getRightNode());

                        }
                        members.remove(m);
                         for (Member i : members) {
                            System.out.println(i);
                        }
                        cor_GUI.updateMemberNum(members.size());
                        for (Member i : members) {
                            try {
                                Socket sendNextNodes = new Socket("localhost", i.getPortNum());
                                ObjectOutputStream oos = new ObjectOutputStream(sendNextNodes.getOutputStream());
                                oos.writeObject(i);
                                oos.close();
                                sendNextNodes.close();
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }

                    } else {
                        members.remove(m);
                        cor_GUI.updateMemberNum(members.size());
                        for (Member i : members) {
                            try {
                                Socket sendNextNodes = new Socket("localhost", i.getPortNum());
                                ObjectOutputStream oos = new ObjectOutputStream(sendNextNodes.getOutputStream());
                                oos.writeObject(i);
                                oos.close();
                                sendNextNodes.close();
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    }
                    for (Member i : members) {
                        try {
                            Socket temSocket = new Socket("localhost", i.getPortNum());
                            ObjectOutputStream outMsg = new ObjectOutputStream(temSocket.getOutputStream());
                            Message mesg = new Message("all", "Coordinator", m.getName() + " left the chat", false);
                            outMsg.writeObject(mesg);
                            outMsg.close();
                            temSocket.close();
                        } catch (Exception e) {
                            System.out.println(e);
                        }

                    }
                    cor_GUI.appendLeaveLogScreen(m.getName() + " left the chat");
                    output.writeObject("leave");//to get out of loop in clients

                }
            }

            input.close();
            output.close();
            clientSocket.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void main(String[] args) {
        try {
            ServerSocket coordinator = new ServerSocket(1111);
            //Member cor = new Member(1111, "Coordinator");
            //members.add(cor);
            cor_GUI = new ChatGUI();
            cor_GUI.setVisible(true);
            while (true) {
                Socket client = coordinator.accept();
                Thread t = new Thread(new Chat(client));
                t.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
