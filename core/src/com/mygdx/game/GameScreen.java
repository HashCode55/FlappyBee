package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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

    private Texture background;
    private Texture floorTexture;
    private Texture ceilTexture;
    private Texture flappeeBee;
    //this shows how into how many units the screen will be divided.
    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 640;
    private static final float GAP_BET_FLOWERS = 200f;
    private int score = 0;
    private ShapeRenderer shapeRenderer;
    private FitViewport viewPort;
    private Camera camera;
    private SpriteBatch spriteBatch;

    public Flappee flappyBee;
    private Array<Flower> flowers = new Array<Flower>();

    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;


    @Override
    public void show() {
        background = new Texture("bg.png");
        ceilTexture = new Texture("flowerTop.png");
        floorTexture = new Texture("flowerBottom.png");
        flappeeBee = new Texture("bee.png");
        camera = new OrthographicCamera();

        flappyBee = new Flappee(flappeeBee);
        //set the camera to the center of the world.
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewPort = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        bitmapFont = new BitmapFont();
        glyphLayout = new GlyphLayout();
        flappyBee.setPosition(WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        //this is used to load the assets from the folder
        spriteBatch.setProjectionMatrix(camera.projection);
        spriteBatch.setTransformMatrix(camera.view);
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        drawScore();
        drawFlowers();
        flappyBee.draw(spriteBatch);
        spriteBatch.end();

        update(delta);
        //this is used to render the shape
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        //flappyBee.drawDebug(shapeRenderer);
        //drawDebug();
        shapeRenderer.end();



    }


    public void update(float delta){
        flappyBee.update(delta);
        updateFlowers(delta);
        updateScore();
        if(checkCollisionCircle() || checkGroundTouch()){
            restartEngine();
        }
        //if(Gdx.input.justTouched()) flappyBee.flyUp();
        //handle the input from the user.
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) flappyBee.flyUp();
        blockFlappeeLeavingTheWorld();
    }

    public boolean checkGroundTouch(){
        if(flappyBee.getY() < flappyBee.getRadius())
            return true;
        return false;
    }

    public void updateFlowers(float delta){
        for(Flower flower : flowers){
            flower.update(delta);
        }
        checkIfNewFlowerIsNeeded();
        removeFlowers();
    }

    private void updateScore(){
        Flower flower = flowers.first();
        //System.out.print(flower.isPointClaimed());
        if(flappyBee.getX() > flower.getX() && !flower.isPointClaimed()) {
            System.out.println(score);
            flower.markPointClaimed();
            score++;
        }

    }
    private void createNewFlower(){
        Flower flower = new Flower(floorTexture, ceilTexture);
        flower.setPosition(WORLD_WIDTH + Flower.WIDTH);
        flowers.add(flower);
    }

    private void drawDebug(){
        for(Flower flower : flowers){
            flower.drawDebug(shapeRenderer);
        }
    }

    private void drawFlowers(){
        for(Flower flower : flowers){
            flower.draw(spriteBatch);
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
            Flower firstFlower = flowers.first();
            if(firstFlower.getX() < -Flower.WIDTH){
                //System.out.println("REMOVED");
                flowers.removeValue(firstFlower, true);
            }
        }
    }
    public void blockFlappeeLeavingTheWorld(){
        flappyBee.setPosition(flappyBee.getX(), MathUtils.clamp(flappyBee.getY(), 0, WORLD_HEIGHT));
    }

    public boolean checkCollisionCircle(){
        for(Flower flower : flowers){
            if(flower.isFlappeeColliding(flappyBee)){
                return true;
            }
        }
        return false;
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

    private void drawScore(){
        String scoreString = Integer.toString(score);
        glyphLayout.setText(bitmapFont, scoreString);
        bitmapFont.draw(spriteBatch, scoreString, ((viewPort.getWorldWidth() - glyphLayout.width) / 2), (4 * viewPort.getWorldHeight() / 5)
                - glyphLayout.height / 2);
    }
    private void restartEngine(){
        flappyBee.setPosition(WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
        flowers.clear();
        score = 0;
    }
}
