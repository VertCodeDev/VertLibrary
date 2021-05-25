


/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.object;

import lombok.Getter;

@Getter
public class Pair<T, E> {

    protected T first;
    protected E second;

    public Pair(T first, E second) {
        this.first = first;
        this.second = second;
    }

    public Pair<T, E> setFirst(T first) {
        this.first = first;
        return this;
    }

    public Pair<T, E> setSecond(E second) {
        this.second = second;
        return this;
    }
}
