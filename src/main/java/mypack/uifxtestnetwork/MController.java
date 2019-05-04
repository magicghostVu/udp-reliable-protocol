package mypack.uifxtestnetwork;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import mypack.log.LoggingService;
import mypack.uifxtestnetwork.task.MyTaskTest;

import java.net.URL;
import java.util.ResourceBundle;

public class MController implements Initializable {


    @FXML
    private Label labelMsg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoggingService.getInstance().getLogger().info("MController init done!");


        MyTaskTest taskTest = new MyTaskTest(this);


        labelMsg.textProperty().bind(taskTest.messageProperty());
        new Thread(taskTest).start();

        Runnable r = () -> {
            try {
                for (int i = 0; i < 10; i++) {
                    taskTest.addJob();
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(r).start();


    }


    public Label getLabelMsg() {
        return labelMsg;
    }

    public void setLabelMsg(Label labelMsg) {
        this.labelMsg = labelMsg;
    }
}
