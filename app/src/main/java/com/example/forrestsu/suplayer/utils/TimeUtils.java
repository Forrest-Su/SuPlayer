package com.example.forrestsu.suplayer.utils;

public class TimeUtils {

    /**
     * 将毫秒转换为“时，分，秒”
     * @param milliSeconds 毫秒数
     * @param format  输出格式
     * @return 根据格式返回结果
     */
    public static String milliSecondsTo (Long milliSeconds, String format) {
        Long totalSeconds = milliSeconds / 1000;
        Long totalMinutes = totalSeconds / 60;
        int hours = (int) (totalMinutes / 60);
        int minutes = (int) (totalMinutes % 60);
        int seconds = (int) (totalSeconds % 60);

        String hour, minute, second;
        if (hours > 9) {
            hour = String.valueOf(hours);
        } else {
            hour = "0" + String.valueOf(hours);
        }

        if (minutes > 9) {
            minute = String.valueOf(minutes);
        } else {
            minute = "0" + String.valueOf(minutes);
        }

        if (seconds > 9) {
            second = String.valueOf(seconds);
        } else {
            second = "0" + String.valueOf(seconds);
        }

        String result = "";

        switch (format) {
            case ":/:/:":
                result = hour + ":" + minute + ":" + second;
                break;
            case "时/分/秒":
                result = hour + "时" + minute + "分" + second + "秒";
                break;
            case "h/m/s/":
                result = hour + "h" + minute + "m" + second + "s";
                break;
            default:
                break;
        }
        return result;
    }
}
