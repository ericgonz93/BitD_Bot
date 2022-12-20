package me.ericg;

import java.util.Random;

public class dice_actions {
    private Random random = new Random();

    int rollDice(int sides){
        int result = random.nextInt(sides)+1;
        return result;
    }
    int[] rollDice(int amt, int sides){
        int[] results = new int[amt];

        for(int i = 0; i<amt; i++){
            results[i] = random.nextInt(sides)+1;
        }
        return results;
    }

}
