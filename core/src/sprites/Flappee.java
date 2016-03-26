package sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;


/**
 * Created by mehul on 3/25/16.
 */
public class Flappee {
    private static final int TILE_WIDTH = 118;
    private static final int TILE_HEIGHT = 118;
    private static final float FRAME_DURATION = 0.25F;

    private static final float COLLISION_RADIUS = 24f;
    private final Circle collisionCircle;
    private static final float DIVE_ACCEL = 0.30f;
    private static final float FLY_ACCEL = 5f;
    private float ySpeed = 0;

    private float x;
    private float y;
    private TextureRegion flapeeTextRegion[][];
    private float animationTimer = 0;
    //private final TextureRegion flappeeTexture;

    private final Animation animation;

    public Flappee(Texture bee){
        flapeeTextRegion = new TextureRegion(bee).split(TILE_WIDTH, TILE_HEIGHT);
        animation = new Animation(FRAME_DURATION, flapeeTextRegion[0][0], flapeeTextRegion[0][1]);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        //flappeeTexture = flapeeTextRegion[0][0];
        collisionCircle = new Circle(x, y, COLLISION_RADIUS);
    }

    public void drawDebug(ShapeRenderer shapeRenderer){
        shapeRenderer.circle(collisionCircle.x, collisionCircle.y, collisionCircle.radius);
    }

    public void draw(SpriteBatch batch){
        TextureRegion flappeeTexture = animation.getKeyFrame(animationTimer);
        float textureX = collisionCircle.x - flappeeTexture.getRegionWidth() / 2;
        float textureY = collisionCircle.y - flappeeTexture.getRegionHeight() / 2;
        batch.draw(flappeeTexture, textureX, textureY);
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
        updateCollisionCircle();
    }

    private void updateCollisionCircle(){
        collisionCircle.setX(x);
        collisionCircle.setY(y);
    }

    public void update(float delta){
        animationTimer += delta;
        ySpeed -= DIVE_ACCEL;
        setPosition(x, y + ySpeed);
    }

    public void flyUp(){
        ySpeed = FLY_ACCEL;
        setPosition(x, y + ySpeed);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Circle getCollisionCircle(){
        return collisionCircle;
    }

    public float getRadius(){
        return COLLISION_RADIUS;
    }
}
