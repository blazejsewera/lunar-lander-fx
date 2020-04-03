package lunarlander;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public class ApplicationWindow {

  public ApplicationWindow() {
    SidePane sidePane = new SidePane();

    this.mainGameScene = new Scene(sidePane.getSideBorderPane());

    this.mainGameScene.getStylesheets().add("lunarlander/css/style.css");

    final KeyCombination ctrlQuit = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
    this.mainGameScene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
      if (ctrlQuit.match(event)) {
        Platform.exit();
      }
    });

    this.mainGameScene.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.UP) sidePane.setKbdUpPressed();
      if (event.getCode() == KeyCode.LEFT) sidePane.setKbdLeftPressed();
      if (event.getCode() == KeyCode.RIGHT) sidePane.setKbdRightPressed();
      // Only for presentation purposes
      if (event.getCode() == KeyCode.L) sidePane.setLevel(5);
    });
    this.mainGameScene.setOnKeyReleased(event -> {
      if (event.getCode() == KeyCode.UP) sidePane.setKbdUpReleased();
      if (event.getCode() == KeyCode.LEFT) sidePane.setKbdLeftReleased();
      if (event.getCode() == KeyCode.RIGHT) sidePane.setKbdRightReleased();
      // Only for presentation purposes
      if (event.getCode() == KeyCode.L) sidePane.setLevel(2);
    });
  }

  public Scene getMainGameScene() {
    return mainGameScene;
  }

  private final Scene mainGameScene;
}