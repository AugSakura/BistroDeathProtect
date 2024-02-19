package cn.syand.bistrodeathprotect.listener;

import cn.syand.bistrodeathprotect.BistroDeathProtect;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
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
     * 死亡保护 插件实例
     */
    private final BistroDeathProtect plugin;

    public PlayerPrisonListener(BistroDeathProtect bistroDeathProtect) {
        this.plugin = bistroDeathProtect;
    }

    /**
     * 玩家受伤监听
     *
     * @param entityDamageEvent 玩家受伤事件
     */
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent entityDamageEvent) {
        // 判断死亡实体是否为玩家
        if (null == entityDamageEvent
                || !(entityDamageEvent.getEntity() instanceof Player)) {
            return;
        }

        // 清除玩家着火等状态
        Player player = (Player) entityDamageEvent.getEntity();
        player.setFireTicks(0);

        // 取消伤害
        entityDamageEvent.setCancelled(true);
    }

    /**
     * 玩家破坏监听
     *
     * @param blockBreakEvent 玩家破坏事件
     */
    @EventHandler
    public void onPlayerDestroy(BlockBreakEvent blockBreakEvent) {
        // 判断是否为玩家
        if (Objects.isNull(blockBreakEvent)) {
            return;
        }

        // 取消破坏
        blockBreakEvent.setCancelled(true);
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

        // 取消使用物品
        playerInteractEvent.setCancelled(true);
    }

    /**
     * 玩家丢弃物品监听
     */
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent playerDropItemEvent) {
        // 校验参数
        if (Objects.isNull(playerDropItemEvent)) {
            return;
        }

        // 取消丢弃物品
        playerDropItemEvent.setCancelled(true);
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

        // 取消聊天命令
        playerCommandPreprocessEvent.setCancelled(true);

        // 获取当前玩家信息
        Player player = playerCommandPreprocessEvent.getPlayer();

        // 获取语言配置
        YamlConfiguration languageConfig = this.plugin.getLanguageConfig();
        // 获取前缀
        String prefix = languageConfig.getString("prefix");
        // 获取发送的数据
        String prohibition = languageConfig.getString("prison.prohibition");
        if (Objects.isNull(prefix) || Objects.isNull(prohibition)) {
            throw new RuntimeException("prefix 或 prohibition 设置为空");
        }

        // 替换papi占位符
        prohibition = PlaceholderAPI.setPlaceholders(player, prohibition);

        // 发送到玩家自己的聊天中
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + prohibition));
    }

}
