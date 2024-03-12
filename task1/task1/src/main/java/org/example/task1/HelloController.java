package org.example.task1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class HelloController {
    @FXML
    private Button start_btn;
    @FXML
    private Button pause_btn;
    @FXML
    private Button shoot_btn;
    @FXML
    private Label score_label;
    @FXML
    private Label shoots_label;
    @FXML
    private Pane game_scene;
    @FXML
    private Circle big_circle;
    @FXML
    private Circle small_circle;
    Game game;
    private boolean isStarted;


    public void scene_ini(){
        //upd_label_shoots();
        shoot_btn.setDisable(true);
        pause_btn.setDisable(true);
        pause_btn.setOnMouseClicked(mouseEvent -> {
            game.isPaused = !game.isPaused;
            if (game.isPaused){
                shoot_btn.setDisable(true);
                pause_btn.setText("Resume");
                game.resumeThread();
            }
            else {
                shoot_btn.setDisable(false);
                pause_btn.setText("Pause");
                game.resumeThread();
            }
        });
        game = new Game(big_circle, small_circle, game_scene, shoot_btn, shoots_label, score_label);
        start_btn.setOnMouseClicked(mouseEvent -> {
            if (!isStarted) {
                start_btn.setText("New Game");
                isStarted = true;
                game.start();
            }
            else{
                if (game.isPaused){
                    game.isPaused = false;
                    game.resumeThread();
                }
                pause_btn.setText("Pause");
                game.setDefault();
            }
            shoot_btn.setDisable(false);
            pause_btn.setDisable(false);
        });
    }

    /*private void upd_label_shoots(){
        shoots_label.setText("SHOOTS: " + String.valueOf(shoots_count));
    }*/
}