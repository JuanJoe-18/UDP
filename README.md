# 📡 Comunicación UDP - Taller Universidad Icesi

Proyecto Java Maven para comunicación UDP entre dos nodos en una red local.

## 🏗️ Arquitectura

- **UDPConnection**: Clase Singleton que extiende Thread
  - Patrón Singleton para instancia única
  - Hilo principal para recepción continua de mensajes
  - Hilos separados para envío asíncrono de mensajes
  - Socket UDP para comunicación bidireccional

## 📋 Requisitos

- Java 22 o superior
- Maven 3.x
- Wireshark (para captura de paquetes)

## 🚀 Compilación

```bash
mvn clean package
```

Esto genera dos JARs en `target/`:
- `UDP-1.0-SNAPSHOT.jar` - JAR simple
- `UDP-1.0-SNAPSHOT-jar-with-dependencies.jar` - JAR ejecutable con todas las dependencias

## ▶️ Ejecución

El proyecto tiene 3 clases ejecutables:
- **Main**: Interfaz completa (enviar y recibir)
- **Receiver**: Solo recibe mensajes
- **Sender**: Solo envía mensajes

### Opción 1: Usando Main (Completo)

**Terminal 1:**
```bash
java -cp target\UDP-1.0-SNAPSHOT-jar-with-dependencies.jar org.example.Main
```

**Terminal 2:**
```bash
java -cp target\UDP-1.0-SNAPSHOT-jar-with-dependencies.jar org.example.Main
```

### Opción 2: Usando Receiver y Sender (Separados)

**Terminal 1 (Receiver en puerto 5000):**
```bash
java -cp target\UDP-1.0-SNAPSHOT-jar-with-dependencies.jar org.example.Receiver
```
- Puerto local: `5000`
- Esperará mensajes entrantes

**Terminal 2 (Sender en puerto 5001):**
```bash
java -cp target\UDP-1.0-SNAPSHOT-jar-with-dependencies.jar org.example.Sender
```
- Puerto local: `5001`
- Opción 1: Enviar mensaje
  - IP destino: `127.0.0.1`
  - Puerto destino: `5000`
  - Mensaje: `Hola desde Sender!`

### Comunicación Bidireccional

**Opción A: Dos instancias de Main**
- Ambos pueden enviar y recibir

**Opción B: Receiver + Sender + Receiver + Sender**
- Terminal 1: Receiver puerto 5000
- Terminal 2: Sender puerto 5001 → envía a 5000
- Terminal 3: Receiver puerto 5001
- Terminal 4: Sender puerto 5002 → envía a 5001

**Opción C: Dos Receivers y envío cruzado**
- Ambos tienen Receiver activo para recibir
- Ambos pueden enviar (Receiver también recibe pero Main puede enviar)

## 📊 Captura con Wireshark

### Configuración

1. Abrir Wireshark
2. Seleccionar interfaz **Loopback** (para pruebas locales)
3. Aplicar filtro de captura:

```
udp.port == 5000 || udp.port == 5001
```

O más específico:
```
udp.port == 5000
```

### Análisis

En Wireshark podrás ver:
- **Source Port**: Puerto origen del mensaje
- **Destination Port**: Puerto destino
- **Source IP**: IP origen (127.0.0.1 para local)
- **Destination IP**: IP destino
- **Data**: Contenido del mensaje en bytes
- **Length**: Tamaño del paquete UDP

## 🌐 Pruebas en Red Local

Para probar entre dos equipos diferentes:

**Equipo A (Receiver):**
```bash
java -cp UDP-1.0-SNAPSHOT-jar-with-dependencies.jar org.example.Receiver
```
- Puerto local: `5000`
- IP del equipo A: Ejemplo `192.168.1.10`

**Equipo B (Sender):**
```bash
java -cp UDP-1.0-SNAPSHOT-jar-with-dependencies.jar org.example.Sender
```
- Puerto local: `5001`
- Enviar mensaje:
  - IP destino: `192.168.1.10` (IP del Equipo A)
  - Puerto destino: `5000`
  - Mensaje: `Hola desde Equipo B!`

**Filtro Wireshark para red local:**
```
udp.port == 5000 && (ip.addr == 192.168.1.10 || ip.addr == 192.168.1.20)
```

## 🔧 Características Técnicas

### Clases Principales

**1. UDPConnection (Singleton + Thread)**
- **Singleton Pattern**: `getInstance()` garantiza única instancia
- **Thread de Recepción**: Método `run()` ejecuta bucle continuo para recibir mensajes
- **Envío Asíncrono**: Método `sendDatagram()` crea un Thread separado para cada envío
- **Buffer**: 1024 bytes para mensajes
- **Gestión de Socket**: Cierre automático al reconfigurar puerto

**2. Receiver (Solo Recepción)**
- Programa dedicado a recibir mensajes
- Inicia el hilo de recepción automáticamente
- Ideal para nodo pasivo/servidor

**3. Sender (Solo Envío)**
- Programa dedicado a enviar mensajes
- Menú interactivo para envíos múltiples
- Ideal para nodo activo/cliente

**4. Main (Completo)**
- Interfaz completa con ambas funcionalidades
- Permite enviar y recibir desde el mismo programa

### Seguridad de Hilos

- ✅ Todas las operaciones de red se ejecutan en hilos separados
- ✅ El hilo principal maneja la interfaz de usuario
- ✅ Sincronización del patrón Singleton
- ✅ Cierre seguro de recursos

## 🐛 Solución de Problemas

### Puerto en uso
```
Error al configurar puerto: Address already in use
```
**Solución**: El puerto ya está siendo usado. Intenta con otro puerto (ej: 5002, 5003)

### No se reciben mensajes
1. Verificar que ambos programas estén ejecutándose
2. Confirmar que el puerto destino coincida con el puerto configurado en el receptor
3. Para red local: Verificar firewall y que estén en la misma red

### Wireshark no captura paquetes
1. Para pruebas locales (127.0.0.1): Seleccionar interfaz **Loopback/Npcap Loopback Adapter**
2. Para red local: Seleccionar interfaz de red correcta (WiFi o Ethernet)
3. Verificar que el filtro UDP esté correctamente escrito

## 📝 Notas

- UDP es un protocolo **no confiable**: Los mensajes pueden perderse o llegar en desorden
- No hay confirmación de recepción (ACK)
- Ideal para comunicación en tiempo real donde la velocidad es más importante que la confiabilidad
- Tamaño máximo recomendado del mensaje: ~1472 bytes (para evitar fragmentación IP)

## 👥 Autores

Taller de Redes - Universidad Icesi
