package voiceOverWifi;


import javax.sound.sampled.*;
import java.io.*;
import java.net.*;

public class Client {
	// initialize socket and input output streams
	private Socket socket = null;
	private DataInputStream input = null;
	private DataOutputStream out = null;

	// constructor to put ip address and port
	public Client(String address, int port)
	{
		// establish a connection
		try 
		{
			AudioFormat format = getAudioFormat();
            DataLine.Info microphoneInfo = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(microphoneInfo);
            microphone.open(format);
            microphone.start();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] audioData = new byte[10000]; // Adjust the buffer size as needed
            
            System.out.println("Connecting to server");
            socket = new Socket(address, port);
			System.out.println("Connected");
			
			while (true) {
                int bytesRead = microphone.read(audioData, 0, audioData.length);
                baos.write(audioData, 0, bytesRead);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.write(baos.toByteArray());
                dos.flush();
                baos.reset();
            }

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	 private static AudioFormat getAudioFormat() 
	 {
	        float sampleRate = 8000.0F; // Sample rate
	        int sampleSizeInBits = 16; // Bits per sample
	        int channels = 1; // Mono
	        boolean signed = true; // Signed data
	        boolean bigEndian = false; // Little
	        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	 }

	public static void main(String args[])
	{
		Client client = new Client("192.168.1.25", 5000);
	}
}