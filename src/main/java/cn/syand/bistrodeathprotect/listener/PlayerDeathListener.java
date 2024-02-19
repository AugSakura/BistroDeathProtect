package cn.syand.bistrodeathprotect.listener;

import cn.syand.bistrodeathprotect.BistroDeathProtect;
import cn.syand.bistrodeathprotect.runnable.DeathProtectTask;
import cn.syand.bistrodeathprotect.runnable.PrisonTask;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

/**
 * PlayerDeathListener
 * 玩家死亡监听器
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/18
 */
public class PlayerDeathListener implements Listener {

    /**
     * 死亡保护 插件实例
     */
    private final BistroDeathProtect plugin;

    public PlayerDeathListener(BistroDeathProtect bistroDeathProtect) {
        this.plugin = bistroDeathProtect;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent entityDamageEvent) {
        // 判断死亡实体是否为玩家
        if (null == entityDamageEvent
                || !(entityDamageEvent.getEntity() instanceof Player)) {
            return;
        }

        // 玩家信息 和 配置信息
        Player player = (Player) entityDamageEvent.getEntity();
        FileConfiguration config = this.plugin.getConfig();

        // 获取玩家当前血量
        double health = player.getHealth();
        // 获取最后一次伤害的值
        double finalDamage = entityDamageEvent.getFinalDamage();

        // 判断玩家是否会造成死亡
        if (health - finalDamage > 0) {
            return;
        }

        // 判断玩家是否在黑名单中
        if (this.isWorldInBlackList(player, config)) {
            // 使用原生死亡
            return;
        }

        // 获取玩家总血量
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (Objects.isNull(healthAttribute)) {
            return;
        }
        double maxHealth = healthAttribute.getValue();

        // 玩家死亡次数增加
        player.setStatistic(Statistic.DEATHS, player.getStatistic(Statistic.DEATHS) + 1);
        // 扣除玩家经验
        this.deductExperience(player, config);
        // 阻止玩家死亡
        entityDamageEvent.setCancelled(true);
        // 清除玩家药水效果
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        // 清除玩家着火等状态
        player.setFireTicks(0);
        // 设置玩家血量为满血
        player.setHealth(maxHealth);
        try {
            // 播放死亡声音
            String deathSound = config.getString("setting.sound");
            player.playSound(player, Sound.valueOf(deathSound), 1.0F, 1.0F);
        } catch (Exception e) {
            throw new RuntimeException("音效不存在, 请在 config 中调整", e);
        }

        // 判断是否进入小黑屋
        if (this.enterBlackRoom(player, config)) {
            return;
        }

        // 设置玩家游戏模式
        player.setGameMode(GameMode.SPECTATOR);
        // 开启死亡保护任务
        new DeathProtectTask(plugin, player).start(0, 1);
    }

    /**
     * 进入小黑屋
     *
     * @param player 玩家
     * @param config 配置
     * @return 是否进入小黑屋
     */
    public boolean enterBlackRoom(Player player, FileConfiguration config) {
        // 判断是否需要进入小黑屋
        if (!this.isEnterBlackRoom(player, config)) {
            return Boolean.FALSE;
        }

        // 获取小黑屋世界
        String worldName = config.getString("prison.location.world");
        double x = config.getDouble("prison.location.x");
        double y = config.getDouble("prison.location.y");
        double z = config.getDouble("prison.location.z");
        if (Objects.isNull(worldName)) {
            throw new RuntimeException("小黑屋世界不存在, 请在 config 中调整");
        }

        // 获取小黑屋世界
        World world = Bukkit.getWorld(worldName);
        if (Objects.isNull(world)) {
            throw new RuntimeException("小黑屋世界不存在, 请在 config 中调整");
        }

        // 获取倒计时时间
        int time = config.getInt("prison.time");

        // 传送玩家到小黑屋
        player.teleport(new Location(world, x, y, z));
        // 设置玩家失明 持续时间为配置的时间 * 2
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, time * 2 * 20, 1));

        // 小黑屋倒计时
        new PrisonTask(plugin, player).start(0, 1);
        return Boolean.TRUE;
    }

    /**
     * 判断是否需要进入小黑屋
     *
     * @param player 玩家
     * @param config 配置
     */
    private boolean isEnterBlackRoom(Player player, FileConfiguration config) {
        // 判断是否开启小黑屋功能
        if (!config.getBoolean("prison.enable")) {
            return Boolean.FALSE;
        }

        // 获取当前玩家死亡次数
        int deaths = player.getStatistic(Statistic.DEATHS);
        // 判断死亡次数是否等于配置次数 死亡次数 % 配置次数 == 0
        if (deaths % config.getInt("prison.death") != 0) {
            return Boolean.FALSE;
        }

        // 需要进入小黑屋
        return Boolean.TRUE;
    }

    /**
     * 扣除玩家经验
     *
     * @param player 玩家
     * @param config 配置
     */
    private void deductExperience(Player player, FileConfiguration config) {
        // 获取开启是否扣除经验功能
        if (!config.getBoolean("deathPenalty.enable")) {
            return;
        }

        // 获取扣除经验值
        double level = config.getDouble("deathPenalty.level");
        if (level < 0 || level > 1) {
            return;
        }

        // 获取玩家当前经验
        double playerExperience = player.getLevel();
        Bukkit.getConsoleSender().sendMessage("playerExperience: " + playerExperience);
        if (playerExperience <= 0) {
            return;
        }

        // 扣除经验 百分比
        double deductExperience = playerExperience * level;
        Bukkit.getConsoleSender().sendMessage("deductExperience: " + deductExperience);
        player.setLevel((int) (playerExperience - deductExperience));
    }

    /**
     * 判断当前世界是否在黑名单中
     * 如果在黑名单中则返回 true
     * 否则返回 false
     *
     * @param player 玩家
     * @param config 配置
     * @return 是否在黑名单中
     */
    private boolean isWorldInBlackList(Player player, FileConfiguration config) {
        // 获取是否启用黑名单
        if (!config.getBoolean("blackWorld.enable")) {
            return false;
        }

        // 获取玩家所在世界
        String worldName = player.getWorld().getName();

        // 判断
        return config.getStringList("blackWorld.worlds").contains(worldName);
    }
}
