package me.ericg;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
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
                    MessageChannel channel = event.getChannel();
                    if(parsed.length != 3){channel.sendMessage("Invalid Name.").queue(); break;}

                    Storage cheese = new Storage();
                    String name = parsed[1] + "_" + parsed[2];
                    Scoundrel input = new Scoundrel();
                    Scoundrel output;

                    try {
                        output = cheese.readInfo(name, input);
                        outputCharacter(output, channel);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "!create": {
                    MessageChannel channel = event.getChannel();
                    if(parsed.length != 9){     //check if valid for createCharacter()
                        channel.sendMessage("Parameter order is firstname lastname playbook alias" +
                                " crew heritage background vice").queue();
                        break;
                    }

                    Storage cheese = new Storage();
                    String name = (parsed[1] + "_" + parsed[2]).toLowerCase();
                    String output;

                    try {
                        //separated output for cleaner look
                        output = cheese.createCharacter(name, parsed[3], parsed[4],
                                parsed[5], parsed[6], parsed[7], parsed[8]);
                        channel.sendMessage(output).queue();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "!update": {
                    MessageChannel channel = event.getChannel();
                    if(parsed.length != 5){     //check if valid for updateStat()
                        channel.sendMessage("Use update as !update [first name] [last name]" +
                                "[stat] [value]. " +
                                "Format special ability and trauma updates as follows \n" +
                                "```!update [first name] [last name] special [ability-1,ability-2]\n" +
                                "!update [first name] [last name] trauma [trauma-1,trauma-2].```").queue();
                        break;
                    }

                    Storage cheese = new Storage();
                    String name = (parsed[1] + "_" + parsed[2]).toLowerCase();
                    String output;

                    try {
                        //separated output for cleaner look
                        output = cheese.updateStat(name, parsed[3], parsed[4]);
                        channel.sendMessage(output).queue();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
        }
    void outputCharacter(Scoundrel toRead, MessageChannel channel){
            if(toRead.getName() == null){channel.sendMessage("Character not found.").queue(); return;}

            EmbedBuilder embed = new EmbedBuilder();

            embed.setTitle(toRead.getName());
            embed.setColor(Color.red);
            embed.addField("Alias", toRead.getAlias(),true);
            embed.addField("Playbook", toRead.getPlaybook(),true);
            embed.addField("Crew", toRead.getCrew(),true);
            embed.addField("Heritage", toRead.getHeritage(),true);
            embed.addField("Background", toRead.getBackground(),true);
            embed.addField("Vice", toRead.getVice(),true);
            embed.addField("Special Ability", toRead.getSpecial(),true);
            embed.addField("Attune", Integer.toString(toRead.getAttune()),true);
            embed.addField("Command", Integer.toString(toRead.getCommand()),true);
            embed.addField("Consort", Integer.toString(toRead.getConsort()),true);
            embed.addField("Finesse", Integer.toString(toRead.getFinesse()),true);
            embed.addField("Hunt", Integer.toString(toRead.getHunt()),true);
            embed.addField("Prowl", Integer.toString(toRead.getProwl()),true);
            embed.addField("Skirmish", Integer.toString(toRead.getSkirmish()),true);
            embed.addField("Study", Integer.toString(toRead.getStudy()),true);
            embed.addField("Survey", Integer.toString(toRead.getSurvey()),true);
            embed.addField("Sway", Integer.toString(toRead.getSway()),true);
            embed.addField("Tinker", Integer.toString(toRead.getTinker()),true);
            embed.addField("Wreck", Integer.toString(toRead.getWreck()),true);
            embed.addField("Coin", Integer.toString(toRead.getCoin ()),true);
            embed.addField("Stress", Integer.toString(toRead.getStress()),true);
            embed.addField("Trauma", toRead.getTrauma(),true);

        channel.sendMessageEmbeds(embed.build()).queue();
    }

    }