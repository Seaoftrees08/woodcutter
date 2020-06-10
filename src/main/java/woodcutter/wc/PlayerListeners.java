package woodcutter.wc;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PlayerListeners implements Listener {

    public PlayerListeners(Main main) {
       main.getServer().getPluginManager().registerEvents(this, main);
    }

    public static List<Player> disable = new ArrayList();

    @EventHandler
    public void BrockBreakEvent(BlockBreakEvent e){

        //壊したブロックが原木であるか
        //survival or adventure でのみ使用可
        if(WoodUtil.isWood(e.getBlock().getType()) && !disable.contains(e.getPlayer())
            && (e.getPlayer().getGameMode().equals(GameMode.SURVIVAL) || e.getPlayer().getGameMode().equals(GameMode.ADVENTURE))){

            //Can only be used if it contains the "AXE" name
            ItemStack its = e.getPlayer().getInventory().getItemInMainHand();
            if(!its.getType().name().matches(".*" + "AXE" + ".*")) return;

            //select tree block
            Tree tree = new Tree(e.getBlock());
            if(!tree.isTree()) return;

            e.setCancelled(true);

            //cut
            tree.cut(e.getPlayer());
        }
    }
}
