/**
 * 
 */
package net.themcfun.serverstart.Main;


/**
 * @author Lukas
 *
 */
public class MessageOnServerexit implements Runnable{
	private String servername;
	private Process p;
	public MessageOnServerexit(String servername, Process p ) {
		this.servername = servername;
		this.p = p;
	}
	/**
	 * 
	 */
	public void run() {
		int exitVal;
		try {
			exitVal = p.waitFor();
			Main.INSTANCE.getLogger().warning("Server "+servername+" was shutdown with exitCode: "+exitVal);
		} catch (InterruptedException e) {
			//TODO 
			e.printStackTrace();
		}
														
	}
	

}