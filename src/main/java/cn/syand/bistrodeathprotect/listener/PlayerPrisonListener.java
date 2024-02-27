package cn.syand.bistrodeathprotect.listener;

import cn.syand.bistrodeathprotect.config.LanguageInfo;
import cn.syand.bistrodeathprotect.config.PrisonList;
import cn.syand.bistrodeathprotect.utils.CommonUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

/**
 * PlayerPrisonListener
 * 玩家小黑监听事件
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/19
 */
public class PlayerPrisonListener implements Listener {

    /**
     * 判断是否是小黑屋玩家
     *
     * @param player 玩家
     * @return 是否是当前玩家
     */
    private boolean isCurrentPlayer(Player player) {
        // 判断小黑屋列表是否为空
        if (PrisonList.PRISON_LIST.isEmpty()) {
            return Boolean.TRUE;
        }

        return !PrisonList.PRISON_LIST.contains(player.getName());
    }

    /**
     * 玩家放置监听
     *
     * @param entityPlaceEvent 玩家放置事件
     */
    @EventHandler
    public void onPlayerPlace(EntityPlaceEvent entityPlaceEvent) {
        // 判断是否为玩家
        if (Objects.isNull(entityPlaceEvent)
                || !(entityPlaceEvent.getEntity() instanceof Player)) {
            return;
        }

        // 判断是否为当前玩家
        if (this.isCurrentPlayer((Player) entityPlaceEvent.getEntity())) {
            return;
        }

        // 取消放置
        entityPlaceEvent.setCancelled(true);
    }

    /**
     * 玩家使用物品监听
     *
     * @param playerInteractEvent 玩家使用物品事件
     */
    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent playerInteractEvent) {
        // 校验参数
        if (Objects.isNull(playerInteractEvent)) {
            return;
        }

        // 判断是否为当前玩家
        if (this.isCurrentPlayer(playerInteractEvent.getPlayer())) {
            return;
        }

        // 取消使用物品
        playerInteractEvent.setCancelled(true);
    }

    /**
     * 玩家聊天命令监听
     *
     * @param playerCommandPreprocessEvent 玩家聊天命令事件
     */
    @EventHandler
    public void onPlayerChatCommand(PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
        // 校验参数
        if (Objects.isNull(playerCommandPreprocessEvent)) {
            return;
        }

        // 判断是否为当前玩家
        if (this.isCurrentPlayer(playerCommandPreprocessEvent.getPlayer())) {
            return;
        }

        // 取消聊天命令
        playerCommandPreprocessEvent.setCancelled(true);

        // 获取当前玩家信息
        Player player = playerCommandPreprocessEvent.getPlayer();

        // 替换papi占位符
        String prohibition = LanguageInfo.Prison.PROHIBITION;
        String info = PlaceholderAPI.setPlaceholders(player, prohibition);

        // 发送到玩家自己的聊天中
        player.sendMessage(CommonUtils.translateColor(LanguageInfo.PREFIX + info));
    }

}
