package cn.syand.bistrodeathprotect.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Setting
 * 整体设置实体类
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/18
 */
@Data
@AllArgsConstructor
public class Setting implements ConfigurationSerializable {

    /**
     * 前缀
     */
    public static final String PREFIX = "setting";

    /**
     * 死亡保护时间
     */
    private long time;

    /**
     * 死亡时音效
     */
    private String sound;

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        // 数据集合
        Map<String, Object> data = new HashMap<>(16);

        // 添加数据
        data.put("time", this.time);
        data.put("sound", this.sound);
        return data;
    }

    /**
     * 反序列化
     *
     * @param args 参数
     * @return 设置对象
     */
    public static Setting deserialize(@NotNull Map<String, Object> args) {
        return new Setting(NumberConversions.toLong(args.get("time")), args.get("sound").toString());
    }
}
