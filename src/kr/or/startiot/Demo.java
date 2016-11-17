package kr.or.startiot;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import comus.wp.onem2m.iwf.run.IWF;

public class Demo {

	private static String line;
	private static String[] data;
	static int humidity=0;
	static int temperature=0;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		final IWF device = new IWF("55555.5555.RP09");

		device.register();

		while(true){
			Runtime rt= Runtime.getRuntime();
			Process p=rt.exec("python /home/pi/lib/dht.py");
			BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
			if((line = bri.readLine()) != null){
				if(!(line.contains("ERR_CRC") || line.contains("ERR_RNG"))){
					data=line.split("@");
					temperature=Integer.parseInt(data[0]);
					humidity=Integer.parseInt(data[1]);
				}
				else 
					System.out.println("Data Error");
			}

			bri.close();
			p.waitFor();
			device.putContent("sensor", "{\"temperature\":"+temperature+",\"humidity\":"+ humidity+"}");
			Thread.sleep(1000);
		}
	}

}