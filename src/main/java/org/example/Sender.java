package org.example;

import java.io.IOException;
import java.util.Scanner;

public class Sender {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UDPConnection connection = UDPConnection.getInstance();

        System.out.println("═══════════════════════════════════════════════");
        System.out.println("        UDP SENDER - Universidad Icesi");
        System.out.println("═══════════════════════════════════════════════\n");

        try {
            System.out.print("→ Ingrese el puerto local (ej: 5001): ");
            int localPort = Integer.parseInt(scanner.nextLine().trim());

            if (localPort < 1024 || localPort > 65535) {
                System.out.println("✗ Puerto inválido. Use un valor entre 1024 y 65535");
                scanner.close();
                return;
            }

            connection.setPort(localPort);
            connection.start();

            System.out.println("\n✓ Sender configurado en puerto " + localPort);
            System.out.println("✓ Listo para enviar mensajes\n");

            while (true) {
                System.out.println("┌────────────────────────────────┐");
                System.out.println("│ 1. Enviar mensaje              │");
                System.out.println("│ 2. Salir                       │");
                System.out.println("└────────────────────────────────┘");
                System.out.print(">>> Seleccione opción: ");

                String option = scanner.nextLine().trim();

                switch (option) {
                    case "1":
                        enviarMensaje(scanner, connection);
                        break;

                    case "2":
                        System.out.println("\n✓ Cerrando Sender...");
                        connection.closeConnection();
                        
                        if (connection.isAlive()) {
                            try {
                                connection.join(1000);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                        
                        System.out.println("✓ ¡Hasta pronto!");
                        scanner.close();
                        return;

                    default:
                        System.out.println("✗ Opción inválida\n");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("✗ Error: Ingrese un número válido");
        } catch (IOException e) {
            System.out.println("✗ Error al configurar puerto: " + e.getMessage());
            System.out.println("  Tip: El puerto podría estar en uso. Intente con otro.");
        } finally {
            scanner.close();
        }
    }

    private static void enviarMensaje(Scanner scanner, UDPConnection connection) {
        try {
            System.out.println("\n─────── ENVIAR MENSAJE ───────");
            
            System.out.print("→ IP destino (ej: 127.0.0.1): ");
            String destIP = scanner.nextLine().trim();

            System.out.print("→ Puerto destino (ej: 5000): ");
            int destPort = Integer.parseInt(scanner.nextLine().trim());

            if (destPort < 1024 || destPort > 65535) {
                System.out.println("✗ Puerto inválido\n");
                return;
            }

            System.out.print("→ Mensaje: ");
            String message = scanner.nextLine().trim();

            if (message.isEmpty()) {
                System.out.println("✗ El mensaje no puede estar vacío\n");
                return;
            }

            connection.sendDatagram(message, destIP, destPort);
            System.out.println();

        } catch (NumberFormatException e) {
            System.out.println("✗ Error: Puerto inválido\n");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage() + "\n");
        }
    }
}
