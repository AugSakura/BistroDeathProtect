package cn.syand.bistrodeathprotect.listener;

import cn.syand.bistrodeathprotect.config.PrisonList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
     * 玩家死亡事件监听器
     */
    private final PlayerDeathListener playerDeathListener;

    public PlayerJoinListener(PlayerDeathListener playerDeathListener) {
        this.playerDeathListener = playerDeathListener;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        // 校验参数
        if (Objects.isNull(event)) {
            return;
        }

        // 判断小黑屋列表是否为空
        if (PrisonList.PRISON_LIST.isEmpty()) {
            return;
        }

        // 获取玩家名称 和 配置
        Player player = event.getPlayer();
        String name = player.getName();

        // 判断是否在小黑屋列表中 区分大小写
        boolean isPrison = PrisonList.PRISON_LIST.contains(name);
        if (!isPrison) {
            return;
        }

        // 如果在小黑屋中 那么开始执行小黑屋中的逻辑
        playerDeathListener.enterBlackRoom(player);
    }
}
