package cn.syand.bistrodeathprotect.command;

import cn.syand.bistrodeathprotect.BistroDeathProtect;
import cn.syand.bistrodeathprotect.constants.DeathProtectConstants;
import cn.syand.bistrodeathprotect.enums.CommandEnums;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * PluginCommand
 * 插件命令
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/18
 */
public class DeathProtectCommand implements CommandExecutor, TabExecutor {

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
            // 获取二级命令
            CommandEnums commandEnums = CommandEnums.getByCommand(args[0]);

            switch (commandEnums) {
                case RELOAD:
                    // 重载插件
                    this.reloadConfig(languageConfig, prefix, sender);
                    break;
                case SET_PRISON:
                    // 设置小黑屋
                    this.setPrison(languageConfig, prefix, sender);
                    break;
                default:
                    break;
            }

            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    /**
     * 重载配置
     *
     * @param languageConfig 语言配置
     * @param prefix         前缀
     * @param sender         命令发送者
     */
    private void reloadConfig(YamlConfiguration languageConfig, String prefix, CommandSender sender) {
        // 重载配置
        plugin.reloadConfig();

        // 打印重载成功
        String info = ChatColor.translateAlternateColorCodes('&', prefix + languageConfig.get("reload.success"));
        sender.sendMessage(info);
        Bukkit.getConsoleSender().sendMessage(info);
    }

    /**
     * 设置小黑屋
     *
     * @param languageConfig 语言配置
     * @param prefix         前缀
     * @param sender         命令发送者
     */
    private void setPrison(YamlConfiguration languageConfig, String prefix, CommandSender sender) {
        // 获取发送者的信息
        Player player = sender.getServer().getPlayer(sender.getName());
        if (Objects.isNull(player)) {
            return;
        }

        // 获取发送者的位置信息
        Location location = player.getLocation();
        World world = location.getWorld();

        // 获取世界名称
        String worldName = Objects.isNull(world) ? DeathProtectConstants.DEFAULT_WORLD_NAME : world.getName();

        // 设置小黑屋的位置
        plugin.getConfig().set("prison.location.x", location.getX());
        plugin.getConfig().set("prison.location.y", location.getY());
        plugin.getConfig().set("prison.location.z", location.getZ());
        plugin.getConfig().set("prison.location.world", worldName);

        // 刷新配置
        plugin.saveConfig();

        // 打印设置成功
        String info = ChatColor.translateAlternateColorCodes('&', prefix + languageConfig.get("prison.setSuccess"));
        sender.sendMessage(info);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // 判断是否为第一个参数
        if (args.length == 1) {
            // 返回tab补全
            return new ArrayList<>(CommandEnums.getEnumMap().keySet());
        }

        return null;
    }
}
