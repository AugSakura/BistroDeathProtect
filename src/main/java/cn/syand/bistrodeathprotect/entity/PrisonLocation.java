package cn.syand.bistrodeathprotect.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Location
 * 小黑屋的位置实体类
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/18
 */
@Data
@AllArgsConstructor
public class PrisonLocation implements ConfigurationSerializable {
    /**
     * 前缀
     */
    public static final String PREFIX = "location";

    private String world;
    private double x;
    private double y;
    private double z;

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        // 数据集合
        Map<String, Object> data = new HashMap<>(16);

        data.put("world", this.world);
        data.put("x", this.x);
        data.put("y", this.y);
        data.put("z", this.z);

        return data;
    }

    public static PrisonLocation deserialize(Map<String, Object> args) {
        return new PrisonLocation(args.get("world").toString(), NumberConversions.toDouble(args.get("x")), NumberConversions.toDouble(args.get("y")), NumberConversions.toDouble(args.get("z")));
    }
}