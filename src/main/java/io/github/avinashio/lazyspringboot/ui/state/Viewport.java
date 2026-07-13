package io.github.avinashio.lazyspringboot.ui.state;

public class Viewport {

    private int offset;

    public int offset() {
        return offset;
    }

    public void update(
            int selectedIndex,
            int itemCount,
            int visibleHeight) {
        if (itemCount <= 0 || visibleHeight <= 0) {
            offset = 0;
            return;
        }

        if (selectedIndex < offset) {
            offset = selectedIndex;
        }

        int lastVisibleIndex = offset + visibleHeight - 1;

        if (selectedIndex > lastVisibleIndex) {
            offset = selectedIndex - visibleHeight + 1;
        }

        int maximumOffset =
                Math.max(0, itemCount - visibleHeight);

        offset = Math.min(offset, maximumOffset);
    }

    public void reset() {
        offset = 0;
    }
}