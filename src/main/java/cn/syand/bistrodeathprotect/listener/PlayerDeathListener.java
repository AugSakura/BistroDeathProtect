package cn.syand.bistrodeathprotect.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * PlayerDeathListener
 * 玩家死亡监听器
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/18
 */
public class PlayerDeathListener implements Listener {

    @EventHandler
    void onPlayerDeath(EntityDeathEvent entityDeathEvent) {
        // 判断死亡实体是否为玩家
        if (null == entityDeathEvent
                || !(entityDeathEvent.getEntity() instanceof Player)) {
            return;
        }

        // 是玩家
        Player player = (Player) entityDeathEvent.getEntity();

        // 复活玩家
    }
}
