package cn.syand.bistrodeathprotect.enums;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * DeathProtectCommandEnums
 * 酒馆死亡保护命令枚举
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/19
 */
@RequiredArgsConstructor
public enum CommandEnums {

    /**
     * 重载命令
     */
    RELOAD("reload", "bistro.death.command.reload"),

    /**
     * 设置小黑屋地点
     */
    SET_PRISON("setPrison", "bistro.death.command.setPrison"),

    /**
     * 未知
     */
    UNKNOWN("unknown", "");

    /**
     * 命令
     */
    private final String command;

    /**
     * 权限
     */
    private final String permission;

    /**
     * 获取命令
     *
     * @return 命令
     */
    public String command() {
        return command;
    }

    /**
     * 获取权限
     */
    public String permission() {
        return permission;
    }

    /**
     * 枚举map集合
     * key:    命令
     * value:  枚举
     */
    private static final Map<String, CommandEnums> ENUM_MAP = new HashMap<>((int) ((CommandEnums.values().length / 0.75) + 1));

    static {
        // 添加枚举到map集合
        for (CommandEnums command : CommandEnums.values()) {
            // 不添加 UNKNOWN
            if (command.command().toLowerCase().equals(UNKNOWN.command)) {
                continue;
            }

            ENUM_MAP.put(command.command().toLowerCase(), command);
        }
    }

    /**
     * 获取MAP集合
     */
    public static Map<String, CommandEnums> getEnumMap() {
        return ENUM_MAP;
    }

    /**
     * 根据command获取枚举
     * 默认返回 UNKNOWN
     *
     * @param command 命令
     * @return 枚举类
     */
    public static CommandEnums getByCommand(String command) {
        if (Objects.isNull(command)) {
            return UNKNOWN;
        }

        return ENUM_MAP.getOrDefault(command.toLowerCase(), UNKNOWN);
    }
}
