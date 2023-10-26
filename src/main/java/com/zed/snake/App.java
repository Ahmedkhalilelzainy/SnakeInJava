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

  void play() {
    TermSize term = getTermSize();
    int width = term.width;
    int height = term.height;
  }

  public static void main(String[] args) {
    App app = new App();
    app.play();
  };
}
