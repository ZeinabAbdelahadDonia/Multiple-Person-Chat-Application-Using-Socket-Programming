/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat;

import java.io.Serializable;
import java.net.*;

public class Member implements Serializable{
    private int portNum;
    private String name; 
    private int rightNode; 

    public Member(int portNum, String name, int rightNode) {
        this.portNum = portNum;
        this.name = name;
        this.rightNode = rightNode;
    }

      public Member(int portNum, String name) {
        this.portNum = portNum;
        this.name = name;
    }

    public Member(String name) {
        this.name = name;
    }

    public Member() {
    }

   
   
    

    public int getPortNum() {
        return portNum;
    }

    public void setPortNum(int portNum) {
        this.portNum = portNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRightNode() {
        return rightNode;
    }

    public void setRightNode(int rightNode) {
        this.rightNode = rightNode;
    }

    @Override
    public String toString() {
        return "Member{" + "portNum=" + portNum + ", name=" + name + ", rightNode=" + rightNode + '}';
    }


  
    

    
    

   
   
    
}
