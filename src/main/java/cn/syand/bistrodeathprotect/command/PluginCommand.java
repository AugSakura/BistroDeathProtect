package cn.syand.bistrodeathprotect.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * PluginCommand
 * 插件命令
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/18
 */
public class PluginCommand implements CommandExecutor {

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
        return false;
    }
}
