/*
 * VertCode Development  - Wesley Breukers
 *
 * [2019] - [2021] VertCode Development
 * All Rights Reserved.
 *
 * GUI UTILITIES MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.gui.entry;

@FunctionalInterface
public interface GUIEntryFunction<T, U, R> {

    R compile(T t, U u);
}
