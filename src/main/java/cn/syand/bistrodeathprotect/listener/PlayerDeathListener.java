package cn.syand.bistrodeathprotect.listener;

import cn.syand.bistrodeathprotect.BistroDeathProtect;
import cn.syand.bistrodeathprotect.config.PrisonList;
import cn.syand.bistrodeathprotect.config.ProtectConfig;
import cn.syand.bistrodeathprotect.runnable.DeathProtectTask;
import cn.syand.bistrodeathprotect.runnable.PrisonTask;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
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

        // 判断玩家是否在小黑屋中
        if (PrisonList.PRISON_LIST.contains(player.getName())) {
            // 阻止玩家收到伤害
            entityDamageEvent.setCancelled(true);
            return;
        }
        
        // 判断是否是中毒伤害
        if (entityDamageEvent.getCause().equals(EntityDamageEvent.DamageCause.POISON)) {
            return;
        }

        // 获取玩家当前血量
        double health = player.getHealth();
        // 获取最后一次伤害的值
        double finalDamage = entityDamageEvent.getFinalDamage();

        // 判断玩家是否会造成死亡
        if (health - finalDamage > 0) {
            return;
        }

        // 判断副手或当前手是否有不死图腾
        if (player.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)
                || player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING)) {
            return;
        }

        // 判断当前世界是否在黑名单中
        if (this.isWorldInBlackList(player)) {
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
        this.deductExperience(player);
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
            String deathSound = ProtectConfig.Setting.SOUND;
            player.playSound(player, Sound.valueOf(deathSound), 1.0F, 1.0F);
        } catch (Exception e) {
            throw new RuntimeException("音效不存在, 请在 config 中调整", e);
        }

        // 判断是否进入小黑屋
        if (this.enterBlackRoom(player)) {
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
     * @return 是否进入小黑屋
     */
    public boolean enterBlackRoom(Player player) {
        // 判断是否需要进入小黑屋
        if (!this.isEnterBlackRoom(player)) {
            return Boolean.FALSE;
        }

        // 获取小黑屋世界
        String worldName = ProtectConfig.Prison.Location.WORLD;
        double x = ProtectConfig.Prison.Location.X;
        double y = ProtectConfig.Prison.Location.Y;
        double z = ProtectConfig.Prison.Location.Z;
        if (Objects.isNull(worldName)) {
            throw new RuntimeException("小黑屋世界不存在, 请在 config 中调整");
        }

        // 获取小黑屋世界
        World world = Bukkit.getWorld(worldName);
        if (Objects.isNull(world)) {
            throw new RuntimeException("小黑屋世界不存在, 请在 config 中调整");
        }

        // 获取倒计时时间
        int time = ProtectConfig.Prison.TIME;

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
     */
    private boolean isEnterBlackRoom(Player player) {
        // 判断是否开启小黑屋功能
        if (!ProtectConfig.Prison.ENABLE) {
            return Boolean.FALSE;
        }

        // 获取当前玩家死亡次数
        int deaths = player.getStatistic(Statistic.DEATHS);
        // 判断死亡次数是否等于配置次数 死亡次数 % 配置次数 == 0
        if (deaths % ProtectConfig.Prison.DEATH != 0) {
            return Boolean.FALSE;
        }

        // 需要进入小黑屋
        return Boolean.TRUE;
    }

    /**
     * 扣除玩家经验
     *
     * @param player 玩家
     */
    private void deductExperience(Player player) {
        // 获取开启是否扣除经验功能
        if (!ProtectConfig.DeathPenalty.ENABLE) {
            return;
        }

        // 获取扣除经验值
        double level = ProtectConfig.DeathPenalty.LEVEL;
        if (level < 0 || level > 1) {
            return;
        }

        // 获取玩家当前经验
        double playerExperience = player.getLevel();
        if (playerExperience <= 0) {
            return;
        }

        // 扣除经验 百分比
        double deductExperience = playerExperience * level;
        player.setLevel((int) (playerExperience - deductExperience));
    }

    /**
     * 判断当前世界是否在黑名单中
     * 如果在黑名单中则返回 true
     * 否则返回 false
     *
     * @param player 玩家
     * @return 是否在黑名单中
     */
    private boolean isWorldInBlackList(Player player) {
        // 获取是否启用黑名单
        if (!ProtectConfig.BlackWorld.ENABLE) {
            return false;
        }

        // 获取玩家所在世界
        String worldName = player.getWorld().getName();

        // 判断
        return ProtectConfig.BlackWorld.WORLDS.contains(worldName);
    }
}
