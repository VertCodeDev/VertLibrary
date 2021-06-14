/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.gui.page;

import dev.vertcode.vertlibrary.gui.entry.GUIEntry;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GUIPageBuilder {

    private final List<GUIEntry> entries = new ArrayList<>();

    private String title;
    private int rows;

    private boolean fillBorders = false;

    public GUIPageBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public GUIPageBuilder setRows(int rows) {
        this.rows = rows;
        return this;
    }

    public GUIPageBuilder addItem(GUIEntry entry) {
        entries.add(entry);
        return this;
    }

    public GUIPageBuilder fillBorders() {
        this.fillBorders = true;
        return this;
    }

    public GUIPage build() {
        GUIPage page = new GUIPage(this.title, this.rows) {
        };
        this.entries.forEach(page::addItem);

        return page;
    }
}
