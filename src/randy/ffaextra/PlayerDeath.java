package randy.ffaextra;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import randy.core.CoreAPI;
import randy.core.CoreScoreboard;
import randy.core.tools.CoreDatabase;

public class PlayerDeath implements Listener{
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Player player = event.getEntity();
		Player killer = event.getEntity().getKiller();
		
		if(main.towerPlayers.contains(player)){
			ArrayList<Integer> scoreList = main.towerScores.get(player.getName());
			scoreList.set(1, scoreList.get(1) + 1);
			CoreDatabase.ModifyDeaths(player.getName(), "tower", 1);
			CoreScoreboard.UpdateScoreboard(player.getName());
			
			if(main.towerPlayers.contains(killer)){
				ArrayList<Integer> killerScoreList = main.towerScores.get(killer.getName());
				killerScoreList.set(0, killerScoreList.get(0) + 1);
				killerScoreList.set(2, killerScoreList.get(2) + 1);
				CoreDatabase.ModifyKills(killer.getName(), "tower", 1);
				CoreScoreboard.UpdateScoreboard(killer.getName());
				
				CoreAPI.BroadcastKillstreak(killer, killerScoreList.get(2), "tower");
			}
			
			scoreList.set(2, 0);
			return;
		}	
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player player = event.getPlayer();		
		if(main.towerPlayers.contains(player)){
			main.SetPlayerTower(player);
		}
	}
}
