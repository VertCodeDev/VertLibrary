/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.ascii;

import com.github.lalyos.jfiglet.FigletFont;
import lombok.SneakyThrows;

public class AsciGen {

    private static AsciGen instance;

    /**
     * @return the instance of this class
     */
    public static AsciGen getInstance() {
        if (instance == null) instance = new AsciGen();
        return instance;
    }

    /**
     * Converts a {@link String} into a ASCII Letter String.
     *
     * @param str The {@link String} you want to convert to ASCII Letters
     * @return Converted {@link String}
     */
    @SneakyThrows
    public String generateAscii(String str) {
        return FigletFont.convertOneLine(this.getClass().getClassLoader().getResourceAsStream("Graffiti.flf"), str);
    }

}
