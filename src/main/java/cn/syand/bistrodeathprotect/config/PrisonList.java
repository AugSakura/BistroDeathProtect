package cn.syand.bistrodeathprotect.config;

import cn.syand.bistrodeathprotect.BistroDeathProtect;
import cn.syand.bistrodeathprotect.constants.DeathProtectConstants;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

/**
 * PrisonList
 * 小黑屋列表
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/27
 */
public class PrisonList {

    /**
     * 单例模式
     */
    public static PrisonList INSTANCE;

    /**
     * 挂机池插件实例
     */
    public final BistroDeathProtect plugin;

    /**
     * 插件目录
     */
    public static File PLUGIN_DIR;

    /**
     * 小黑屋列表
     */
    public static List<String> PRISON_LIST;

    public PrisonList(BistroDeathProtect plugin) {
        this.plugin = plugin;
        PLUGIN_DIR = plugin.getDataFolder();
        INSTANCE = this;
    }

    /**
     * 重载
     */
    public static void reloadConfig() {
        INSTANCE.loadConfig();
        INSTANCE.saveConfig();
    }

    /**
     * 初始化
     */
    public void saveDefaultConfig() {
        // 保存已存在小黑屋中的玩家列表
        File prisonListFile = new File(PLUGIN_DIR, DeathProtectConstants.PRISON_PATH);
        if (!prisonListFile.exists()) {
            plugin.saveResource(DeathProtectConstants.PRISON_PATH, false);
        }
    }

    /**
     * 加载配置
     */
    public void loadConfig() {
        // 初始化
        saveDefaultConfig();

        // 获取配置
        File listFile = new File(PLUGIN_DIR, DeathProtectConstants.PRISON_PATH);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(listFile);
        PRISON_LIST = configuration.getStringList("player");
    }

    /**
     * 保存配置
     */
    public void saveConfig() {
        // 获取配置
        File listFile = new File(PLUGIN_DIR, DeathProtectConstants.PRISON_PATH);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(listFile);
        configuration.set("player", PRISON_LIST);
        try {
            configuration.save(listFile);
        } catch (Exception e) {
            throw new RuntimeException("保存小黑屋列表失败");
        }
    }

    /**
     * 添加玩家
     *
     * @param name 玩家名称
     */
    public static void addPlayer(String name) {
        // 判断是否存在
        if (PRISON_LIST.contains(name)) {
            return;
        }

        // 不存在则添加玩家
        PRISON_LIST.add(name);

        // 保存
        INSTANCE.saveConfig();
    }

    /**
     * 移除玩家
     *
     * @param name 玩家名称
     */
    public static void removePlayer(String name) {
        // 判断是否存在
        if (!PRISON_LIST.contains(name)) {
            // 不存在就返回
            return;
        }

        // 存在则移除玩家
        PRISON_LIST.remove(name);

        // 保存
        INSTANCE.saveConfig();
    }
}
