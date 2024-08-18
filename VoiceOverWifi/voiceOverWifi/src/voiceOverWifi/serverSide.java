package voiceOverWifi;


import javax.sound.sampled.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
public class serverSide {
public static void main(String[] args) {
try {
int serverPort = 12345;
System.out.println("Server is starting...");
ServerSocket serverSocket = new ServerSocket(serverPort);
System.out.println("Server is listening on port " + serverPort);
System.out.println("Waiting for a client to connect...\n");
Socket socket = serverSocket.accept();
System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());
AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, true);
DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
sourceDataLine.open(audioFormat);
sourceDataLine.start();
System.out.println("Speakers opened \nReceiving and playing audio...");
byte[] buffer = new byte[1024];
InputStream inputStream = socket.getInputStream();
while (true) {
int bytesRead = inputStream.read(buffer, 0, buffer.length);
sourceDataLine.write(buffer, 0, bytesRead);
}
} catch (Exception e) {
e.printStackTrace();
}
}
}