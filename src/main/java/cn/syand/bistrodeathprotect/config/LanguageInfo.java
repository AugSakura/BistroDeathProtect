package cn.syand.bistrodeathprotect.config;

import cn.syand.bistrodeathprotect.BistroDeathProtect;
import cn.syand.bistrodeathprotect.constants.DeathProtectConstants;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * LanguageInfo
 * 语言文件
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/27
 */
public class LanguageInfo {

    /**
     * 单例模式
     */
    public static LanguageInfo INSTANCE;

    /**
     * 挂机池插件实例
     */
    public final BistroDeathProtect plugin;

    /**
     * 插件目录
     */
    public static File PLUGIN_DIR;

    /**
     * 前缀
     */
    public static String PREFIX;

    /**
     * 没有权限
     */
    public static String NO_PERMISSION;

    /**
     * 死亡提示
     */
    public static class Death {
        /**
         * 标题
         */
        public static String TITLE;

        /**
         * 副标题
         */
        public static String SUBTITLE;
    }

    /**
     * 小黑屋提示
     */
    public static class Prison {
        /**
         * 标题
         */
        public static String TITLE;

        /**
         * 副标题
         */
        public static String SUBTITLE;

        /**
         * 指令禁止提示
         */
        public static String PROHIBITION;

        /**
         * 小黑屋设置提示
         */
        public static String SET_SUCCESS;
    }

    /**
     * 重载提示
     */
    public static class Reload {
        /**
         * 重载成功
         */
        public static String SUCCESS;
    }

    public LanguageInfo(BistroDeathProtect plugin) {
        this.plugin = plugin;
        PLUGIN_DIR = plugin.getDataFolder();
        INSTANCE = this;
    }

    /**
     * 重载
     */
    public static void reloadConfig() {
        INSTANCE.loadConfig();
    }

    /**
     * 初始化
     */
    public void saveDefaultConfig() {
        // 语言文件地址
        String languagePath = DeathProtectConstants.LANGUAGE_PATH + DeathProtectConstants.DEFAULT_LANGUAGE;

        // 保存默认语言配置
        File outFile = new File(PLUGIN_DIR, languagePath);
        if (!outFile.exists()) {
            plugin.saveResource(languagePath, false);
        }
    }

    /**
     * 加载配置
     */
    public void loadConfig() {
        // 初始化
        saveDefaultConfig();

        // 获取配置
        File languageFile = new File(PLUGIN_DIR, DeathProtectConstants.LANGUAGE_PATH + plugin.getConfig().getString("setting.language") + DeathProtectConstants.FILE_SUFFIX);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(languageFile);

        // 前缀
        PREFIX = configuration.getString("prefix");

        // 没有权限
        NO_PERMISSION = configuration.getString("no-permission");

        // 死亡提示
        Death.TITLE = configuration.getString("death.title");
        Death.SUBTITLE = configuration.getString("death.subtitle");

        // 小黑屋提示
        Prison.TITLE = configuration.getString("prison.title");
        Prison.SUBTITLE = configuration.getString("prison.subtitle");
        Prison.PROHIBITION = configuration.getString("prison.prohibition");
        Prison.SET_SUCCESS = configuration.getString("prison.set-success");

        // 重载提示
        Reload.SUCCESS = configuration.getString("reload.success");
    }
}
