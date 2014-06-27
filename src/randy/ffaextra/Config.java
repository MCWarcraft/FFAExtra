package randy.ffaextra;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import randy.kits.Kits;

public class Config {

	//File
	static File configfile = new File("plugins" + File.separator + "FFAExtra" + File.separator + "config.yml");
	static FileConfiguration configuration = YamlConfiguration.loadConfiguration(configfile);

	//Load config
	public static void LoadConfig(){

		File directory = new File("plugins" + File.separator + "FFAExtra");
		if(!directory.exists()){
			directory.mkdir();
		}

		//Create file if it does not exist
		if(!configfile.exists()) {
			try {
				configfile.createNewFile();
			} catch (IOException e) {
				System.out.print("The config file could not be created.");
			}
		}
		
		//Tower
		if(configuration.contains("Tower.Location")){
			String[] locSplit = configuration.getString("Tower.Location").split(":");
			main.towerLocation = new Location(Bukkit.getWorld("world"), Integer.parseInt(locSplit[0]), Integer.parseInt(locSplit[1]), Integer.parseInt(locSplit[2]), Float.parseFloat(locSplit[3]), 0);
		}
		if(configuration.contains("Tower.Sign")){
			String[] locSplit = configuration.getString("Tower.Sign").split(":");
			main.towerSign = new Location(Bukkit.getWorld("world"), Integer.parseInt(locSplit[0]), Integer.parseInt(locSplit[1]), Integer.parseInt(locSplit[2]), Float.parseFloat(locSplit[3]), 0);
		}
		if(configuration.contains("Tower.Kit")){
			main.towerKit = Kits.getKitByName(configuration.getString("Tower.Kit"), "ffa");
		}
	}

	public static void SaveConfig(){
		
		//Tower
		if(main.towerLocation != null){
			Location towerLoc = main.towerLocation;
			configuration.set("Tower.Location", towerLoc.getBlockX() + ":" +  towerLoc.getBlockY()+ ":" + towerLoc.getBlockZ()+ ":" + towerLoc.getYaw());
		}
		if(main.towerSign != null){
			Location signLoc = main.towerSign;
			configuration.set("Tower.Sign", signLoc.getBlockX() + ":" +  signLoc.getBlockY()+ ":" + signLoc.getBlockZ()+ ":" + signLoc.getYaw());
		}
		if(main.towerKit != null){
			configuration.set("Tower.Kit", main.towerKit.kitname);
		}
		
		//Save file
		try {
			configuration.save(configfile);
		} catch (IOException e) {
			System.out.print("The config file could not be saved.");
		}
	}
}
