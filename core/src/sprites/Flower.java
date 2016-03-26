package sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by mehul on 3/25/16.
 */
public class Flower {

    private final Texture floorTexture;
    private final Texture ceilTexture;
    private static final float COLLISION_RECT_WIDTH = 13f;
    private static final float COLLISION_RECT_HEIGHT = 447f;
    private static final float COLLISION_CIRCLE_RADIUS = 33f;
    private static final float MAX_SPEED_PER_SECOND = 200F;
    public static final float WIDTH = COLLISION_CIRCLE_RADIUS * 2;
    private static final float HEIGHT_OFFSET = -400f;
    private static final float DISTANCE_BETWEEN_FLOOR_CEILING = 225F;
    private boolean pointClaimed = false;

    private final Circle floorCircleCollision;
    private final Rectangle floorCollisionRectangle;
    private final Circle ceilCircleCollision;
    private final Rectangle ceilCollisionRectangle;

    private float x;
    private float y;

    public Flower(Texture floorTexture, Texture ceilTexture){
        this.floorTexture = floorTexture;
        this.ceilTexture = ceilTexture;

        this.y = MathUtils.random(HEIGHT_OFFSET);
        this.floorCollisionRectangle = new Rectangle(x, y, COLLISION_RECT_WIDTH, COLLISION_RECT_HEIGHT);
        this.floorCircleCollision = new Circle(x + floorCollisionRectangle.width/2, y + floorCollisionRectangle.height, COLLISION_CIRCLE_RADIUS);

        //Ceiling initialisations
        this.ceilCollisionRectangle = new Rectangle(x, DISTANCE_BETWEEN_FLOOR_CEILING + floorCircleCollision.y , COLLISION_RECT_WIDTH, COLLISION_RECT_HEIGHT);
        this.ceilCircleCollision = new Circle(x + ceilCollisionRectangle.width / 2, ceilCollisionRectangle.y, COLLISION_CIRCLE_RADIUS);
    }

    public void draw(SpriteBatch spriteBatch){
        drawFloor(spriteBatch);
        drawCeil(spriteBatch);
    }

    public void drawFloor(SpriteBatch batch){
        float textureX = floorCircleCollision.x - floorTexture.getWidth() / 2;
        float textureY = floorCollisionRectangle.y + COLLISION_CIRCLE_RADIUS;
        batch.draw(floorTexture, textureX, textureY);
    }

    public void drawCeil(SpriteBatch batch){
        float textureX = ceilCircleCollision.x - ceilTexture.getWidth() / 2;
        float textureY = ceilCollisionRectangle.y - COLLISION_CIRCLE_RADIUS;
        batch.draw(ceilTexture, textureX, textureY);
    }

    public void setPosition(float x){
        this.x = x;
        updateCollisionCircle();
        updateCollisionRect();
    }

    private void updateCollisionCircle() {
        floorCircleCollision.setX(x + floorCollisionRectangle.width / 2);
        ceilCircleCollision.setX(x + ceilCollisionRectangle.width / 2);
    }

    private void updateCollisionRect(){
        floorCollisionRectangle.setX(x);
        ceilCollisionRectangle.setX(x);
    }

    public void update(float delta){
        setPosition(x - (MAX_SPEED_PER_SECOND * delta));
    }

    public void drawDebug(ShapeRenderer shapeRenderer){
        shapeRenderer.circle(floorCircleCollision.x, floorCircleCollision.y, floorCircleCollision.radius);
        shapeRenderer.rect(floorCollisionRectangle.x, floorCollisionRectangle.y, floorCollisionRectangle.width, floorCollisionRectangle.height);
        shapeRenderer.circle(ceilCircleCollision.x, ceilCircleCollision.y, ceilCircleCollision.radius);
        shapeRenderer.rect(ceilCollisionRectangle.x, ceilCollisionRectangle.y, ceilCollisionRectangle.width, ceilCollisionRectangle.height);
    }

    public boolean isFlappeeColliding(Flappee flappee){
        Circle flappeeCollisionCircle = flappee.getCollisionCircle();
        return Intersector.overlaps(flappeeCollisionCircle, ceilCircleCollision)
                || Intersector.overlaps(flappeeCollisionCircle, floorCircleCollision)
                || Intersector.overlaps(flappeeCollisionCircle, ceilCollisionRectangle)
                || Intersector.overlaps(flappeeCollisionCircle, floorCollisionRectangle);
    }

    public boolean isPointClaimed(){
        return pointClaimed;
    }

    public void markPointClaimed(){
        pointClaimed = true;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
