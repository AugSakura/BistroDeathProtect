package cn.syand.bistrodeathprotect.utils;

import cn.syand.bistrodeathprotect.constants.DeathProtectConstants;
import org.bukkit.ChatColor;

import java.util.Objects;

/**
 * CommonUtils
 * 通用工具类
 *
 * @author Aug_Sakura
 * @version 1.0
 * @date 2024/02/27
 */
public class CommonUtils {

    /**
     * 转换消息颜色符号
     *
     * @param message 消息
     * @return 转换后的消息
     */
    public static String translateColor(String message) {
        // 校验参数
        if (Objects.isNull(message) || message.isEmpty()) {
            return message;
        }

        return ChatColor.translateAlternateColorCodes(DeathProtectConstants.COLOR_SYMBOL, message);
    }

}