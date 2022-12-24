package me.ericg;

import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;
import java.util.Arrays;

public class SpoggListener extends ListenerAdapter
    {
        @Override
        public void onMessageReceived(MessageReceivedEvent event)
        {
            if (event.getAuthor().isBot()) return;
            // We don't want to respond to other bot accounts, including ourselves
            Message message = event.getMessage();
            String content = message.getContentRaw();
            // getContentRaw() is an atomic getter
            // getContentDisplay() is a lazy getter which modifies the content for e.g. console view (strip discord formatting)
            String[] parsed = content.split(" "); //Using to get arguments for games and rolls
            dice_actions roller = new dice_actions();

            switch(parsed[0]) {
                case "!ping": {
                    MessageChannel channel = event.getChannel();
                    channel.sendMessage("Pong!").queue(); // Important to call .queue() on the RestAction returned by sendMessage(...)
                    break;
                }
                case "weh": {
                    MessageChannel channel = event.getChannel();
                    channel.sendMessage("weh").queue();
                    break;
                }
                case "!roll": {
                    MessageChannel channel = event.getChannel();

                    if (parsed.length == 2) { //for when a user asks for more dice
                        int[] diceCount = new int[2];    //create holder for numbers to send rollDice()
                        String[] howMany = parsed[1].split("d"); //
                        diceCount[0] = Integer.parseInt(howMany[0]); //how many dice
                        diceCount[1] = Integer.parseInt(howMany[1]); //how many sides

                        int[] multi_result =
                                roller.rollDice(diceCount[0], diceCount[1]);
                        channel.sendMessage(Arrays.toString(multi_result)).queue();
                        return;
                    }
                    int dice_result = roller.rollDice(6);

                    channel.sendMessage(Integer.toString(dice_result)).queue();
                    break;
                }
                case "!character": {
                    Storage cheese = new Storage();
                    String name = parsed[1] + "_" + parsed[2];
                    Scoundrel input = new Scoundrel();
                    Scoundrel output;

                    try {
                        output = cheese.readinfo(name, input);
                        MessageChannel channel = event.getChannel();
                        outputCharacter(output, channel);
                        //channel.sendMessage("cheese").queue();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
        }
    void outputCharacter(Scoundrel toRead, MessageChannel channel){

            String build = "```" +     //TODO: change to just a string call
            "Name: " + toRead.name + "\n" +
            "Alias: " + toRead.alias + "\n" +
            "Playbook: " + toRead.playbook + "\n" +
            "Crew: " + toRead.crew + "\n" +
            "Heritage: " + toRead.heritage + "\n" +
            "Background: " + toRead.background + "\n" +
            "Vice: " + toRead.vice + "\n" +
            "Special: " + toRead.special + "\n" +
            "Attune: " + toRead.attune + "\n" +
            "Command: " + toRead.command + "\n" +
            "Consort: " + toRead.consort + "\n" +
            "Finesse: " + toRead.finesse + "\n" +
            "Hunt: " + toRead.hunt + "\n" +
            "Prowl: " + toRead.prowl + "\n" +
            "Skirmish: " + toRead.skirmish + "\n" +
            "Study: " + toRead.study + "\n" +
            "Survey: " + toRead.survey + "\n" +
            "Sway: " + toRead.sway + "\n" +
            "Tinker: " + toRead.tinker + "\n" +
            "Wreck: " + toRead.wreck + "\n" +
            "```";

            channel.sendMessage(build).queue();

    }

    }