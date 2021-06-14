/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.number;

import java.util.Arrays;

public class NumberUtils {

    private static NumberUtils instance;

    /**
     * @return The instance of this class
     */
    public static NumberUtils getInstance() {
        if (instance == null) instance = new NumberUtils();
        return instance;
    }

    /**
     * Increase a number by a certain %
     *
     * @param number  The number you want to increase
     * @param percent the percent you want to increase the number with
     * @return the increased number
     */
    public int increase(int number, double percent) {
        return (int) Math.round(number / 100 * (percent + 100));
    }

    /**
     * Increase a number by a certain %
     *
     * @param number  The number you want to increase
     * @param percent the percent you want to increase the number with
     * @return the increased number
     */
    public double increase(double number, double percent) {
        return number / 100 * (percent + 100);
    }

    /**
     * Calculates how many % the number is of the maximum
     *
     * @param number  The number you want to know of what % it is from maximum.
     * @param maximum The maximum of where you want to calculate the % of
     * @return The calculated %
     */
    public int percent(double number, double maximum) {
        return (int) (number / maximum * 100.0D);
    }

    /**
     * Calculates the average of a list with doubles.
     *
     * @param values The values you want to calculate the average from
     * @return the average of the values.
     */
    public double average(double... values) {
        double combined = Arrays.stream(values).sum();
        return combined / values.length;
    }
}
