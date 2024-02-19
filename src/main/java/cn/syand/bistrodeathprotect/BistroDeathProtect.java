package cn.syand.bistrodeathprotect;

import cn.syand.bistrodeathprotect.command.DeathProtectCommand;
import cn.syand.bistrodeathprotect.constants.DeathProtectConstants;
import cn.syand.bistrodeathprotect.listener.PlayerDeathListener;
import com.google.common.base.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
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

        // 文件地址
        String languagePath = DeathProtectConstants.LANGUAGE_PATH + DeathProtectConstants.DEFAULT_LANGUAGE;
        // 保存默认语言配置
        File outFile = new File(this.getDataFolder(), languagePath);
        if (!outFile.exists()) {
            this.saveResource(languagePath, false);
        }

        // 注册命令
        PluginCommand command = this.getCommand("bistrodeathprotect");
        if (Objects.nonNull(command)) {
            command.setExecutor(new DeathProtectCommand(this));
        }

        // 注册事件监听器
        // 事件监听器: 玩家死亡监听器
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);

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
}
