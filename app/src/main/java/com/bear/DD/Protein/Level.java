package com.bear.DD.Protein;

public class Level {
    long number=1;
    double HPgrowthrate=0.3;
    double ResistanceEffect=1.0;
    double SaveDamageBonus=0;
    int BasicDamage=10;
    int BasicDamageIncrease=1;
    int ChessboardRows=3;
    int CHessboardCols=3;
    int maxturns=10;
    public Level(long number) {
        this.number = number;
    }

    public Level() {
    }
}
