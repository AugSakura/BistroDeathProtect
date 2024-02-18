package cn.syand.bistrodeathprotect.config;

import cn.syand.bistrodeathprotect.BistroDeathProtect;
import cn.syand.bistrodeathprotect.constants.PluginConstants;
import cn.syand.bistrodeathprotect.entity.BlackWorld;
import cn.syand.bistrodeathprotect.entity.Prison;
import cn.syand.bistrodeathprotect.entity.PrisonLocation;
import cn.syand.bistrodeathprotect.entity.Setting;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * PluginConfig
 * 插件配置
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/18
 */
public class PluginConfig {

    /**
     * 重载并保存配置
     */
    public void reloadConfig() {
        // 获取插件数据文件夹
        File dataFolder = BistroDeathProtect.getProvidingPlugin(BistroDeathProtect.class).getDataFolder();

        // 获取配置文件
        File configFile = new File(dataFolder, PluginConstants.CONFIG_PATH);

        // 获取默认配置文件
        YamlConfiguration defaultConfig = getDefaultConfig(configFile);

        // 复制默认配置
        defaultConfig.options().copyDefaults(true);

        // 保存配置
        try {
            defaultConfig.save(configFile);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("§cBistroDeathProtect §f配置文件保存失败");
        }
    }

    /**
     * 获取默认配置
     *
     * @param configFile 配置文件
     * @return 默认配置
     */
    public YamlConfiguration getDefaultConfig(File configFile) {
        // 转化为配置文件
        YamlConfiguration configYml = YamlConfiguration.loadConfiguration(configFile);

        // 设置默认配置
        configYml.addDefault(Setting.PREFIX, new Setting(5, "ENTITY_PLAYER_DEATH"));

        // 黑名单世界设置
        configYml.addDefault(BlackWorld.PREFIX, new BlackWorld(false, new String[]{"world_nether", "world_the_end"}));

        // 小黑屋设置
        PrisonLocation location = new PrisonLocation(PluginConstants.DEFAULT_WORLD_NAME, 0, 0, 0);
        configYml.addDefault(Prison.PREFIX, new Prison(
                false,
                3,
                30,
                new String[]{"spawns teleport default %player%"},
                location));
        return configYml;
    }

}
