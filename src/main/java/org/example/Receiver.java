package org.example;

import java.io.IOException;
import java.util.Scanner;

public class Receiver {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UDPConnection connection = UDPConnection.getInstance();

        System.out.println("═══════════════════════════════════════════════");
        System.out.println("        UDP RECEIVER - Universidad Icesi");
        System.out.println("═══════════════════════════════════════════════\n");

        try {
            System.out.print("→ Ingrese el puerto local para recibir (ej: 5000): ");
            int port = Integer.parseInt(scanner.nextLine().trim());

            if (port < 1024 || port > 65535) {
                System.out.println("✗ Puerto inválido. Use un valor entre 1024 y 65535");
                scanner.close();
                return;
            }

            connection.setPort(port);
            connection.start();

            System.out.println("\n✓ Receiver iniciado en puerto " + port);
            System.out.println("✓ Esperando mensajes...");
            System.out.println("\n>>> Presione ENTER para salir <<<\n");

            scanner.nextLine();

            System.out.println("\n✓ Cerrando Receiver...");
            connection.closeConnection();
            
            if (connection.isAlive()) {
                try {
                    connection.join(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            System.out.println("✓ ¡Hasta pronto!");

        } catch (NumberFormatException e) {
            System.out.println("✗ Error: Ingrese un número válido");
        } catch (IOException e) {
            System.out.println("✗ Error al configurar puerto: " + e.getMessage());
            System.out.println("  Tip: El puerto podría estar en uso. Intente con otro.");
        } finally {
            scanner.close();
        }
    }
}
