package me.ericg;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;

import java.awt.*;
import java.io.File;
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
                case "!map": {

                    MessageChannel channel = event.getChannel();
                    if(parsed.length != 2){channel.sendMessage("Invalid Input.").queue(); break;}
                    outputMap(channel,parsed[1].toLowerCase());
                    break;
                }

                case "!prowess": {
                    MessageChannel channel = event.getChannel();
                    if(parsed.length != 3){channel.sendMessage("Invalid Input.").queue(); break;}

                    Storage access = new Storage();
                    String name = parsed[1] + "_" + parsed[2];
                    Scoundrel input = new Scoundrel();
                    Scoundrel output;

                    try {
                        output = access.readInfo(name, input);
                        channel.sendMessage(output.getName() + "\'s Prowess is " + Integer.toString(output.getProwess())).queue();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }

                case "!insight": {
                    MessageChannel channel = event.getChannel();
                    if(parsed.length != 3){channel.sendMessage("Invalid Input.").queue(); break;}

                    Storage access = new Storage();
                    String name = parsed[1] + "_" + parsed[2];
                    Scoundrel input = new Scoundrel();
                    Scoundrel output;

                    try {
                        output = access.readInfo(name, input);
                        channel.sendMessage(output.getName() + "\'s Insight is " + Integer.toString(output.getInsight())).queue();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }


                case "!resolve": {
                    MessageChannel channel = event.getChannel();
                    if(parsed.length != 3){channel.sendMessage("Invalid Input.").queue(); break;}

                    Storage access = new Storage();
                    String name = parsed[1] + "_" + parsed[2];
                    Scoundrel input = new Scoundrel();
                    Scoundrel output;

                    try {
                        output = access.readInfo(name, input);
                        channel.sendMessage(output.getName() + "\'s Resolve is " + Integer.toString(output.getResolve())).queue();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "!character": {
                    MessageChannel channel = event.getChannel();
                    if(parsed.length != 3){channel.sendMessage("Invalid Name.").queue(); break;}

                    Storage access = new Storage();
                    String name = parsed[1] + "_" + parsed[2];
                    Scoundrel input = new Scoundrel();
                    Scoundrel output;

                    try {
                        output = access.readInfo(name, input);
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

                    Storage access = new Storage();
                    String name = (parsed[1] + "_" + parsed[2]).toLowerCase();
                    String output;

                    try {
                        //separated output for cleaner look
                        output = access.createCharacter(name, parsed[3], parsed[4],
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

                    Storage access = new Storage();
                    String name = (parsed[1] + "_" + parsed[2]).toLowerCase();
                    String output;

                    try {
                        //separated output for cleaner look
                        output = access.updateStat(name, parsed[3], parsed[4]);
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
            embed.addField("Stash", Integer.toString(toRead.getStash()),true);
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    void outputMap(MessageChannel channel,String mapName){
        //System.out.println(new File(".").getAbsolutePath());
        String filePath = "C:\\Users\\ericg\\Documents\\Java Projects\\BitD Discord Bot\\src\\main\\java\\assets\\";
        String mapTitle = "default";
        String description = "A place.";
        String landmarks = "a note";
        int wealth = 0;
        int security = 0;
        int criminal = 0;
        int occult = 0;
        String bonuses = "stuff";

        switch (mapName) {
            case "barrowcleft": {
                filePath = filePath + "doskvol_barrowcleft.jpg";
                mapTitle = "Barrowcleft";
                description = "Home to the laborers and overseers of the Ministry of Preservation who attend the radiant energy farms of Doskvol. It is a dusty, rural district, with simple wooden buildings of only one or two stories and wide dirt roads to accommodate large cargo wagons. The farmers of Barrowcleft are organized into tight-knit family-based clans that are proud of their vital role in the city’s welfare and hold themselves apart from the \"city folk\" across the river. Outsiders are welcome here for honest trade, but are met with a cold suspicion otherwise.";
                landmarks = "Barrow Bridge. Lightning Tower. Barrowcleft Market. Radiant Energy Farm.";
                wealth = 1;
                security = 3;
                criminal = 0;
                occult = 0;
                bonuses = "Barrowcleft market is one of the best marketplaces in the city, but criminal types draw lots of unwanted attention. You can take +1d to acquire an asset here, but also accrue +2 HEAT";
                break;
            }
            case "brightstone": {
                filePath = filePath + "doskvol_brightstone.jpg";
                mapTitle = "Brightstone";
                description = "Home to many of the wealthiest and most influential citizens of Doskvol. Its streets are broad and paved, under bright electric lights; its canals are sparkling and clean, with perfumed water; its houses are all of fine, pale marble blocks, rich timbers, and intricate ironworks. There are cultivated parks fed by radiant energy; lavish restaurants and cafes; jewelers, tailors, and other luxury shops. Street-side vendors are forbidden here, resulting in a serene, spacious atmosphere, punctuated by the occasional carriage or marching Bluecoat patrol.";
                landmarks = "Unity Park. Silver Market. The Sanctorium. Bowmore Bridge.";
                wealth = 4;
                security = 4;
                criminal = 0;
                occult = 2;
                bonuses = "Most engagement rolls suffer -1d due to heavy Bluecoat patrols. Operations against the nobility in Brightstone are considered on \"hostile turf\" for the purpose of generating HEAT.";
                break;
            }
            case "charhollow": {
                filePath = filePath + "doskvol_charhollow.jpg";
                mapTitle = "Charhollow";
                description = "This crowded district is home to the bulk of the workforce of the city—servants, dockers, sailors, stockyard and eelery workers, cabbies, and so on. It’s cheap, noisy, cramped, and sweltering from cookfires and hissing steam-pipes, but there’s a familial camaraderie among its residents that you won’t find anywhere else. The people of Charhollow are a true community, brought together by circumstance, but bound by ties of mutual support and care in stark contrast to the cutthroat ruthlessness that constitutes business as usual in the rest of the city.";
                landmarks = "The Sheets. Strangford House. Charhollow Market. Kellen's";
                wealth = 0;
                security = 2;
                criminal = 1;
                occult = 0;
                bonuses = "Operations against the citizenry in Charhollow are considered on \"hostile turf\" for the purpose of generating HEAT.";
                break;
            }
            case "charterhall": {
                filePath = filePath + "doskvol_charterhall.jpg";
                mapTitle = "Charterhall";
                description = "The site of the first major construction in the city, in the days before the cataclysm. The old wall upon which was built the first lightning barrier in the Empire still stands in partial ruin around the district. The area is now home to the civic offices of the government including the courts, licensing and taxation offices, banks, and records archives. City officials and students at Charterhall University live here, along with the captains of Imperial industry who prefer to reside within sight of their fortunes.";
                landmarks = "Charter Wall. Bellweather Crematorium. Clerk Street. Jayan Park. Charterhall University.";
                wealth = 4;
                security = 4;
                criminal = 0;
                occult = 0;
                bonuses = "The records in Charterhall can be of particular interest to ccriminal sorts. Take a Devil's Bargain for +1d. to gather info her in exchange for 1 HEAT (the Bluecoats are always watching for scoundrels like you).";
                break;
            }
            case "coalridge": {
                filePath = filePath + "doskvol_coalridge.jpg";
                mapTitle = "Coalridge ";
                description = "Home to most of the machinists, industrial laborers, and factories of the city. It’s cramped, soot-choked, and loud— spewing dense clouds of black smoke, showers of sparks, and burning cinders. The old elevated train lines that once hauled coal now carry heavy equipment and raw materials to and from Gaddoc Station, though many of the ancient tracks and cars have been abandoned to squatters who’ve converted them into makeshift homes.";
                landmarks = "Coalridge Mine. The Old Rail Yard. The Ironworks. Brickston.";
                wealth = 1;
                security = 1;
                criminal = 1;
                occult = 0;
                bonuses = "Because the factories of Coalridge operate around the clock, there's no ideal time for clandestine crime here, but foremen are happy to be bribed to \"take a break\" or look the other way.";
                break;
            }
            case "crowsfoot": {
                filePath = filePath + "doskvol_crowsfoot.jpg";
                mapTitle = "Crow's Foot";
                description = "This district is a crossroads, merging many qualities of its neighboring districts: the illict vices of Silkshore, the labor and trade of the Docks, the poverty of Charhollow, and the classic architecture of Charterhall. The district is a patchwork, both held together and threatened to be torn apart by the menagerie of competing street gangs and Bluecoat squads that claim every avenue and corner as territory in an endless turf war.";
                landmarks = "Crow's Nest. Tangletown. Strathmill House. Red Sash Sword Academy.";
                wealth = 2;
                security = 1;
                criminal = 4;
                occult = 2;
                bonuses = "Years of murder have made this the most haunted district. Angry ghosts crave bloodshed here. You may take a Devil's Bargain for +1d for violent action, but the ghost will lash out, too.";
                break;
            }
            case "dunslough": {
                filePath = filePath + "doskvol_dunslough.jpg";
                mapTitle = "Dunslough";
                description = "A ghetto for the destitute poor of the city, as well as the site of Ironhook Prison and its labor camp. Originally, the ghetto was a neighborhood for families of prisoners, but over the years, extreme poverty and neglect have worn it down into a sodden ruin. A vicious cycle plays out here: crime driven by desperation, then arrest, incarceration, and release back to Dunslough — giving Ironhook an endless supply of laborers to exploit.";
                landmarks = "Ironhook Prison. Dunvil Labor Camp. Dunslough Ghetto. The Mire.";
                wealth = 0;
                security = 0;
                criminal = 2;
                occult = 1;
                bonuses = "None";
                break;
            }
            case "lostdistrict": {
                filePath = filePath + "doskvol_lostdistrict.jpg";
                mapTitle = "Lost District";
                description = "A once wealthy area, ravaged by plague then abandoned to the deathlands when the second lightning barrier was built. To the northeast of the city. Contains many lost treasures for the foolhardy to seek out.";
                landmarks = "Barrow Bridge. Lightning Tower. Barrowcleft Market. Radiant Energy Farm";
                wealth = 0;
                security = 0;
                criminal = 0;
                occult = 0;
                bonuses = "None";
                break;
            }
            case "nightmarket": {
                filePath = filePath + "doskvol_nightmarket.jpg";
                mapTitle = "Night Market";
                description = "A district dominated by commerce. Situated near Gaddoc Rail Station, Nightmarket receives the bulk of salable goods from the cargo trains that travel across the Imperium, bringing the exotic and rare to Duskwall. The citizens that call Nightmarket home constitute a new class of \"elites\" — wealthy people who are not of noble descent but nevertheless claim land, status, and power without titles. The district has been taken over by new construction, introducing lavish private townhouses with all of the modern advances for the elites that can afford them.";
                landmarks = "The Veil. Dundridge and Sons. Vreen's Hound Races. The Devil's Tooth.";
                wealth = 3;
                security = 3;
                criminal = 2;
                occult = 1;
                bonuses = "Nightmarket is the best place to trade illicit and arcane goods in the city, but the darker corners are full of strange horrors. You can take +1d to acquire an asset here, at the cost of 2 stress.";
                break;
            }
            case "silkshore": {
                filePath = filePath + "doskvol_silkshore.jpg";
                mapTitle = "Silkshore";
                description = "Criss-crossed by dozens of narrow canals, Silkshore is a district best navigated by gondola, as most visitors to this \"red lamp district\" do. The brothels, vice dens, food stalls, and exotic shops all perch at the waterside, ready to satisfy the appetites of their clientele, no questions asked. Silkshore is a place of public indulgence and private indelicacy, catering to every pleasure imaginable — as well as some that strain the bounds of fantasy.";
                landmarks = "The Spark Grounds. The Ease. Fogcrest. Ankhayat Park";
                wealth = 2;
                security = 2;
                criminal = 3;
                occult = 1;
                bonuses = "Should you overindulge your vice while in Silkshore, you'll get a taste for it. Take +1d to your roll the next time you indulge your vice here.";
                break;
            }
            case "sixtowers": {
                filePath = filePath + "doskvol_sixtowers.jpg";
                mapTitle = "Six Towers";
                description = "A formerly prestigious district that has faded over the centuries into a pale shadow of what it once was. The eponymous six towers were originally the grand residences of Doskvol’s first noble families. All but two (Bowmore House and Rowan House) have been sold off and converted into cheap apartments or fallen into ruin and abandoned. The district has an empty, haunted feel, with many sprawling old buildings dark without power, broad stone streets cracked and buckled, and the fires of squatters crackling from overgrown lots.";
                landmarks = "Rowan House. Mistshore Park. Scurlock Manor. Arms of the Weeping Lady.";
                wealth = 2;
                security = 1;
                criminal = 2;
                occult = 3;
                bonuses = "The many empty buildings and abandoned properties make this district a perfect location for a scoundrel's lair.";
                break;
            }
            case "thedocks": {
                filePath = filePath + "doskvol_thedocks.jpg";
                mapTitle = "The Docks";
                description = "The docks of Doskvol are ancient, going back to the days before the cataclysm, when the area was a colony town of the old Skov kingdom. Today, some commerce has shifted to the new electro-rail lines of the Imperium, but the docks are still bustling with cargo haulers, fishing boats, and the prestigious leviathan hunter ships that provide the raw material that keeps the city running.";
                landmarks = "The North Hook Company. Ink Lane. Saltford's. The Menagerie.";
                wealth = 2;
                security = 2;
                criminal = 2;
                occult = 2;
                bonuses = "Operations against ships at port are considered \"hostile turf\" for the purpose of generating HEAT.";
                break;
            }
            case "whitecrown": {
                filePath = filePath + "doskvol_whitecrown.jpg";
                mapTitle = "White Crown";
                description = "This district sits atop a grand peak on the island across North Hook channel from the city proper. From this lofty height, the Lord Governor’s stronghold oversees all, flanked by the grand estates of the most powerful nobility and the extravagantly appointed campus of Doskvol Academy. Whitecrown is a rich and rarefied world unto itself — most citizens live out their entire lives in the city without ever once crossing the bridge to the glittering spires of wealth and power there.";
                landmarks = "Lord Governor's Stronghold. Doskvol Academy. Master Warden's Estate. North Hook Lighthouse.";
                wealth = 4;
                security = 4;
                criminal = 0;
                occult = 2;
                bonuses = "Most engagement rolls suffer -2d due to heavy Bluecoat Patrols. Operations against the nobility in Whitecrown are considered on \"hostile turf\" for the purpose of generating HEAT.";
                break;
            }

        }


        File image = new File(filePath);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(mapTitle);
        embed.setColor(Color.blue);
        embed.setImage("attachment://map.jpg");
        embed.setDescription(description);
        embed.addField("Landmarks", landmarks,false);
        embed.addField("Wealth", Integer.toString(wealth), true);
        embed.addField("Security and Safety", Integer.toString(security), true);
        embed.addField("Criminal Influence", Integer.toString(criminal), true);
        embed.addField("Occult Influence", Integer.toString(occult), true);
        embed.addField("Trait", bonuses, false);

        channel.sendMessageEmbeds(embed.build()).addFiles(FileUpload.fromData(image, "map.jpg")).queue();
    }

    }