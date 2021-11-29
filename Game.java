package sample;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    enum Phase {
        register,
        preFlop,
        flop,
        turn,
        river,
        showDown;
    }


    int round_no = 0;
    public void registerPlayers (ArrayList<Player> players) throws Exception{
//        String name = System.in.re;
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String name = in.next();
        System.out.println(name);
    }
}
