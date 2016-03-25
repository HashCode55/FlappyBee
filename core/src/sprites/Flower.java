package sprites;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by mehul on 3/25/16.
 */
public class Flower {
    private static final float COLLISION_RECT_WIDTH = 13f;
    private static final float COLLISION_RECT_HEIGHT = 447f;
    private static final float COLLISION_CIRCLE_RADIUS = 33f;
    private static final float MAX_SPEED_PER_SECOND = 200F;
    public static final float WIDTH = COLLISION_CIRCLE_RADIUS * 2;
    private static final float HEIGHT_OFFSET = -400f;

    private final Circle circleCollision;
    private final Rectangle collisionRectangle;

    private float x;
    private float y;

    public Flower(){
        this.y = MathUtils.random(HEIGHT_OFFSET);
        this.collisionRectangle = new Rectangle(x, y, COLLISION_RECT_WIDTH, COLLISION_RECT_HEIGHT);
        this.circleCollision = new Circle(x + collisionRectangle.width/2, y + collisionRectangle.height, COLLISION_CIRCLE_RADIUS);
        //System.out.println(collisionRectangle.y);
    }

    public void setPosition(float x){
        this.x = x;
        updateCollisionCircle();
        updateCollisionRect();
    }

    private void updateCollisionCircle() {
        circleCollision.setX(x + collisionRectangle.width / 2);
    }

    private void updateCollisionRect(){
        collisionRectangle.setX(x);
    }

    public void update(float delta){
        setPosition(x - (MAX_SPEED_PER_SECOND * delta));
    }

    public void drawDebug(ShapeRenderer shapeRenderer){
        shapeRenderer.circle(circleCollision.x, circleCollision.y, circleCollision.radius);
        shapeRenderer.rect(collisionRectangle.x, collisionRectangle.y, collisionRectangle.width, collisionRectangle.height);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
