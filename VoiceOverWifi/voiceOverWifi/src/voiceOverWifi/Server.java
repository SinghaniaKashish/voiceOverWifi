package voiceOverWifi;

import javax.sound.sampled.*;
import java.net.*;
import java.io.*;

public class Server
{
	//initialize socket and input stream
	private Socket		 socket = null;
	private ServerSocket server = null;
	private DataInputStream in	 = null;

	// constructor with port
	public Server(int port)
	{
		// starts server and waits for a connection
		try
		{
			server = new ServerSocket(port);
			System.out.println("Server started");

			System.out.println("Waiting for a client ...");

			socket = server.accept();
			System.out.println("Client accepted");

			DataInputStream dis = new DataInputStream(socket.getInputStream());
            AudioFormat format = getAudioFormat();
            SourceDataLine speaker = AudioSystem.getSourceDataLine(format);
            speaker.open(format);
            speaker.start();
            byte[] audioData = new byte[10000]; // Adjust the buffer size as needed
            while (true) 
            {
            	int bytesRead = dis.read(audioData);
                if (bytesRead == -1) break;
                ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
                AudioInputStream audioInputStream = new AudioInputStream(bais, format, audioData.length / format.getFrameSize());
                byte[] tempBuffer = new byte[10000]; // Adjust buffer size
                while ((bytesRead = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) 
                {
                	if (bytesRead > 0) 
                	{
                		speaker.write(tempBuffer, 0, bytesRead);
                    }
                }
                audioInputStream.close();
            }
            speaker.drain();
            speaker.close();
            socket.close();
            System.out.println("Closing connection");
    		in.close();
		}
		catch(Exception i)
		{
			System.out.println(i);
		}
	}
	static public AudioFormat getAudioFormat() 
	{
		float sampleRate = 8000.0F; // Sample rate
		int sampleSizeInBits = 16; // Bits per sample
		int channels = 1; // Mono
		boolean signed = true; // Signed data
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	public static void main(String args[])
	{
		Server server = new Server(5000);
	}
}