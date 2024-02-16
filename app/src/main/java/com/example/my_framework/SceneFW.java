package com.example.my_framework;

public abstract class SceneFW {
    private CoreFW coreFW;
    public int sceneWidth;
    public int sceneHeigth;
    public GraphicsFW graphicsFW;
    public SceneFW(CoreFW coreFW){
        this.coreFW=coreFW;
        sceneWidth=coreFW.getGraphicsFW().getWidthFrameBuffer();
        sceneHeigth=coreFW.getGraphicsFW().getWidthFrameBuffer();
        graphicsFW=coreFW.getGraphicsFW();
    }
    public abstract void update();
    public abstract void drawing();
    public abstract void pause();
    public abstract void resume();
    public abstract void dispose();
}
