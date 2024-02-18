package cn.syand.bistrodeathprotect;

import cn.syand.bistrodeathprotect.command.PluginCommand;
import cn.syand.bistrodeathprotect.listener.PlayerDeathListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * BistroDeathProtect
 * 酒馆死亡保护
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2023/12/4
 */
public final class BistroDeathProtect extends JavaPlugin {

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        // 保存默认配置
        this.saveDefaultConfig();
        // 保存默认语言配置
        this.saveResource("lang/zh_CN.yml", false);

        // 注册命令
        this.getCommand("bistrodeathprotect").setExecutor(new PluginCommand());

        // 注册事件监听器
        // 事件监听器: 玩家死亡监听器
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);

        // 打印插件信息
        Bukkit.getConsoleSender().sendMessage("§aBistroDeathProtect §f插件已加载");
        Bukkit.getConsoleSender().sendMessage("§aBistroDeathProtect §f插件由 §bAug_Sakura §f开发, QQ: §b1048064671");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // 打印插件信息
        Bukkit.getConsoleSender().sendMessage("§aBistroDeathProtect §f插件已卸载");
    }
}
