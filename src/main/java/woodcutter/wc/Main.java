package woodcutter.wc;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        getCommand("woodcutter").setExecutor(new Commands());
        new PlayerListeners(this);

    }

    @Override
    public void onDisable() {
    }
}
