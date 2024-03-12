package org.example.task1;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class Game extends Thread {
    private final Object key = new Object();
    private Circle big_circle;
    private Circle small_circle;
    private Pane game_scene;
    public Button shoot_btn;
    private Label shoots_label;
    private Label score_label;
    public boolean isPaused;
    public int pos_small_Y;
    public int pos_big_Y;
    private int speed_big;
    private int speed_small;
    private int speed_bullet;
    private int score;
    private int shoots;
    private ArrayList<Circle> bullets;


    Game(Circle big_circle, Circle small_circle, Pane game_scene, Button shoot_btn, Label shoots_label, Label score_label){
        this.big_circle = big_circle;
        this.small_circle = small_circle;
        this.game_scene = game_scene;
        this.shoot_btn = shoot_btn;
        this.shoots_label = shoots_label;
        this.score_label = score_label;
        isPaused = false;
        pos_big_Y = (int)this.big_circle.getCenterY();
        pos_small_Y = (int)this.small_circle.getCenterY();
        speed_big = 1;
        speed_small = 2;
        speed_bullet = 5;
        score = 0;
        shoots = 0;
        shoot_btn.setOnMouseClicked(mouseEvent -> {
            shoots++;
            shoots_label.setText("SHOOTS: " + String.valueOf(shoots));
            Circle bullet = new Circle(75, 250, 5, Color.BLACK);
            bullets.add(bullet);
            game_scene.getChildren().add(bullet);
        });
        bullets = new ArrayList<>();
    }

    @Override
    public void run(){
        while (true) {
            synchronized (key) {

                while (isPaused) {
                    try {
                        key.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    sleep(30);
                    System.out.println("aaaaaaa");
                    Platform.runLater(() -> {
                        System.out.println("pos_small = " + String.valueOf(pos_small_Y) + " | pos_big = " + String.valueOf(pos_big_Y));
                        pos_big_Y += speed_big;
                        big_circle.setCenterY(pos_big_Y);
                        pos_small_Y += speed_small;
                        small_circle.setCenterY(pos_small_Y);
                        if (pos_big_Y >= game_scene.getPrefHeight() - big_circle.getRadius() || pos_big_Y <= big_circle.getRadius()){
                            speed_big *= -1;
                        }
                        if (pos_small_Y >= game_scene.getPrefHeight() - small_circle.getRadius() || pos_small_Y <= small_circle.getRadius()){
                            speed_small *= -1;
                        }
                        for (int i = bullets.size() - 1; i >= 0; i--) {
                            Circle bullet = bullets.get(i);
                            bullet.setCenterX(bullet.getCenterX() + 5);
                            if (bullet.intersects(big_circle.getBoundsInLocal())){
                                game_scene.getChildren().remove(bullet);
                                bullets.remove(bullet);
                                score++;
                                score_label.setText("SCORE: " + String.valueOf(score));
                            }
                            else if (bullet.intersects(small_circle.getBoundsInLocal())){
                                game_scene.getChildren().remove(bullet);
                                bullets.remove(bullet);
                                score += 2;
                                score_label.setText("SCORE: " + String.valueOf(score));
                            }
                            else if (bullet.getCenterX() >= 795){
                                game_scene.getChildren().remove(bullet);
                                bullets.remove(bullet);
                            }
                        };
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public void resumeThread() {
        synchronized (key) {
            key.notify();
        }
    }
    public void setDefault() {
        for (int i = bullets.size() - 1; i >= 0; i--){
            Circle bullet = bullets.get(i);
            game_scene.getChildren().remove(bullet);
            bullets.remove(bullet);
        }
        big_circle.setCenterY(250);
        small_circle.setCenterY(250);
        pos_big_Y = 250;
        pos_small_Y = 250;
        speed_big = 1;
        speed_small = 2;
        score = 0;
        shoots = 0;
        score_label.setText("SCORE: 0");
        shoots_label.setText("SHOOTS: 0");
    }
}
