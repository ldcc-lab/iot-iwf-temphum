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
		IWF device = new IWF("00000.0001.ldcc");
		Runtime rt = Runtime.getRuntime();
		rt.exec("gpio mode 0 out");
		rt.exec("gpio mode 7 in");
		device.register();
		/* �젣�뼱 紐낅졊
		AnyArgType command = new AnyArgType();
		command.setName("switch");
		command.setValue("initial_value");
		try
		{
			device.putControl("control", M2MCmdType.DOWNLOAD, M2MExecModeType.IMMEDIATEONCE, new AnyArgType[] { command });
		}
		catch (M2MException e)
		{
			e.printStackTrace();
		}
		device.addCmdListener(new CmdListener()
		{
			String power;

			public void excute(Map<String, String> cmd, NotifyResponse response)
			{
				try
				{
					Runtime rt = Runtime.getRuntime();
					this.power = ((String)cmd.get("switch"));
					if ("OFF".equals(this.power)) {
						rt.exec("gpio write 0 0");
					} else {
						rt.exec("gpio write 0 1");
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});*/
		
		while(true){
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
			Thread.sleep(30000);
		}
	}

}
