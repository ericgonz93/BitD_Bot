package me.ericg;

import java.util.Random;

public class dice_actions {
    private Random random = new Random();

    int rollDice(int sides){
        int result = random.nextInt(sides)+1;
        return result;
    }

}
