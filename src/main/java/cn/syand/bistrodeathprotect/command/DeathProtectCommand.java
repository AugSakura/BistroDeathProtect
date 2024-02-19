package cn.syand.bistrodeathprotect.command;

import cn.syand.bistrodeathprotect.BistroDeathProtect;
import cn.syand.bistrodeathprotect.enums.CommandEnums;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * PluginCommand
 * 插件命令
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/18
 */
public class DeathProtectCommand implements CommandExecutor {

    /**
     * 死亡保护 插件实例
     */
    private final BistroDeathProtect plugin;

    public DeathProtectCommand(BistroDeathProtect bistroDeathProtect) {
        this.plugin = bistroDeathProtect;
    }

    /**
     * 执行命令
     *
     * @param sender  命令发送者
     * @param command 命令
     * @param label   标签
     * @param args    参数
     * @return 是否执行成功
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // 获取语言配置
        YamlConfiguration languageConfig = plugin.getLanguageConfig();
        // 获取前缀
        String prefix = languageConfig.getString("prefix");

        if (args.length == 1) {
            // 重载配置
            if (CommandEnums.RELOAD.command().equalsIgnoreCase(args[0])) {
                plugin.reloadConfig();

                // 打印重载成功
                String info = ChatColor.translateAlternateColorCodes('&', prefix + languageConfig.get("reload.success"));
                sender.sendMessage(info);
                Bukkit.getConsoleSender().sendMessage(info);
                return true;
            }
        }

        return false;
    }
}
