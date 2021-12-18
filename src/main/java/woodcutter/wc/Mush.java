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
import java.util.List;

public class Mush {

    private final int MAX_AMOUNT = 200;
    private final boolean mush;
    private final List<Block> mushLog = new ArrayList<Block>();
    private boolean canCut = true;

    /** 木を選択するコンストラクタ
     *
     * @param b 最初に破壊したブロック  キノコでなければならない
     */
    public Mush(Block b) {
        mush = isMush(b);
    }

    /** 木であるかどうかを返す
     *
     * @return 木：Ture 木でない:False
     */
    public boolean isMush(){
        return mush;
    }

    /** 選択したものが木であるかを判別し、返す
     *
     * @param b 最初に壊したブロック
     * @return 木：Ture 木でない:False
     */
    private boolean isMush(Block b) {
        Location under = b.getLocation().clone();
        under.setY(under.getY()-1);
        Material ub = under.getBlock().getType();

        //原木の下が土系のブロックか
        if(!(ub.equals(Material.DIRT)
                || ub.equals(Material.PODZOL)
                || ub.equals(Material.COARSE_DIRT)
                || ub.equals(Material.GRASS_BLOCK)
        )){
            return false;
        }

        //原木と、隣接している葉をフィールドに入れていく
        mushLogic(b);

        //葉が原木に一つでも隣接していればTrueを返す
        return mushLog.size() > 0;
    }

    /** 周囲のブロックを検査し、原木があればさらにその周りを検査する
     *
     * @param firstBlock 最初に壊したブロック  キノコでなければならない
     */
    private void mushLogic(Block firstBlock) {
        Location l = firstBlock.getLocation().clone();
        canCut = searchAround(l);
    }

    /** ある原木の周りを検査し、原木と葉をフィールドに追加する
     *
     * ある原木のxy周囲8マス、y座標1増加した9増マス分を検査し
     * 同種原木なら再帰、葉なら追加してreturnする
     *
     *
     * @param center 検査する原木の座標
     * @return 検査の最大値を超えたかどうか (超えたらfalse)
     */
    private boolean searchAround(Location center){
        if(mushLog.size() > MAX_AMOUNT) return false;
        Location l = center.clone();
        Block b;

        //y=-1, 0, 1の順番に検査
        for(int y=-1; y<2; y++){
            l.setY(center.getY() + y);
            for(int x=-1; x<2; x++){
                l.setX(center.getX() + x);
                for(int z=-1; z<2; z++){
                    l.setZ(center.getZ() + z);
                    b = l.getBlock();
                    if(isMushLog(b.getType())){
                        if(!mushLog.contains(b)){
                            mushLog.add(b);
                            searchAround(l);
                        }
                    }
                }
            }
        }
        return !(mushLog.size() > MAX_AMOUNT);
    }

    /** 木を切る
     *
     * @param p 木を切ったPlayer
     * @return 実行したかどうか(trueは実行)
     */
    public boolean cut(Player p){
        if(!canCut) return false;
        ItemStack tool = p.getInventory().getItemInMainHand();
        new BukkitRunnable(){
            @Override
            public void run() {
                for(Block b : mushLog) b.breakNaturally(tool);
            }
        }.run();

        consumption(p, mushLog.size());
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

    public static boolean isMushLog(Material m){
        return m.equals(Material.MUSHROOM_STEM) || m.equals(Material.BROWN_MUSHROOM_BLOCK) || m.equals(Material.RED_MUSHROOM_BLOCK);
    }

}
