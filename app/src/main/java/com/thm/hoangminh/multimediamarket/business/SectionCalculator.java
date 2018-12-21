package com.thm.hoangminh.multimediamarket.business;

public class SectionCalculator {
    public static int calculateDominatedArcadeGamePoint(int buyCount, int ratingCount) {
        return buyCount + Math.round(ratingCount / 2);
    }

    public static int calculateHintedGamePoint(int views) {
        return views;
    }
}
