package cn.syand.bistrodeathprotect.runnable;

import cn.syand.bistrodeathprotect.BistroDeathProtect;
import cn.syand.bistrodeathprotect.constants.DeathProtectConstants;
import cn.syand.bistrodeathprotect.listener.PlayerPrisonListener;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * PrisonTask
 * 小黑屋任务
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/19
 */
public class PrisonTask extends BukkitRunnable {

    /**
     * 死亡保护 插件实例
     */
    private final BistroDeathProtect plugin;

    /**
     * 玩家信息
     */
    private final Player player;

    /**
     * 小黑屋倒计时间
     */
    private int time;

    /**
     * 小黑屋结束后执行的命令
     */
    private final List<String> commands;

    /**
     * 玩家小黑屋监听
     */
    private final PlayerPrisonListener playerPrisonListener;

    public PrisonTask(BistroDeathProtect plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.commands = plugin.getConfig().getStringList("prison.commands");

        // 开启玩家小黑屋监听
        this.playerPrisonListener = new PlayerPrisonListener(plugin);
        Bukkit.getPluginManager().registerEvents(playerPrisonListener, plugin);

        // 将玩家添加到小黑屋列表中
        plugin.addPrisonList(player.getName());

        // 获取死亡保护时间
        this.time = BistroDeathProtect.INSTANCE.getConfig().getInt("prison.time", 30);
    }

    /**
     * 开始任务
     * 传1则为1秒后执行，传20则为20秒后执行
     *
     * @param delay 延迟
     * @param time  间隔时间
     */
    public void start(long delay, long time) {
        this.runTaskTimer(this.plugin, 20L * delay, 20L * time);
    }

    @Override
    public void run() {
        // 判断玩家是否在线
        if (this.player == null || !this.player.isOnline()) {
            // 关闭玩家小黑屋监听
            HandlerList.unregisterAll(playerPrisonListener);
            this.cancel();
            return;
        }

        // 判断时间是否小于等于0
        if (this.time <= 0) {
            this.cancelRun();
            return;
        }

        // 获取语言配置
        YamlConfiguration languageConfig = this.plugin.getLanguageConfig();
        // 获取标题
        String title = languageConfig.getString("prison.title");
        // 获取副标题
        String subtitle = languageConfig.getString("prison.subtitle");
        if (title == null || subtitle == null) {
            throw new RuntimeException("title 或 subtitle 设置为空");
        }

        // 时间减少
        int timer = this.time;
        this.time--;

        // 替换自定义占位符
        title = title.replace(DeathProtectConstants.PLACEHOLDER_PRISON_TIME, String.valueOf(timer));
        subtitle = subtitle.replace(DeathProtectConstants.PLACEHOLDER_PRISON_TIME, String.valueOf(timer));

        // 替换papi占位符
        title = PlaceholderAPI.setPlaceholders(player, title);
        subtitle = PlaceholderAPI.setPlaceholders(player, subtitle);

        // 发送标题
        player.sendTitle(
                ChatColor.translateAlternateColorCodes('&', title),
                ChatColor.translateAlternateColorCodes('&', subtitle),
                10,
                20,
                10
        );
    }

    /**
     * 取消任务
     */
    public void cancelRun() {
        // 执行小黑屋结束后的命令
        Server server = this.plugin.getServer();
        commands.forEach(command -> server.dispatchCommand(
                server.getConsoleSender(),
                PlaceholderAPI.setPlaceholders(this.player, command)
        ));

        // 将玩家添加到小黑屋列表中
        plugin.removePrisonList(player.getName());

        // 关闭玩家小黑屋监听
        HandlerList.unregisterAll(playerPrisonListener);

        // 取消失明效果
        this.player.removePotionEffect(PotionEffectType.BLINDNESS);
        // 设置玩家模式为生存模式
        player.setGameMode(GameMode.SURVIVAL);

        // 取消任务
        this.cancel();
    }
}
