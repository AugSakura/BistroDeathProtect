package cn.syand.bistrodeathprotect.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Prison
 * 小黑屋实体类
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/18
 */
@Data
@AllArgsConstructor
public class Prison implements ConfigurationSerializable {

    /**
     * 前缀
     */
    public static final String PREFIX = "prison";

    /**
     * 是否启用小黑屋
     */
    private boolean enable;

    /**
     * 累计死亡多少次后进入小黑屋
     */
    private int death;

    /**
     * 小黑屋倒计时时间
     */
    private long time;

    /**
     * 小黑屋倒计时结束后执行的命令
     */
    private String[] commands;

    /**
     * 小黑屋的位置
     */
    private PrisonLocation prisonLocation;

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        // 数据集合
        Map<String, Object> data = new HashMap<>(16);

        data.put("enable", this.enable);
        data.put("death", this.death);
        data.put("time", this.time);
        data.put("commands", this.commands);
        data.put(PrisonLocation.PREFIX, this.prisonLocation.serialize());

        return data;
    }

    /**
     * 反序列化
     *
     * @param args 参数
     * @return 对象
     */
    public static Prison deserialize(@NotNull Map<String, Object> args) {

        // 获取位置对象
        PrisonLocation prisonLocation = null;
        if (args.containsKey(PrisonLocation.PREFIX) && args.get(PrisonLocation.PREFIX) instanceof Map) {
            prisonLocation = PrisonLocation.deserialize((Map<String, Object>) args.get(PrisonLocation.PREFIX));
        }

        // 返回对象
        return new Prison(
                (Boolean) args.get("enable"),
                NumberConversions.toInt(args.get("death")),
                NumberConversions.toLong(args.get("time")),
                (String[]) args.get("commands"),
                prisonLocation
        );
    }
}
