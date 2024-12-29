package net.whu.ctotem.client;

public class CtotemConfig {
    public static float healthLevel = 2.0f;
    public static String command = "lobby";

    public static void loadDefault() {
        healthLevel = 2.0f;
        command = "lobby";
    }
}
