package br.com.aeroboteco.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class ClientUDP {
    public static void main(String[] args) {
        try {
            /*DatagramSocket clientSocket = new DatagramSocket();
            InetAddress addr=InetAddress.getLocalHost();
            String toSend="660+";
            byte[] buffer=toSend.getBytes();
            DatagramPacket pergunta=new DatagramPacket(buffer,buffer.length,addr,49000);
            clientSocket.send(pergunta);
            System.out.println("resposta");
            DatagramPacket resposta=new DatagramPacket(new byte[512],512);
            clientSocket.receive(resposta);
            System.out.println("Question:"+resposta.getData());*/


  String sumString = "";
  System.out.println("Abrindo o socket e criando o stream.");
  String host = "127.0.0.1";
  Socket socket = new Socket (host, 49000);

  DataOutputStream ostream = new DataOutputStream(socket.getOutputStream());
  DataInputStream istream = new DataInputStream(socket.getInputStream());

  System.out.println("inicializando sum 0.");
  ostream.writeUTF("set_sum");
  ostream.flush();
  sumString = istream.readUTF();

  System.out.println("Incrementando.");
  int count = new Integer(args[1]).intValue();
  long startTime = System.currentTimeMillis();

  for (int i = 0; i < count; i++)
  {
  ostream.writeUTF("incrementando");
  ostream.flush();
  sumString = istream.readUTF();
  }

  long stopTime = System.currentTimeMillis();
  System.out.println ("AVG ping = " + ((stopTime - startTime) /
(float)count) + "msecs") ;
  System.out.println("Contador = " + sumString);

  /*          String sumString = "";

  System.out.println("Abrindo o socket e criando o stream.");
  String host = "127.0.0.1";
  Socket socket = new Socket (host, 9000);

  DataOutputStream ostream = new DataOutputStream(socket.getOutputStream());
  DataInputStream istream = new DataInputStream(socket.getInputStream());

  System.out.println("inicializando sum 0.");
  ostream.writeUTF("set_sum");
  ostream.flush();
  sumString = istream.readUTF();

  System.out.println("Incrementando.");
  int count = new Integer(args[1]).intValue();
  long startTime = System.currentTimeMillis();

  for (int i = 0; i < count; i++)
  {
  ostream.writeUTF("incrementando");
  ostream.flush();
  sumString = istream.readUTF();
  }

  long stopTime = System.currentTimeMillis();
  System.out.println ("AVG ping = " + ((stopTime - startTime) /
(float)count) + "msecs") ;
  System.out.println("Contador = " + sumString);*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
