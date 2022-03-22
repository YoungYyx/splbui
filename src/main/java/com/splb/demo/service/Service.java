package com.splb.demo.service;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

@org.springframework.stereotype.Service
public class Service {
    private int splbListenPort = 15000;
    private int mptcpListenPort = 16000;
    private Double splbEth0Speed = 0.0;
    private Double splbEth1Speed = 0.0;
    private Double mptcpSpeed = 0.0;
    private Double realEth0Speed = 0.0;
    private Double realEth1Speed = 0.0;
    private Double realEth0Rtt = 0.0;
    private Double realEth1Rtt = 0.0;
    private Thread splbListenThread = null;
    private Thread mptcpListenThread = null;
    private boolean endSign;
    private DatagramSocket splbSocket = null;
    private DatagramSocket mptcpSocket = null;

    //获取实时Eth0带宽
    public String getRealEth0Speed(){
        return realEth0Speed.toString();
    }

    //获取实时Eth1带宽
    public String getRealEth1Speed(){
        return realEth1Speed.toString();
    }
    //获取实时Eth0Rtt
    public String getRealEth0Rtt(){
        return realEth0Rtt.toString();
    }

    //获取实时Eth1带宽
    public String getRealEth1Rtt(){
        return realEth1Rtt.toString();
    }


    public String getSplbEth0Speed(){
        if(!endSign){
            return splbEth0Speed.toString();
        }else{
            return "0.0";
        }
    }
    public String getSplbEth1Speed(){
        if(!endSign){
            return splbEth1Speed.toString();
        }else{
            return "0.0";
        }
    }
    public String getMptcpSpeed(){

        if (!endSign){
            System.out.println(mptcpSpeed);
            return mptcpSpeed.toString();
        }else{
            System.out.println(0);
            return "0.0";
        }
    }
    public void startService(){
        if(splbSocket == null){
            try {
                splbSocket = new DatagramSocket(splbListenPort);
            } catch (SocketException e) {
                splbEth0Speed = 0.0;
                splbEth1Speed = 0.0;
                e.printStackTrace();
            }
        }
        splbListenThread = new Thread(new Runnable() { //启动线程接受数据
            @Override
            public void run() {
                while(true){
                    DatagramPacket inPacket = new DatagramPacket(new byte[512],512);
                    try {
                        splbSocket.receive(inPacket);
                        String speed = new String(inPacket.getData(),0,inPacket.getLength(),StandardCharsets.UTF_8);
                        if(speed.contains("-")){
                            String s[] = speed.split("-");
                            splbEth0Speed = Double.parseDouble(s[0].substring(0,6));
                            splbEth1Speed = Double.parseDouble(s[1].substring(0,6));
                        }else if(speed.contains(":")){
                            String s[] = speed.split(":");
                            realEth0Speed = Double.parseDouble(s[0])/1000;
                            realEth1Speed = Double.parseDouble(s[1])/1000;
                            System.out.println(realEth0Speed+":"+realEth1Speed);
                        }else if(speed.contains(",")){
                            String s[] = speed.split(",");
                            realEth0Rtt = Double.parseDouble(s[0]);
                            realEth1Rtt = Double.parseDouble(s[1]);
                            System.out.println(realEth0Rtt+","+realEth1Rtt);
                        }

                        // System.out.println("Eth0:"+splbEth0Speed+";Eth1:"+splbEth1Speed);
                    } catch (IOException | NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        splbListenThread.start();
        if(mptcpSocket == null){
            try {
                mptcpSocket = new DatagramSocket(mptcpListenPort);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        mptcpListenThread = new Thread(new Runnable() { //启动线程接受数据
            @Override
            public void run() {
                while(true){
                    DatagramPacket inPacket = new DatagramPacket(new byte[512],512);
                    try {
                        mptcpSocket.receive(inPacket);
                        String speed = new String(inPacket.getData(),0,inPacket.getLength(),StandardCharsets.UTF_8);
                        if(speed.contains(":")){
                            String s[] = speed.split(":");
                            realEth0Speed = Double.parseDouble(s[0])/1000;
                            realEth1Speed = Double.parseDouble(s[1])/1000;
                            System.out.println(realEth0Speed+":"+realEth1Speed);
                        }else if(speed.contains(",")){
                            String s[] = speed.split(",");
                            realEth0Rtt = Double.parseDouble(s[0]);
                            realEth1Rtt = Double.parseDouble(s[1]);
                            System.out.println(realEth0Rtt+","+realEth1Rtt);
                        }else{
                            mptcpSpeed = Double.parseDouble(speed.substring(0,6));
                        }

                    } catch (IOException | NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mptcpListenThread.start();
    }
    public void splbStart(){
        endSign = false;
        sendStartMessage(0,"10.37.132.3",15001);  //客户端启动
        sendStartMessage(0,"10.37.132.4",15001);  //服务端启动
    }
    public void mptcpStart(){
        endSign = false;
        sendStartMessage(1,"10.37.132.3",16001);
        sendStartMessage(1,"10.37.132.4",16001);
    }

    private void  sendStartMessage(int type,String IP,int port){
        try {
            InetAddress address = InetAddress.getByName(IP);
            String msg = "start";
            byte[] data = msg.getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet = new DatagramPacket(data,data.length,address,port);
            if(type==0){
                splbSocket.send(packet);
            }else{
                mptcpSocket.send(packet);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void  sendEndMessage(int type,String IP,int port){
        try {
            InetAddress address = InetAddress.getByName(IP);
            String msg = "end";
            byte[] data = msg.getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet = new DatagramPacket(data,data.length,address,port);
            if(type==0){
                splbSocket.send(packet);
            }else{
                mptcpSocket.send(packet);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void splbEnd(){
        endSign = true;
        sendEndMessage(0,"10.37.132.3",15001);  //客户端结束
        sendEndMessage(0,"10.37.132.4",15001);  //服务端结束
        splbEth0Speed = 0.0;
        splbEth1Speed = 0.0;
    }
    public void mptcpEnd(){
        endSign = true;
        sendEndMessage(1,"10.37.132.3",16001);  //客户端结束
        sendEndMessage(1,"10.37.132.4",16001);  //服务端结束
        mptcpSpeed = 0.0;

    }
}
