package cn.syand.bistrodeathprotect.runnable;

import cn.syand.bistrodeathprotect.BistroDeathProtect;
import cn.syand.bistrodeathprotect.constants.DeathProtectConstants;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

/**
 * DeathProtectTask
 * 死亡保护任务
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/19
 */
public class DeathProtectTask extends BukkitRunnable {

    /**
     * 死亡保护 插件实例
     */
    private final BistroDeathProtect plugin;

    /**
     * 玩家信息
     */
    private final Player player;

    /**
     * 死亡保护时间
     */
    private int time;

    public DeathProtectTask(BistroDeathProtect plugin, Player player) {
        this.plugin = plugin;
        this.player = player;

        // 获取死亡保护时间
        this.time = BistroDeathProtect.INSTANCE.getConfig().getInt("setting.time", 5);
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
        if (Objects.isNull(player) || !player.isOnline()) {
            this.cancel();
            return;
        }

        // 判断时间是否小于等于0
        if (time <= 0) {
            player.setGameMode(GameMode.SURVIVAL);
            this.cancel();
            return;
        }

        // 获取语言配置
        YamlConfiguration languageConfig = this.plugin.getLanguageConfig();
        // 获取标题
        String title = languageConfig.getString("death.title");
        // 获取副标题
        String subtitle = languageConfig.getString("death.subtitle");
        if (Objects.isNull(title) || Objects.isNull(subtitle)) {
            throw new RuntimeException("title 或 subtitle 设置为空");
        }

        // 时间减少
        int timer = this.time;
        this.time--;

        // 替换自定义占位符
        title = title.replace(DeathProtectConstants.PLACEHOLDER_TIME, String.valueOf(timer));
        subtitle = subtitle.replace(DeathProtectConstants.PLACEHOLDER_TIME, String.valueOf(timer));

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

}
