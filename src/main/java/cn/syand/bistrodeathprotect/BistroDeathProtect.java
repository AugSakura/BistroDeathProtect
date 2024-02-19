package cn.syand.bistrodeathprotect;

import cn.syand.bistrodeathprotect.command.DeathProtectCommand;
import cn.syand.bistrodeathprotect.constants.DeathProtectConstants;
import cn.syand.bistrodeathprotect.listener.PlayerDeathListener;
import cn.syand.bistrodeathprotect.listener.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
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

    public BistroDeathProtect() {
        INSTANCE = this;
    }


    @Override
    public void onEnable() {
        // 保存配置
        this.saveDefaultConfig();

        // 语言文件地址
        String languagePath = DeathProtectConstants.LANGUAGE_PATH + DeathProtectConstants.DEFAULT_LANGUAGE;
        // 保存默认语言配置
        File outFile = new File(this.getDataFolder(), languagePath);
        if (!outFile.exists()) {
            this.saveResource(languagePath, false);
        }

        // 保存已存在小黑屋中的玩家列表
        File prisonListFile = new File(this.getDataFolder(), DeathProtectConstants.PRISON_PATH);
        if (!prisonListFile.exists()) {
            this.saveResource(DeathProtectConstants.PRISON_PATH, false);
        }

        // 注册命令
        PluginCommand command = this.getCommand("bistrodeathprotect");
        if (Objects.nonNull(command)) {
            command.setExecutor(new DeathProtectCommand(this));
        }

        // 注册事件监听器
        // 事件监听器: 玩家死亡监听器
        PlayerDeathListener playerDeathListener = new PlayerDeathListener(this);
        this.getServer().getPluginManager().registerEvents(playerDeathListener, this);

        // 事件监听器: 玩家加入游戏
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, playerDeathListener), this);

        // 打印插件信息
        Bukkit.getConsoleSender().sendMessage("§aBistroDeathProtect §f插件已加载");
        Bukkit.getConsoleSender().sendMessage("§aBistroDeathProtect §f插件由 §bAug_Sakura §f开发, QQ: §b1048064671");
    }

    @Override
    public void onDisable() {
        // 打印插件信息
        Bukkit.getConsoleSender().sendMessage("§aBistroDeathProtect §f插件已卸载");
    }

    /**
     * 获取语言配置
     *
     * @return 语言配置
     */
    public YamlConfiguration getLanguageConfig() {
        FileConfiguration config = this.getConfig();
        String language = config.getString("setting.language");

        // 语言文件 和 语言资源
        File languageFile = new File(this.getDataFolder(), DeathProtectConstants.LANGUAGE_PATH + language + DeathProtectConstants.FILE_SUFFIX);
        return YamlConfiguration.loadConfiguration(languageFile);
    }

    /**
     * 获取小黑屋中的玩家列表
     *
     * @return 小黑屋中的玩家列表
     */
    public List<String> getPrisonList() {
        // 获取配置
        File prisonListFile = new File(this.getDataFolder(), DeathProtectConstants.PRISON_PATH);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(prisonListFile);
        return configuration.getStringList("player");
    }

    /**
     * 将玩家添加到小黑屋列表中
     *
     * @param name 玩家名称
     */
    public void addPrisonList(String name) {
        // 获取配置
        File prisonListFile = new File(this.getDataFolder(), DeathProtectConstants.PRISON_PATH);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(prisonListFile);

        // 获取玩家列表
        List<String> player = configuration.getStringList("player");
        // 判断是否存在
        if (player.contains(name)) {
            return;
        }

        // 不存在则添加玩家
        player.add(name);

        // 保存
        configuration.set("player", player);
        try {
            configuration.save(prisonListFile);
        } catch (Exception e) {
            throw new RuntimeException("保存小黑屋列表失败");
        }
    }

    /**
     * 将玩家从小黑屋列表中移除
     *
     * @param name 玩家名称
     */
    public void removePrisonList(String name) {
        // 获取配置
        File prisonListFile = new File(this.getDataFolder(), DeathProtectConstants.PRISON_PATH);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(prisonListFile);

        // 获取玩家列表
        List<String> player = configuration.getStringList("player");
        // 判断是否存在
        if (!player.contains(name)) {
            return;
        }

        // 存在则移除玩家
        player.removeIf(playerName -> playerName.equals(name));

        // 保存
        configuration.set("player", player);
        try {
            configuration.save(prisonListFile);
        } catch (Exception e) {
            throw new RuntimeException("保存小黑屋列表失败");
        }
    }
}
