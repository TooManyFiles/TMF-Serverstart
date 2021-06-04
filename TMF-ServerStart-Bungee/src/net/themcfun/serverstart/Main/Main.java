package net.themcfun.serverstart.Main;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Main extends Plugin{

	String path;
	String defaultpath;
	public static String servername;
	
	@Override
	public void onEnable() {
		
		registerCommands();
		
		System.out.println("[TMF-Serverstart] sucessfully loaded!");
		
		try {
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		File file = new File(getDataFolder().getPath(), "config.yml");
		if(!file.exists()) {
			file.createNewFile();
		} 
		
		Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		config.set("defaultpath", "unset");
		ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
		
		defaultpath = config.getString("defaultpath");
		path = (defaultpath + "/" + servername);
		
		} catch(IOException e) {
			
		}
	}	

	@Override
	public void onDisable() {
		System.out.println("[TMF-Serverstart] sucessfully disabled!");
		
	}	
	private void registerCommands() {
	   
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new ServerstartCMD("msg"));
		
	}
	public void startserver() {
		try {
			Process p = Runtime.getRuntime().exec(path + "/start.sh");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
