package lunarlander;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage stage) {
    String javaVersion = System.getProperty("java.version");
    String javafxVersion = System.getProperty("javafx.version");
    Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
    Scene scene = new Scene(new StackPane(l), 640, 480);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {

    launch();

    Configuration conf = new Configuration();
    SomePrimitives ja = new SomePrimitives("Mateusz", 12, 3005);

    conf.toFile(ja);

    SomePrimitives obj2 = conf.fromFile("src/main/resources/lunarlander/configuration.json");
    System.out.println(obj2.getAge());
  }

}
