package com.example.miriyusifli.cardgame.services;

/**
 * Created by miriyusifli on 7/21/17.
 */

public class Service {

    public static Object[] mixArray(Object cards[]) {

        for (int i = 0; i < cards.length; i++) {
            int random = (int) (Math.random() * cards.length);
            Object c = cards[random];
            cards[random] = cards[i];
            cards[i] = c;

        }
        return  cards;


    }


    }
