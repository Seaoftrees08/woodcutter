package woodcutter.wc;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
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

    public static List<Player> disable = new ArrayList<Player>();

    @EventHandler
    public void BrockBreakEvent(BlockBreakEvent e){

        //サバイバルとアドベンチャー以外除外
        if(!e.getPlayer().getGameMode().equals(GameMode.SURVIVAL) && !e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) return;

        //壊したブロックが原木orキノコであるか
        //survival or adventure でのみ使用可
        if((WoodUtil.isWood(e.getBlock().getType()) || Mush.isMushLog(e.getBlock().getType())) && !disable.contains(e.getPlayer())){

            //Can only be used if it contains the "AXE" name
            ItemStack its = e.getPlayer().getInventory().getItemInMainHand();
            if(!its.getType().name().matches(".*" + "AXE" + ".*")) return;

            //select tree block
            if(Mush.isMushLog(e.getBlock().getType())){
                Mush mush = new Mush(e.getBlock());
                if(!mush.isMush()) return;
                boolean execute = mush.cut(e.getPlayer());
                if(!execute) e.getPlayer().sendMessage(ChatColor.RED + "[woodcutter] This tree is so big that plugin cannot cut this.");
            }else{
                Tree tree = new Tree(e.getBlock());
                if(!tree.isTree()) return;
                tree.cut(e.getPlayer());
            }

            e.setCancelled(true);

        //シャベルでマングローブの泥の根を破壊した時
        }else if(e.getBlock().getType() == Material.MUDDY_MANGROVE_ROOTS && !disable.contains(e.getPlayer())){

            //Can only be used if it contains the "AXE" name
            ItemStack its = e.getPlayer().getInventory().getItemInMainHand();
            if(!its.getType().name().matches(".*" + "SHOVEL" + ".*")) return;


            if(e.getBlock().getType().equals(Material.MUDDY_MANGROVE_ROOTS)){
                var mmr = new MuddyMangroveRoots(e.getBlock());
                mmr.dig(e.getPlayer());
            }

        }
    }
}
