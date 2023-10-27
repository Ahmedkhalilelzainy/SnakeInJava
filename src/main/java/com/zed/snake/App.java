package com.zed.snake;

import com.zed.snake.Moves.Move;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

class Moves extends Thread {
  enum Move {
    UP, LEFT, RIGHT, DOWN
  }

  class TermSize {
    int columns = 0;
    int rows = 0;
  }

  private TermSize getTermSize() {
    TermSize term = new TermSize();
    try {
      Terminal terminal = TerminalBuilder.builder().system(true).build();
      term.columns = terminal.getSize().getColumns();
      term.rows = terminal.getSize().getRows();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return term;
  }

  public final static Move[] moves = { Move.UP, Move.DOWN, Move.LEFT,
      Move.RIGHT };

  Move getRandomMove() {
    int r = ThreadLocalRandom.current().nextInt(4);
    return moves[r];
  }

  private Move move;

  public void setMove(Move move) {
    this.move = move;
  }

  void moveCursor(int r, int c) {
    System.out.printf("\u001B[%d;%dH", r, c);
  }

  void redraw(int rows, int columns) {
    for (int i = 1; i <= columns; i++) {
      for (int j = 1; j <= rows; j++) {
        moveCursor(j, i);
        System.out.print(" ");
      }
    }
  }

  boolean wallHit(int r, int c, int rows, int columns) {
    if ((r <= 0 || r >= rows) || (c <= 0 || c >= columns)) {
      return true;
    }
    return false;
  }

  public void run() {
    TermSize term = getTermSize();
    int columns = term.columns;
    int rows = term.rows;
    int r = rows / 2;
    int c = columns / 2;
    move = getRandomMove();
    System.out.print("\u001B[2J");
    while (true) {
      redraw(rows, columns);

      switch (move) {
        case UP:
          r--;
          break;
        case DOWN:
          r++;
          break;
        case LEFT:
          c--;
          break;
        case RIGHT:
          c++;
          break;
        default:
          break;
      }
      if (wallHit(r, c, rows, columns)) {
        System.exit(1);
      }
      ;
      moveCursor(r, c);
      System.out.print("0");
      moveCursor(r, c - 1);
      System.out.print(" ");
    }
  }
}

class Reader extends Thread {

  private String move;

  public void run() {
    try (Terminal term = TerminalBuilder.terminal()) {
      term.enterRawMode();
      while (true) {
        move = Character.toString(term.reader().read());
        int r = term.getHeight();
        int c = term.getWidth();
        System.out.printf("\u001B[%d;%dH", r - 1, c - 1);
        System.out.print(" ");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getMove() {
    return move;
  }
}

public class App {

  void hideCursor() {
    System.out.print("\u001B[?25l");
  }

  void showCursor() {
    System.out.print("\u001B[?25h");
  }

  void play() {
    Reader read = new Reader();
    read.start();
    Moves moves = new Moves();
    moves.start();
    hideCursor();
    while (true) {
      String move = read.getMove();
      if (move != null) {
        switch (move) {
          case "w":
            moves.setMove(Move.UP);
            break;
          case "s":
            moves.setMove(Move.DOWN);
            break;
          case "d":
            moves.setMove(Move.RIGHT);
            break;
          case "a":
            moves.setMove(Move.LEFT);
            break;
          default:
            break;
        }
        move = null;
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      // System.out.printf("\u001B[%d;%dH", r, c);
    }
  }

  public static void main(String[] args)
      throws InterruptedException, IOException {
    App app = new App();
    app.play();
  };
}
