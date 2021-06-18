/**
 * 
 */
package net.themcfun.serverstart.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.md_5.bungee.api.CommandSender;

/**
 * @author Lukas
 *
 */
public class SendConsolLog implements Runnable{
	
	String servername;
	CommandSender sender;
	Process p;
	public SendConsolLog(String servername, CommandSender sender, Process p) {
		this.servername = servername;
		this.sender = sender;
		this.p = p;

	}
	public void setSender(CommandSender sender) {
		this.sender = sender;
	}
	/**
	 * 
	 */
	public void run() {
		System.out.println("Printer started");
		InputStream stdIn = p.getInputStream();
		
		




		InputStreamReader isr = new InputStreamReader(stdIn);
		BufferedReader br = new BufferedReader(isr);

		String line = null;
		 
		try {
			while (((line = br.readLine()) != null))
			{
				try {
				sender.sendMessage(String.format("§c[%s-ConsolLog]:§r  %s", servername, line).toString());
				} catch (NullPointerException e) {}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		sender.sendMessage("[" + servername + "-ConsoleFeed end]");
		Main.consoles.remove(servername);
		Main.consolprinter.remove(servername);
		Main.seeconsol.remove(servername);
		Main.consolinput.remove(servername);
		if(line == null) {

			int exitVal = -1;
			try {
				exitVal = p.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sender.sendMessage(String.format("§c[%0$s-ConsolLog]:§4 shutdown with exitCode: %1$d", servername, exitVal));
		}

	}

}
