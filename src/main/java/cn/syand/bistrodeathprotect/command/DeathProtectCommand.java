package cn.syand.bistrodeathprotect.command;

import cn.syand.bistrodeathprotect.BistroDeathProtect;
import cn.syand.bistrodeathprotect.config.LanguageInfo;
import cn.syand.bistrodeathprotect.config.PrisonList;
import cn.syand.bistrodeathprotect.config.ProtectConfig;
import cn.syand.bistrodeathprotect.constants.DeathProtectConstants;
import cn.syand.bistrodeathprotect.enums.CommandEnums;
import cn.syand.bistrodeathprotect.utils.CommonUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
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
        if (args.length == 1) {
            // 获取二级命令
            CommandEnums commandEnums = CommandEnums.getByCommand(args[0]);

            switch (commandEnums) {
                case RELOAD:
                    // 重载插件
                    this.reloadConfig(sender);
                    break;
                case SET_PRISON:
                    // 设置小黑屋
                    this.setPrison(sender);
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
     * @param sender 命令发送者
     */
    private void reloadConfig(CommandSender sender) {
        // 校验参数
        if (Objects.isNull(sender)) {
            return;
        }

        // 校验权限
        if (!sender.hasPermission(CommandEnums.RELOAD.permission())) {
            sender.sendMessage(CommonUtils.translateColor(LanguageInfo.PREFIX + LanguageInfo.NO_PERMISSION));
            return;
        }

        // 重载语言配置
        LanguageInfo.reloadConfig();

        // 重载配置
        ProtectConfig.reloadConfig();

        // 重载小黑屋列表
        PrisonList.reloadConfig();

        // 打印重载成功
        sender.sendMessage(CommonUtils.translateColor(LanguageInfo.PREFIX + LanguageInfo.Reload.SUCCESS));
    }

    /**
     * 设置小黑屋
     *
     * @param sender 命令发送者
     */
    private void setPrison(CommandSender sender) {
        // 校验参数
        if (Objects.isNull(sender)) {
            return;
        }

        // 校验权限
        if (!sender.hasPermission(CommandEnums.RELOAD.permission())) {
            sender.sendMessage(CommonUtils.translateColor(LanguageInfo.PREFIX + LanguageInfo.NO_PERMISSION));
            return;
        }

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

        // 保存配置
        plugin.saveConfig();

        // 打印设置成功
        sender.sendMessage(CommonUtils.translateColor(LanguageInfo.PREFIX + LanguageInfo.Prison.SET_SUCCESS));
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // 判断是否为第一个参数
        if (args.length == 1) {
            // 返回tab补全
            return new ArrayList<>(CommandEnums.getEnumMap().keySet());
        }
        return Collections.emptyList();
    }
}
