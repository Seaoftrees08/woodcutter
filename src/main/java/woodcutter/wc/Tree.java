package woodcutter.wc;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Tree{

    private boolean tree;
    private Material logType;
    private Material leaveType;
    private Material saplingType;
    private int index;
    private List<Block> treeLog = new ArrayList();
    private List<Block> treeLeaves = new ArrayList();
    private List<Location> firstLayerLoc = new ArrayList();

    /** 木を選択するコンストラクタ
     *
     * @param b 最初に破壊したブロック  原木でなければならない
     */
    public Tree(Block b) {
        logType = b.getType();
        index = WoodUtil.getIndex(b.getType());
        leaveType = WoodUtil.getLeavesMaterial(index);
        saplingType = WoodUtil.getSaplingMaterial(index);
        tree = isTree(b);
    }

    /** 木であるかどうかを返す
     *
     * @return 木：Ture 木でない:False
     */
    public boolean isTree(){
        return tree;
    }

    /** 選択したものが木であるかを判別し、返す
     *
     * @param b 最初に壊したブロック
     * @return 木：Ture 木でない:False
     */
    private boolean isTree(Block b) {
        Location under = b.getLocation().clone();
        under.setY(under.getY()-1);
        Material ub = under.getBlock().getType();

        //原木の下が土系のブロックか
        if(!(ub.equals(Material.DIRT) || ub.equals(Material.PODZOL) || ub.equals(Material.COARSE_DIRT)
            || ub.equals(Material.GRASS_BLOCK) || ub.equals(Material.MYCELIUM))){
            return false;
        }

        //原木と、隣接している葉をフィールドに入れていく
        orkLogic(b);

        //葉が原木に一つでも隣接していればTrueを返す
        return treeLeaves.size() > 0;
    }

    /** 周囲のブロックを検査し、原木があればさらにその周りを検査する
     *
     * @param firstBlock 最初に壊したブロック  原木でなければならない
     */
    private void orkLogic(Block firstBlock) {
        Location l = firstBlock.getLocation().clone();
        boolean firstLayer = true;

        while (l.getBlock().getType().equals(logType)) {
            searchAround(l, firstLayer);
            if(firstLayer) firstLayer = false;
            l.setY(l.getY()+1);
        }
    }

    /** ある原木の周りを検査し、原木と葉をフィールドに追加する
     *
     * ある原木のxy周囲8マス、y座標1増加した9増マス分を検査し
     * 同種原木なら再帰、葉なら追加してreturnする
     *
     *
     * @param center 検査する原木の座標
     * @param firstLayer 地面に隣接している場所かどうか
     */
    private void searchAround(Location center, boolean firstLayer){
        Location l = center.clone();
        Block b = l.getBlock();

        if(b.getType().equals(logType)) {
            if(!treeLog.contains(b)) treeLog.add(b);
            if(firstLayer) firstLayerLoc.add(l.clone());
        }else if(b.getType().equals(leaveType)){
            if(!treeLeaves.contains(b)) treeLeaves.add(b);
            return;
        }else{
            return;
        }

        for(int h=0; h<2; h++) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    l.setX(center.getX() + i);
                    l.setZ(center.getZ() + j);
                    b = l.getBlock();

                    if (b.getType().equals(logType) && !treeLog.contains(b)) {
                        treeLog.add(b);
                        searchAround(l, firstLayer);
                    } else if (b.getType().equals(leaveType) && !treeLeaves.contains(b)) {
                        treeLeaves.add(b);
                    }
                }
            }
            firstLayer = false;
            l.setY(center.getY()+1);
        }
    }

    /** 木を切る
     *
     * @param p 木を切ったPlayer
     */
    public void cut(Player p){
        ItemStack tool = p.getInventory().getItemInMainHand();
        new BukkitRunnable(){
            @Override
            public void run() {
                for(Block b : treeLog) b.breakNaturally(tool);
                for(Block b : treeLeaves) b.breakNaturally();
                for(Location l : firstLayerLoc) l.getBlock().setType(saplingType);
            }
        }.run();

        consumption(p, treeLog.size());
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

        short decrease = (short)(tool.getDurability() + (short)(value*decreaseProbability));
        if(tool.getType().getMaxDurability() == tool.getDurability()){
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 1);
            p.getWorld().spawnParticle(Particle.ITEM_CRACK, p.getLocation(), 40, tool);
            p.getInventory().setItemInMainHand(null);
            return;
        }else if(tool.getType().getMaxDurability() < decrease){
            decrease = tool.getType().getMaxDurability();
        }
        tool.setDurability(decrease);
    }

}
