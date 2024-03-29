package Bot1;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.util.HashMap;

public class HelloDiscord {
    public static String prefix = "~";
    public static String pog = ":pog:";
    public static int pogCount = 0;
    public static HashMap<Long, Integer> pogCounts = new HashMap<Long, Integer>();
    public static void main(String[] args) throws LoginException {
        JDA jda = JDABuilder.createDefault("NzM4NDE3MTE4NjAwNzU3Mzg4.XyLmiA.M5eC-r4Nvt0fPzzkf3AKGg5vLUA").build();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("with your feelings"));

        // Register listeners
        jda.addEventListener(new ChatCommand());
        jda.addEventListener(new ReactionCommands());
        //jda.build();
    }
}
