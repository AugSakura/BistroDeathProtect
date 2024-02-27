package cn.syand.bistrodeathprotect;

import cn.syand.bistrodeathprotect.command.DeathProtectCommand;
import cn.syand.bistrodeathprotect.config.LanguageInfo;
import cn.syand.bistrodeathprotect.config.PrisonList;
import cn.syand.bistrodeathprotect.config.ProtectConfig;
import cn.syand.bistrodeathprotect.constants.DeathProtectConstants;
import cn.syand.bistrodeathprotect.listener.PlayerDeathListener;
import cn.syand.bistrodeathprotect.listener.PlayerJoinListener;
import cn.syand.bistrodeathprotect.listener.PlayerPrisonListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * BistroDeathProtect
 * 酒馆死亡保护
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2023/12/4
 */
public final class BistroDeathProtect extends JavaPlugin {

    /**
     * 插件实例 单例
     */
    public static BistroDeathProtect INSTANCE;

    /**
     * 插件配置
     */
    private ProtectConfig protectConfig;

    /**
     * 小黑屋列表
     */
    private PrisonList prisonList;

    /**
     * 语言信息
     */
    private LanguageInfo languageInfo;

    public BistroDeathProtect() {
        INSTANCE = this;
    }

    @Override
    public void onLoad() {
        // 初始化配置
        this.protectConfig = new ProtectConfig(this);
        this.prisonList = new PrisonList(this);
        this.languageInfo = new LanguageInfo(this);
    }

    @Override
    public void onEnable() {
        // 加载配置
        protectConfig.loadConfig();

        // 加载小黑屋列表
        prisonList.loadConfig();

        // 加载语言信息
        languageInfo.loadConfig();

        // 注册命令
        PluginCommand command = this.getCommand(DeathProtectConstants.BASE_COMMAND);
        if (Objects.nonNull(command)) {
            command.setExecutor(new DeathProtectCommand(this));
        }

        // 注册事件监听器
        // 事件监听器: 玩家死亡监听器
        PlayerDeathListener playerDeathListener = new PlayerDeathListener(this);
        this.getServer().getPluginManager().registerEvents(playerDeathListener, this);

        // 事件监听器: 玩家加入游戏
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(playerDeathListener), this);

        // 事件监听器: 小黑屋
        this.getServer().getPluginManager().registerEvents(new PlayerPrisonListener(), this);

        // 打印插件信息
        Bukkit.getConsoleSender().sendMessage("§aBistroDeathProtect §f插件已加载");
        Bukkit.getConsoleSender().sendMessage("§aBistroDeathProtect §f插件由 §bAug_Sakura §f开发, QQ: §b1048064671");
    }

    @Override
    public void onDisable() {
        // 打印插件信息
        Bukkit.getConsoleSender().sendMessage("§aBistroDeathProtect §f插件已卸载");
    }
}
