package mypack.uifxtestnetwork;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mypack.log.LoggingService;

import java.net.URL;
import java.util.ResourceBundle;

public class MController implements Initializable {


    @FXML
    private TextField textHost;

    @FXML
    private TextField textPort;

    @FXML
    private Button btnInitConnect;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoggingService.getInstance().getLogger().info("MController init done!");
    }


    // run on main thread
    public void initConnect(Event e) {
        LoggingService.getInstance().getLogger().info("init connect clicked");
        btnInitConnect.setDisable(true);
    }

}
