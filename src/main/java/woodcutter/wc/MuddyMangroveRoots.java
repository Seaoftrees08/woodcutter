package woodcutter.wc;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class MuddyMangroveRoots {

    private final int MAX_TRANSITITION = 3;//四方への最大遷移数, (遷移: transition)
    private final int MAX_AMOUNTS = 100;
    private ArrayList<Block> roots = new ArrayList<>();

    public MuddyMangroveRoots(Block b){
        mmrLogic(b, 1);
    }

    private void mmrLogic(Block b, int transition){
        if(b.getType() != Material.MUDDY_MANGROVE_ROOTS || transition > MAX_TRANSITITION || roots.size()>MAX_AMOUNTS) return;

        if(!roots.contains(b)){
            roots.add(b);
        }else{
            return;
        }

        //四方としたを追加
        mmrLogic((new Location(b.getWorld(), b.getX()+1, b.getY(), b.getZ())).getBlock(), transition+1);
        mmrLogic((new Location(b.getWorld(), b.getX()-1, b.getY(), b.getZ())).getBlock(), transition+1);
        mmrLogic((new Location(b.getWorld(), b.getX(), b.getY(), b.getZ()+1)).getBlock(), transition+1);
        mmrLogic((new Location(b.getWorld(), b.getX(), b.getY(), b.getZ()-1)).getBlock(), transition+1);
        mmrLogic((new Location(b.getWorld(), b.getX(), b.getY()-1, b.getZ())).getBlock(), transition);
        mmrLogic((new Location(b.getWorld(), b.getX(), b.getY()+1, b.getZ())).getBlock(), transition);

    }

    public boolean dig(Player p){
        if(roots.size()>MAX_AMOUNTS) return false;
        ItemStack tool = p.getInventory().getItemInMainHand();
        new BukkitRunnable(){
            @Override
            public void run() {
                for(Block b : roots) b.breakNaturally(tool);
            }
        }.run();
        consumption(p, roots.size());
        return true;
    }

    /** プレイヤーの手に持っているツールの耐久値を減らす
     *
     * @param p Player
     * @param value 減らす値
     */
    private void consumption(Player p, int value){
        ItemStack tool = p.getInventory().getItemInMainHand();
        int level = tool.getEnchantmentLevel(Enchantment.DURABILITY);
        //cf https://minecraft-ja.gamepedia.com/%E8%80%90%E4%B9%85%E5%8A%9B
        double decreaseProbability = (60.0+(40.0/(level+1.0))) / 100.0;

        short decrease = (short)(durability(tool) + (short)(value*decreaseProbability));
        if(tool.getType().getMaxDurability() == durability(tool)){
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 1);
            p.spawnParticle(Particle.ITEM_CRACK, p.getLocation(), 40, tool);
            p.getInventory().setItemInMainHand(null);
            return;
        }else if(tool.getType().getMaxDurability() < decrease){
            decrease = tool.getType().getMaxDurability();
        }
        setDurability(tool, decrease);
    }

    private short durability(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        return meta==null ? 0 : (short)((Damageable)meta).getDamage();
    }

    private void setDurability(ItemStack item, short durability){
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            ((Damageable) meta).setDamage(durability);
            item.setItemMeta(meta);
        }
    }

}
