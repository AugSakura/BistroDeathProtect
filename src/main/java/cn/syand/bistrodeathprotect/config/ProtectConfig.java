package cn.syand.bistrodeathprotect.config;

import cn.syand.bistrodeathprotect.BistroDeathProtect;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;

/**
 * ProtectConfig
 * 死亡保护配置
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/27
 */
public class ProtectConfig {

    /**
     * 单例模式
     */
    public static ProtectConfig INSTANCE;

    /**
     * 挂机池插件实例
     */
    public final BistroDeathProtect plugin;

    /**
     * 插件目录
     */
    public static File PLUGIN_DIR;

    /**
     * 整体设置
     */
    public static class Setting {
        /**
         * 死亡保护时间
         */
        public static int TIME;

        /**
         * 死亡时音效
         */
        public static String SOUND;

        /**
         * 语言文件
         */
        public static String LANGUAGE;
    }

    /**
     * 死亡惩罚
     */
    public static class DeathPenalty {
        /**
         * 是否开启死亡惩罚
         */
        public static boolean ENABLE;

        /**
         * 扣除等级 百分比
         */
        public static int LEVEL;
    }

    /**
     * 世界黑名单
     */
    public static class BlackWorld {
        /**
         * 是否开启世界黑名单
         */
        public static boolean ENABLE;

        /**
         * 黑名单世界列表
         */
        public static List<String> WORLDS;
    }

    /**
     * 小黑屋设置
     */
    public static class Prison {
        /**
         * 是否开启小黑屋
         */
        public static boolean ENABLE;

        /**
         * 累计死亡多少次后进入小黑屋
         */
        public static int DEATH;

        /**
         * 小黑屋坐标
         */
        public static class Location {
            /**
             * 世界
             */
            public static String WORLD;

            /**
             * X
             */
            public static double X;

            /**
             * Y
             */
            public static double Y;

            /**
             * Z
             */
            public static double Z;
        }

        /**
         * 小黑屋倒计时
         */
        public static int TIME;

        /**
         * 小黑屋倒计时结束后执行的命令
         */
        public static List<String> COMMANDS;
    }

    public ProtectConfig(BistroDeathProtect plugin) {
        this.plugin = plugin;
        PLUGIN_DIR = plugin.getDataFolder();
        INSTANCE = this;
    }

    /**
     * 重载配置
     */
    public static void reloadConfig() {
        INSTANCE.loadConfig();
    }

    /**
     * 加载配置文件
     */
    public void loadConfig() {
        // 保存默认配置
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        // 获取配置文件
        FileConfiguration config = this.plugin.getConfig();

        // 整体设置
        Setting.TIME = config.getInt("setting.time", 3);
        Setting.SOUND = config.getString("setting.sound", "ENTITY_PLAYER_DEATH");
        Setting.LANGUAGE = config.getString("setting.language", "zh_CN");

        // 死亡惩罚
        DeathPenalty.ENABLE = config.getBoolean("death-penalty.enable", false);
        DeathPenalty.LEVEL = config.getInt("death-penalty.level", 0);

        // 世界黑名单
        BlackWorld.ENABLE = config.getBoolean("black-world.enable", false);
        BlackWorld.WORLDS = config.getStringList("black-world.worlds");

        // 小黑屋设置
        Prison.ENABLE = config.getBoolean("prison.enable", false);
        Prison.DEATH = config.getInt("prison.death", 3);
        Prison.Location.WORLD = config.getString("prison.location.world", "world");
        Prison.Location.X = config.getDouble("prison.location.x", 0);
        Prison.Location.Y = config.getDouble("prison.location.y", 0);
        Prison.Location.Z = config.getDouble("prison.location.z", 0);
        Prison.TIME = config.getInt("prison.time", 30);
        Prison.COMMANDS = config.getStringList("prison.commands");
    }

}
