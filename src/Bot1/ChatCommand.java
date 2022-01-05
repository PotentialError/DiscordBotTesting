package Bot1;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ChatCommand extends ListenerAdapter {
    //TODO: Make function to end poll early and only allow the person who made the poll to delete it
    //TODO: Add try-catches for functions
    private static final long codeOwner = 439035903344771075L;
    private static final String stopCode = "stop";
    private static final String goCode = "comeback";
    public static final String[] emojis = new String[]{"1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣", "\uD83D\uDD1F"};
    public static int count;
    private static boolean stop = false;
    public static File file = new File("G:/DiscordBotSuggestions.txt");
    private static BufferedWriter writer;
    private boolean writerOpen = false;

    public static int[] pollCount;
    public static String[] pollChoices;
    private static boolean gotPoll = false;
    public static boolean pollOngoing = false;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //Variables declared
        String[] userMessageArray = event.getMessage().getContentRaw().split("\\s+");

        //Turn the array into a string
        String userMessageString = "";
        for (String word : userMessageArray) {
            //check if pog is contained in any word
            if (word.toLowerCase().contains("pog") && !event.getAuthor().isBot() && !stop) {
                if (HelloDiscord.pogCounts.get(event.getMember().getIdLong()) == null) {
                    setMemberInPog(event.getMember());
                }
                HelloDiscord.pogCounts.put(event.getMember().getIdLong(), HelloDiscord.pogCounts.get(event.getMember().getIdLong()) + 1);
            }
            userMessageString += word + " ";
        }
        userMessageString = userMessageString.substring(0, userMessageString.length() - 1);

        //Gives users pog count
        if (userMessageArray[0].equalsIgnoreCase(HelloDiscord.prefix + "pog")) {
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(event.getMember().getEffectiveName() + "'s pog count is **" + HelloDiscord.pogCounts.get(event.getMember().getIdLong()) + "**").queue();
        }

        //Make help embed menu
        if (userMessageArray[0].equalsIgnoreCase(HelloDiscord.prefix + "help")) {
            EmbedBuilder helpMenu = new EmbedBuilder();
            helpMenu.setTitle("MikeyBot Commands:", "https://www.youtube.com/watch?v=oHg5SJYRHA0");
            helpMenu.addField("Timed Poll", "Make a poll with up to 10 choices that all other users can vote for through reactions\n  *One poll at a time\n  *Max time 1000s\n\n~poll [Time(seconds)]|[Question]|[Option1]|[Option2]...|[Option10]", false);
            helpMenu.addField("Pog Counter", "Gives you the amount of times you have said pog in this server\n\n~pog", false);
            helpMenu.addField("Reminder", "Reminds you about a message you provide in a certain amount of time\n\n~remind [Hours]|[Minutes]|[Seconds]|[Message]", false);
            helpMenu.addField("Suggestion Box", "Allows you to give feedback to codeOwner on bugs or feature ideas.\n  *250 character limit\n\n~suggestion [Message]", false);
            helpMenu.setColor(new Color(0x3228ed));
            RestAction<String> getAvatar = event.getJDA().retrieveUserById(codeOwner).map(User::getAvatarUrl);
            String url = getAvatar.complete();
            helpMenu.setFooter("Bot made by: YeOldMicycle", url);
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessageEmbeds(helpMenu.build()).queue();
        }
        //Set reminder
        if (userMessageArray[0].equalsIgnoreCase(HelloDiscord.prefix + "remind")) {
            try {
                int hours = Integer.parseInt(userMessageString.substring(8, userMessageString.indexOf("|")));
                String remindMessage = userMessageString.substring(userMessageString.indexOf("|") + 1);
                int minutes = Integer.parseInt(remindMessage.substring(0, remindMessage.indexOf("|")));
                remindMessage = remindMessage.substring(remindMessage.indexOf("|") + 1);
                int seconds = Integer.parseInt(remindMessage.substring(0, remindMessage.indexOf("|")));
                remindMessage = remindMessage.substring(remindMessage.indexOf("|") + 1);
                int time = hours * 60 * 60 + minutes * 60 + seconds;
                String timeMessage = "";
                if(hours > 1) {
                    timeMessage = hours + " hours ";
                }
                else if(hours == 1) {
                    timeMessage = hours + " hour ";
                }
                if(minutes > 1) {
                    timeMessage+= minutes + " minutes ";
                }
                else if(minutes == 1) {
                    timeMessage+= minutes + " minute ";
                }
                if(seconds > 1) {
                    timeMessage+= seconds + " seconds";
                }
                else if(seconds == 1) {
                    timeMessage+= seconds + " second";
                }
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("Reminder will be sent in: " + timeMessage).queue();
                new Timer().schedule(new RemindTimer(event, event.getMember().getId(), remindMessage), time * 1000);
            }
            catch (Exception e){
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("You did not type the command properly. Do ~help to view the full list of commands.\n~remind [Hours]|[Minutes]|[Seconds]|[Message]").queue();
            }

        }
        //Suggestion box
        if(userMessageArray[0].equalsIgnoreCase(HelloDiscord.prefix + "suggestion")) {
            try {
                if(userMessageString.substring(12).length() > 250) {
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage("Sorry your message is too long. Max character length is 250").queue();
                }
                else {
                    if(!writerOpen) {
                        writerOpen = true;
                        writer = new BufferedWriter(new FileWriter(file));
                    }
                    writer.write(event.getAuthor().getName() + ": " + userMessageString.substring(12));
                    writer.newLine();
                    writer.flush();
                    event.getMessage().addReaction(":pog:728345362015846490").queue();
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage("Suggestion successfully sent").queue();
                }
            } catch (Exception e) {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("You did not type the command properly. Do ~help to view the full list of commands.\n~suggestion [Message]").queue();
            }
        }
        //Take users message and put it into a poll format in an embed
        if (userMessageArray[0].equalsIgnoreCase(HelloDiscord.prefix + "poll") && pollOngoing) {
            //Warn user they cannot make a poll while one is active
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("There is currently a poll running! Wait until the poll ends to make a new one.").queue();
        } else if (userMessageArray[0].equalsIgnoreCase(HelloDiscord.prefix + "poll")) {
            try {
                //Make poll if there is no ongoing poll
                //Get the time and question from user message
                int time = Integer.parseInt(userMessageString.substring(6, userMessageString.indexOf("|")));
                String pollString = userMessageString.substring(userMessageString.indexOf("|") + 1);
                String question = pollString.substring(0, pollString.indexOf("|"));
                //Incase user puts a line at the end
                if (pollString.charAt(pollString.length() - 1) == '|') {
                    pollString = pollString.substring(0, pollString.length() - 1);
                }
                //Get choices from user and format for poll
                pollString = pollString.substring(pollString.indexOf("|"));
                count = 0;
                String answers = "";
                for (int i = 0; i < pollString.length(); i++) {
                    if (pollString.charAt(i) == '|') {
                        answers = answers + "\n" + (count + 1) + ". ";
                        count++;
                    } else {
                        answers = answers + pollString.charAt(i);
                    }
                }
                //Put choices into array
                pollCount = new int[count];
                pollChoices = new String[count];
                String choicesTemp = pollString.substring(1) + "|";
                int tempCount = 0;
                while (tempCount < count) {
                    pollChoices[tempCount] = choicesTemp.substring(0, choicesTemp.indexOf("|"));
                    choicesTemp = choicesTemp.substring(choicesTemp.indexOf("|") + 1);
                    tempCount++;
                }
                //Make the embed and put info
                EmbedBuilder poll = new EmbedBuilder();
                poll.setTitle("Poll Time!", "https://www.youtube.com/watch?v=oHg5SJYRHA0");
                if (time > 1000) {
                    poll.addField(question + "\nYou have 1000 seconds to answer!", answers, false);
                } else {
                    poll.addField(question + "\nYou have " + time + " seconds to answer!", answers, false);
                }
                poll.setColor(new Color(0x3228ed));
                poll.setFooter("Poll created by: " + event.getMember().getEffectiveName(), event.getMember().getUser().getAvatarUrl());
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessageEmbeds(poll.build()).queue();
                //Check that time does not surpass the max time limit
                if (time > 1000) {
                    time = 1000;
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage("Poll set to the max time of 1000 seconds.").queue();
                }
                gotPoll = true;
                pollOngoing = true;
                //Start timer for the poll
                new Timer().schedule(new PollTimer(event, event.getMember().getEffectiveName()), time * 1000);
            }
            catch (Exception e) {
                gotPoll = false;
                pollOngoing = false;
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("You did not type the command properly. Do ~help to view the full list of commands.\n~poll [Time(seconds)]|[Question]|[Option1]|[Option2]...|[Option10]").queue();
            }
        }
        //Send reaction numbers for poll
        if (gotPoll && Objects.requireNonNull(event.getMember()).getUser().equals(event.getJDA().getSelfUser())) {
            for (int i = 0; i < count; i++) {
                event.getMessage().addReaction(emojis[i]).queue();
            }
            gotPoll = false;
        }
        //If message has the stopCode and is from the code owner stop the bot
        if (userMessageArray[0].equalsIgnoreCase(HelloDiscord.prefix + stopCode) && event.getAuthor().getIdLong() == codeOwner && !stop) {
            stop = true;
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("Ok sorry to bother").queue();
        }

        //If message has the stopCode and is from the code owner resume bot
        if (userMessageArray[0].equalsIgnoreCase(HelloDiscord.prefix + goCode) && event.getAuthor().getIdLong() == codeOwner && stop) {
            stop = false;
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("Guess who's back? Back again.").queue();
        }
    }

    //Add user that uses pog for the first time to array
    private void setMemberInPog(Member member) {
        if (!member.getUser().isBot()) {
            HelloDiscord.pogCounts.put(member.getIdLong(), 0);
        }
    }
}

