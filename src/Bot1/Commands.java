package Bot1;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class Commands extends ListenerAdapter {
    public static long evan = 428624339970031629L;
    public static long michael = 439035903344771075L;
    public static long chris = 708370704235757678L;
    public static int chrisStop = 0;
    public static String stopCode = "stop that";
    public static String goCode = "come back";
    public static boolean stop = false;
    private static boolean hadPog = false;


    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        String temp = "";
        for (String word : args) {
            if (word.toLowerCase().contains("pog") && !event.getAuthor().isBot() && !stop) {
                if (HelloDiscord.pogCounts.get(event.getMember().getIdLong()) == null) {
                    setMemberInPog(event.getMember());
                }
                HelloDiscord.pogCounts.put(event.getMember().getIdLong(), HelloDiscord.pogCounts.get(event.getMember().getIdLong()) + 1);
                hadPog = true;
            }
            temp += word + " ";
        }
        if (temp.toLowerCase().contains(stopCode) && event.getAuthor().getIdLong() == michael && !stop) {
            stop = true;
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("Ok sorry to bother").queue();
        }
        if(temp.toLowerCase().contains(goCode) && event.getAuthor().getIdLong() == michael && stop) {
            stop = false;
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("Guess who's back? Back again.").queue();
        }
        if (hadPog) {
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(event.getMember().getEffectiveName() + "'s pog count is now **" + HelloDiscord.pogCounts.get(event.getMember().getIdLong()) + "**").queue();
            hadPog = false;
        }
        if (event.getAuthor().getIdLong() == evan && !stop) {
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("Sure pal").queue();
        }
        if (event.getAuthor().getIdLong() == chris && !stop) {
            chrisStop++;
            if (chrisStop == 1) {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("Stop talking Chris").queue();
            } else {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("Stop talking Chris. I've told you " + chrisStop + " times to stop talking.").queue();
            }
        }
    }

    private void setMemberInPog(Member member) {
        if (!member.getUser().isBot()) {
            HelloDiscord.pogCounts.put(member.getIdLong(), 0);
        }
    }
}
