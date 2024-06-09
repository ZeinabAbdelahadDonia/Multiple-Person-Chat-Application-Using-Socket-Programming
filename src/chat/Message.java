/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat;

import java.io.Serializable;

/**
 *
 * @author USER
 */
public class Message implements Serializable{
    private String receiver;
    private String sender;
    private String msg;
    private boolean priv;

    public Message(String receiver, String sender, String msg, boolean priv) {
        this.receiver = receiver;
        this.sender = sender;
        this.msg = msg;
        this.priv = priv;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isPriv() {
        return priv;
    }

    public void setPriv(boolean priv) {
        this.priv = priv;
    }
    
     public void encryptMsg() {
        String encrypted = "";
        for (int i = this.msg.toString().length() - 1; i >= 0; i--) {
            encrypted = encrypted + this.msg.toString().charAt(i);
        }
        encrypted = encrypted.replaceAll("a", "%");
        encrypted = encrypted.replaceAll("i", "~");
        encrypted = encrypted.replaceAll("o", "]");
        encrypted = encrypted.replaceAll("u", "<");
        encrypted = encrypted.replaceAll("e", "#");

        this.msg = encrypted;
    }

    public void decryptMsg() {
        String decrypted = "";
        for (int i = this.msg.length() - 1; i >= 0; i--) {
            decrypted = decrypted + this.msg.charAt(i);
        }
        decrypted = decrypted.replaceAll("%", "a");

        decrypted = decrypted.replaceAll("~", "i");
        decrypted = decrypted.replaceAll("]", "o");
        decrypted = decrypted.replaceAll("<", "u");
        decrypted = decrypted.replaceAll("#", "e");
        this.msg = decrypted;
    }
}
