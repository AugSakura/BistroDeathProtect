package cn.syand.bistrodeathprotect.runnable;

import cn.syand.bistrodeathprotect.BistroDeathProtect;
import cn.syand.bistrodeathprotect.config.LanguageInfo;
import cn.syand.bistrodeathprotect.config.ProtectConfig;
import cn.syand.bistrodeathprotect.constants.DeathProtectConstants;
import cn.syand.bistrodeathprotect.utils.CommonUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
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
        this.time = ProtectConfig.Setting.TIME;

        // 清空玩家标题
        player.resetTitle();
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

        // 获取标题
        String title = LanguageInfo.Death.TITLE;
        // 获取副标题
        String subtitle = LanguageInfo.Death.SUBTITLE;

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
                CommonUtils.translateColor(title),
                CommonUtils.translateColor(subtitle),
                10,
                20,
                10
        );
    }

}
