package cn.syand.bistrodeathprotect.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * BlackWorld
 * 黑名单世界
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/18
 */
@Data
@AllArgsConstructor
public class BlackWorld implements ConfigurationSerializable {

    /**
     * 前缀
     */
    public static final String PREFIX = "blackWorld";

    /**
     * 是否启用黑名单世界
     */
    private boolean enable;

    /**
     * 黑名单世界列表
     */
    private String[] worlds;

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        // 数据集合
        Map<String, Object> data = new HashMap<>(16);

        // 添加数据
        data.put("enable", this.enable);
        data.put("worlds", this.worlds);
        return data;
    }

    /**
     * 反序列化
     *
     * @param args 参数
     * @return 黑名单世界对象
     */
    public static BlackWorld deserialize(Map<String, Object> args) {
        return new BlackWorld((Boolean) args.get("time"), (String[]) args.get("worlds"));
    }
}
