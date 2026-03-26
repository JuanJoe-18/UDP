package org.example;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;

public class UDPConnection extends Thread {
    private static UDPConnection instance;
    private DatagramSocket socket;
    private int port;
    private boolean running;

    private UDPConnection() {
        this.running = false;
    }

    public static synchronized UDPConnection getInstance() {
        if (instance == null) {
            instance = new UDPConnection();
        }
        return instance;
    }

    public void setPort(int port) throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        this.port = port;
        this.socket = new DatagramSocket(port);
        System.out.println("✓ Socket UDP creado en puerto: " + port);
    }

    @Override
    public void run() {
        running = true;
        byte[] buffer = new byte[1024];
        
        System.out.println("✓ Hilo de recepción iniciado en puerto " + port);
        
        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                
                String message = new String(packet.getData(), 0, packet.getLength());
                String senderIP = packet.getAddress().getHostAddress();
                int senderPort = packet.getPort();
                
                System.out.println("\n┌─────────────────────────────────────");
                System.out.println("│ MENSAJE RECIBIDO");
                System.out.println("├─────────────────────────────────────");
                System.out.println("│ De: " + senderIP + ":" + senderPort);
                System.out.println("│ Mensaje: " + message);
                System.out.println("└─────────────────────────────────────\n");
                System.out.print(">>> ");
                
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error al recibir mensaje: " + e.getMessage());
                }
            }
        }
        
        System.out.println("✓ Hilo de recepción detenido");
    }

    public void sendDatagram(String message, String destIP, int destPort) {
        Thread senderThread = new Thread(() -> {
            try {
                byte[] data = message.getBytes();
                InetAddress address = InetAddress.getByName(destIP);
                DatagramPacket packet = new DatagramPacket(data, data.length, address, destPort);
                
                socket.send(packet);
                System.out.println("✓ Mensaje enviado a " + destIP + ":" + destPort);
                
            } catch (IOException e) {
                System.err.println("✗ Error al enviar mensaje: " + e.getMessage());
            }
        });
        
        senderThread.start();
    }

    public void closeConnection() {
        running = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        System.out.println("✓ Conexión UDP cerrada");
    }

    public boolean isRunning() {
        return running;
    }

    public int getPort() {
        return port;
    }
}
