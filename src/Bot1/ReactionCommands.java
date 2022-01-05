package Bot1;


import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
public class ReactionCommands extends ListenerAdapter {
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if(!event.getMember().getUser().equals(event.getJDA().getSelfUser())) {
            if(event.getReactionEmote().getName().equals(ChatCommand.emojis[0])) {
                ChatCommand.pollCount[0]++;
            }
            if(ChatCommand.count >= 2 && event.getReactionEmote().getName().equals(ChatCommand.emojis[1])) {
                ChatCommand.pollCount[1]++;
            }
            if(ChatCommand.count >= 3 && event.getReactionEmote().getName().equals(ChatCommand.emojis[2])) {
                ChatCommand.pollCount[2]++;
            }
            if(ChatCommand.count >= 4 && event.getReactionEmote().getName().equals(ChatCommand.emojis[3])) {
                ChatCommand.pollCount[3]++;
            }
            if(ChatCommand.count >= 5 && event.getReactionEmote().getName().equals(ChatCommand.emojis[4])) {
                ChatCommand.pollCount[4]++;
            }
            if(ChatCommand.count >= 6 && event.getReactionEmote().getName().equals(ChatCommand.emojis[5])) {
                ChatCommand.pollCount[5]++;
            }
            if(ChatCommand.count >= 7 && event.getReactionEmote().getName().equals(ChatCommand.emojis[6])) {
                ChatCommand.pollCount[6]++;
            }
            if(ChatCommand.count >= 8 && event.getReactionEmote().getName().equals(ChatCommand.emojis[7])) {
                ChatCommand.pollCount[7]++;
            }
            if(ChatCommand.count >= 9 && event.getReactionEmote().getName().equals(ChatCommand.emojis[8])) {
                ChatCommand.pollCount[8]++;
            }
            if(ChatCommand.count >= 10 && event.getReactionEmote().getName().equals(ChatCommand.emojis[9])) {
                ChatCommand.pollCount[9]++;
            }
        }
    }
}