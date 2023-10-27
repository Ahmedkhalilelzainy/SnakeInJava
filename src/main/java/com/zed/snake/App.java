package com.zed.snake;

import java.io.IOException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class App {
  class TermSize {
    int width = 0;
    int height = 0;
  }

  private TermSize getTermSize() {
    TermSize term = new TermSize();
    try {
      Terminal terminal = TerminalBuilder.builder().system(true).build();
      term.width = terminal.getSize().getColumns();
      term.height = terminal.getSize().getRows();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return term;
  }

  void hideCursor() {
    System.out.print("\u001B[?25l");
  }

  void showCursor() {
    System.out.print("\u001B[?25h");
  }

  void moveCursor(int x, int y) {
    System.out.printf("\u001B[%d;%dH", x, y);
  }

  void clear() {
    System.out.print("\u001B[2J");
  }

  void redraw(int height, int width) {
    for (int i = 1; i <= height; i++) {
      for (int j = 1; j <= width; j++) {
        moveCursor(i, j);
        System.out.print(" ");
      }
    }
  }

  void play() throws InterruptedException {
    TermSize term = getTermSize();
    int width = term.width;
    int height = term.height;
    while (true) {
      hideCursor();
      redraw(height, width);
      int x = width / 2;
      int y = height / 2;
      // moveCursor(x, y);
      System.out.printf("\u001B[%d;%dH", x, y);
      System.out.print("0");
      Thread.sleep(1000);
    }
  }

  public static void main(String[] args) throws InterruptedException {
    App app = new App();
    app.play();
  };
}
