package cn.syand.bistrodeathprotect.listener;

import cn.syand.bistrodeathprotect.BistroDeathProtect;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.Objects;

/**
 * PlayerJoinListener
 * 玩家加入监听器
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/19
 */
public class PlayerJoinListener implements Listener {

    /**
     * 死亡保护 插件实例
     */
    private final BistroDeathProtect plugin;

    /**
     * 玩家死亡事件监听器
     */
    private final PlayerDeathListener playerDeathListener;

    public PlayerJoinListener(BistroDeathProtect bistroDeathProtect, PlayerDeathListener playerDeathListener) {
        this.plugin = bistroDeathProtect;
        this.playerDeathListener = playerDeathListener;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        // 校验参数
        if (Objects.isNull(event)) {
            return;
        }

        // 获取当前在小黑屋中的玩家列表
        List<String> prisonList = this.plugin.getPrisonList();
        if (prisonList.isEmpty()) {
            return;
        }

        // 获取玩家名称 和 配置
        FileConfiguration config = this.plugin.getConfig();
        Player player = event.getPlayer();
        String name = player.getName();

        // 判断是否在小黑屋中 区分大小写
        boolean isPrison = prisonList.contains(name);
        if (!isPrison) {
            return;
        }

        // 如果在小黑屋中 那么开始执行小黑屋中的逻辑
        playerDeathListener.enterBlackRoom(player, config);
    }
}