class PollTimer extends TimerTask {
    private MessageReceivedEvent event;
    private String username;

    public PollTimer(MessageReceivedEvent event, String username) {
        this.event = event;
        this.username = username;
    }

    @Override
    public void run() {
        ChatCommand.pollOngoing = false;
        int max = ChatCommand.pollCount[0];
        int index = 0;
        for (int i = 0; i < ChatCommand.pollCount.length; i++) {
            if (max < ChatCommand.pollCount[i]) {
                max = ChatCommand.pollCount[i];
                index = i;
            }
        }
        int tempCount = 0;
        for (int i = 0; i < ChatCommand.pollCount.length; i++) {
            if (ChatCommand.pollCount[i] == max) {
                tempCount++;
            }
        }
        if (tempCount > 1) {
            event.getChannel().sendMessage("The results are in! There was a tie in " + username + "'s poll. Try again.").queue();
        } else {
            event.getChannel().sendMessage("The results are in! **" + ChatCommand.pollChoices[index] + "** won **" + username + "**'s poll!").queue();
        }
    }
}
class RemindTimer extends TimerTask {
    private MessageReceivedEvent event;
    private String username;
    private String message;

    public RemindTimer(MessageReceivedEvent event, String username, String message) {
        this.event = event;
        this.username = username;
        this.message = message;
    }

    @Override
    public void run() {
        event.getChannel().sendMessage("<@" + username + "> do not forget: " + message).queue();
    }
}