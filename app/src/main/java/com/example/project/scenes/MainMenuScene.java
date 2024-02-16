package com.example.project.scenes;

import android.graphics.Color;

import com.example.my_framework.CoreFW;
import com.example.my_framework.SceneFW;

public class MainMenuScene extends SceneFW {
    public MainMenuScene(CoreFW coreFW){
        super(coreFW);
    }

    @Override
    public void update() {

    }

    @Override
    public void drawing() {
        graphicsFW.clearScene(Color.GREEN);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
