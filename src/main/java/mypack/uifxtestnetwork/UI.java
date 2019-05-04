package mypack.uifxtestnetwork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mypack.log.LoggingService;
import mypack.mconfig.ServerConfig;


// phần ui nên sử dung nio cho dễ cài đặt
public class UI extends Application {

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //MyConstants.initConstants();

        ServerConfig.initConfig();
        Parent root = FXMLLoader.load(this.getClass().getResource("/ui.fxml"));
        primaryStage.setTitle("Test network udp");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


        primaryStage.setOnCloseRequest(e -> {
            LoggingService.getInstance().getLogger().info("app closed");
        });
    }
}
