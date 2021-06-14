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
import java.util.Optional;

public enum RomanNumber {

    M(1000),
    CM(900),
    D(500),
    CD(400),
    C(100),
    XC(90),
    L(50),
    XL(40),
    X(10),
    IX(9),
    V(5),
    IV(4),
    I(1);

    private final int amount;

    RomanNumber(int amount) {
        this.amount = amount;
    }

    /**
     * Get a roman number by a {@link String}
     *
     * @param str The string you want to get the roman number by
     * @return the {@link RomanNumber}
     */
    public static Optional<RomanNumber> fromString(String str) {
        return Arrays.stream(values()).filter(romanNumber ->
                romanNumber.name().equalsIgnoreCase(str)).findFirst();
    }

    /**
     * Get a roman number by a {@link Integer}
     *
     * @param integer The integer you want to get the roman number by
     * @return the {@link RomanNumber}
     */
    public static Optional<RomanNumber> fromInteger(int integer) {
        return Arrays.stream(values()).filter(romanNumber ->
                romanNumber.getAmount() == integer).findFirst();
    }

    public int getAmount() {
        return amount;
    }

}
