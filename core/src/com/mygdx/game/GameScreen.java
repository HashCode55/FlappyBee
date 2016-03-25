package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;


import sprites.Flappee;
import sprites.Flower;


/**
 * Created by mehul on 3/25/16.
 */
public class GameScreen extends ScreenAdapter {

    //this shows how into how many units the screen will be divided.
    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 640;
    private static final float GAP_BET_FLOWERS = 200f;
    private ShapeRenderer shapeRenderer;
    private FitViewport viewPort;
    private Camera camera;
    private SpriteBatch spriteBatch;

    public Flappee flappyBee = new Flappee();
    private Array<Flower> flowers = new Array<Flower>();

    @Override
    public void show() {
        camera = new OrthographicCamera();
        //set the camera to the center of the world.
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewPort = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        flappyBee.setPosition(WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        //this is used to load the assets from the folder
        spriteBatch.setProjectionMatrix(camera.projection);
        spriteBatch.setTransformMatrix(camera.view);
        spriteBatch.begin();
        spriteBatch.end();

        update(delta);
        //this is used to render the shape
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        flappyBee.drawDebug(shapeRenderer);
        drawDebug();
        shapeRenderer.end();



    }


    public void update(float delta){
        flappyBee.update();
        updateFlowers(delta);
        //if(Gdx.input.justTouched()) flappyBee.flyUp();
        //handle the input from the user.
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) flappyBee.flyUp();
        blockFlappeeLeavingTheWorld();
    }

    public void updateFlowers(float delta){
        for(Flower flower : flowers){
            flower.update(delta);
        }
        checkIfNewFlowerIsNeeded();
        removeFlowers();
    }
    private void createNewFlower(){
        Flower flower = new Flower();
        flower.setPosition(WORLD_WIDTH + Flower.WIDTH);
        flowers.add(flower);
    }

    private void drawDebug(){
        for(Flower flower : flowers){
            flower.drawDebug(shapeRenderer);
        }
    }

    public void checkIfNewFlowerIsNeeded(){
        if(flowers.size == 0)
            createNewFlower();
        else{
            Flower flower = flowers.peek();
            if(flower.getX() < WORLD_WIDTH - GAP_BET_FLOWERS)
                createNewFlower();
        }
    }

    public void removeFlowers(){
        if(flowers.size > 0){
            Flower firstFlower = flowers.peek();
            if(firstFlower.getX() < -Flower.WIDTH){
                flowers.removeValue(firstFlower, true);
            }
        }
    }
    public void blockFlappeeLeavingTheWorld(){
        flappyBee.setPosition(flappyBee.getX(), MathUtils.clamp(flappyBee.getY(), 0, WORLD_HEIGHT));
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height);
    }

    private void clearScreen(){
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g,
                Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
