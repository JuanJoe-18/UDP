package org.example;

import java.util.Scanner;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UDPConnection connection = UDPConnection.getInstance();
        boolean configured = false;

        System.out.println("═══════════════════════════════════════════════");
        System.out.println("    COMUNICACIÓN UDP - Universidad Icesi");
        System.out.println("═══════════════════════════════════════════════\n");

        while (true) {
            System.out.println("\n┌───────────── MENÚ ─────────────┐");
            System.out.println("│ 1. Configurar puerto local     │");
            System.out.println("│ 2. Enviar mensaje              │");
            System.out.println("│ 3. Salir                       │");
            System.out.println("└────────────────────────────────┘");
            System.out.print(">>> Seleccione una opción: ");

            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    configurarPuerto(scanner, connection);
                    configured = true;
                    break;

                case "2":
                    if (!configured) {
                        System.out.println("✗ Primero debe configurar el puerto local (Opción 1)");
                        break;
                    }
                    enviarMensaje(scanner, connection);
                    break;

                case "3":
                    System.out.println("\n✓ Cerrando aplicación...");
                    connection.closeConnection();
                    if (connection.isAlive()) {
                        try {
                            connection.join(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    scanner.close();
                    System.out.println("✓ ¡Hasta pronto!");
                    return;

                default:
                    System.out.println("✗ Opción inválida. Intente de nuevo.");
            }
        }
    }

    private static void configurarPuerto(Scanner scanner, UDPConnection connection) {
        try {
            System.out.print("\n→ Ingrese el puerto local (ej: 5000): ");
            int port = Integer.parseInt(scanner.nextLine().trim());

            if (port < 1024 || port > 65535) {
                System.out.println("✗ Puerto inválido. Use un valor entre 1024 y 65535");
                return;
            }

            connection.setPort(port);
            
            if (!connection.isAlive()) {
                connection.start();
            }

            System.out.println("✓ Configuración completada");
            System.out.println("✓ Esperando mensajes entrantes...");

        } catch (NumberFormatException e) {
            System.out.println("✗ Error: Ingrese un número válido");
        } catch (IOException e) {
            System.out.println("✗ Error al configurar puerto: " + e.getMessage());
            System.out.println("  Tip: El puerto podría estar en uso. Intente con otro.");
        }
    }

    private static void enviarMensaje(Scanner scanner, UDPConnection connection) {
        try {
            System.out.println("\n─────── ENVIAR MENSAJE ───────");
            
            System.out.print("→ IP destino (ej: 127.0.0.1 para local): ");
            String destIP = scanner.nextLine().trim();

            System.out.print("→ Puerto destino (ej: 5001): ");
            int destPort = Integer.parseInt(scanner.nextLine().trim());

            if (destPort < 1024 || destPort > 65535) {
                System.out.println("✗ Puerto inválido");
                return;
            }

            System.out.print("→ Mensaje: ");
            String message = scanner.nextLine().trim();

            if (message.isEmpty()) {
                System.out.println("✗ El mensaje no puede estar vacío");
                return;
            }

            connection.sendDatagram(message, destIP, destPort);

        } catch (NumberFormatException e) {
            System.out.println("✗ Error: Puerto inválido");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
}
