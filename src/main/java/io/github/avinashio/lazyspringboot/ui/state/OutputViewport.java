package io.github.avinashio.lazyspringboot.ui.state;

public class OutputViewport {

    private int offset;

    public int offset() {
        return offset;
    }

    public void scrollDown(
            int contentSize,
            int visibleHeight) {
        int maximumOffset =
                maximumOffset(
                        contentSize,
                        visibleHeight);

        if (offset < maximumOffset) {
            offset++;
        }
    }

    public void scrollUp() {
        if (offset > 0) {
            offset--;
        }
    }

    public void pageDown(
            int contentSize,
            int visibleHeight) {
        if (visibleHeight <= 0) {
            return;
        }

        int maximumOffset =
                maximumOffset(
                        contentSize,
                        visibleHeight);

        offset =
                Math.min(
                        maximumOffset,
                        offset + visibleHeight);
    }

    public void pageUp(
            int visibleHeight) {
        if (visibleHeight <= 0) {
            return;
        }

        offset =
                Math.max(
                        0,
                        offset - visibleHeight);
    }

    public void moveToTop() {
        offset = 0;
    }

    public void moveToBottom(
            int contentSize,
            int visibleHeight) {
        offset =
                maximumOffset(
                        contentSize,
                        visibleHeight);
    }

    public void reset() {
        offset = 0;
    }

    private int maximumOffset(
            int contentSize,
            int visibleHeight) {
        return Math.max(
                0,
                contentSize - visibleHeight);
    }
}