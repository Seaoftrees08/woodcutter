package woodcutter.wc;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Arrays;
import java.util.List;

public class WoodUtil {

    final static private List<Material> logs = Arrays.asList(
            Material.OAK_LOG,
            Material.SPRUCE_LOG,
            Material.BIRCH_LOG,
            Material.JUNGLE_LOG,
            Material.ACACIA_LOG,
            Material.DARK_OAK_LOG
    );

    final static private List<Material> leaves = Arrays.asList(
            Material.OAK_LEAVES,
            Material.SPRUCE_LEAVES,
            Material.BIRCH_LEAVES,
            Material.JUNGLE_LEAVES,
            Material.ACACIA_LEAVES,
            Material.DARK_OAK_LEAVES
    );

    final static private List<Material> saplings = Arrays.asList(
            Material.OAK_SAPLING,
            Material.SPRUCE_SAPLING,
            Material.BIRCH_SAPLING,
            Material.JUNGLE_SAPLING,
            Material.ACACIA_SAPLING,
            Material.DARK_OAK_SAPLING
    );

    /** Materialが原木かどうかを返す
     *
     * @param type Material
     * @return 原木:true 原木でない:false
     */
    public static boolean isWood(Material type){
        for(Material m : logs){
            if(type.equals(m)) return true;
        }
        return false;
    }

    /** Materialが葉かどうかを返す
     *
     * @param type Naterial
     * @return 葉:true 葉でない:false
     */
    public static boolean isLeavses(Material type){
        for(Material m : leaves){
            if(type.equals(m)) return true;
        }
        return false;
    }

    /** woodcutterでの木の種類を返す
     *  オーク -> 0
     *  松    -> 1
     *  白樺  -> 2
     *  ジャングル  -> 3
     *  アカシア    -> 4
     *  ダークオーク -> 5
     *
     * @param wood 原木
     * @return woodcutterでの木の種類
     */
    public static int getIndex(Material wood){
        int i=0;
        for(Material m : logs){
            if(wood.equals(m)) return i;
            i++;
        }
        return -1;
    }

    /** woodcutterでの木の種類を原木に変換する
     *
     * @param index woodcutterでの木の種類
     * @return 原木のMaterial
     */
    public static Material getLogMaterial(int index){
        return logs.get(index);
    }

    /** woodcutterでの木の種類を葉に変換する
     *
     * @param index woodcutterでの木の種類
     * @return 葉のMaterial
     */
    public static Material getLeavesMaterial(int index){
        return leaves.get(index);
    }

    /** woodcutterでの木の種類を苗に変換する
     *
     * @param index woodcutterでの木の種類
     * @return 苗のMaterial
     */
    public static Material getSaplingMaterial(int index){
        return saplings.get(index);
    }


}
