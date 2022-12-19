package me.ericg;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;



public class BitD_Bot {
    /*private Dotenv config;

    public BitD_Bot(Dotenv config) {
        this.config = config;
    }*/

    public static void main(String[] args) throws LoginException, InterruptedException {
        //config= Dotenv.configure().load();
        key api =  new key();
        //Created a key object in another class to house bot token just to make this work. Will change later.
        JDA bot = JDABuilder.createDefault(api.getToken())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
                .setActivity(Activity.watching("you roll dice"))
                .addEventListeners(new SpoggListener())
                .build().awaitReady();
    }
}
