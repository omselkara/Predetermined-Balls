import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class Chunk {
    ArrayList<Ball> balls;
    float width,height;
    float x,y;
    float posx,posy;
    Chunk(float posx,float posy,float width,float height){
        balls = new ArrayList<>();
        this.posx = posx;
        this.posy = posy;
        this.width = width;
        this.height = height;
        x = (posx*width);
        y = (posy*height);
    }


    public float GetScreenX(float posx){
        return posx*width;
    }

    public float GetScreenY(float posy){
        return posy*height;
    }
    public boolean CanIntersect(Ball ball){
        if (ball.x>=x && ball.x<=x+width && ball.y>=y && ball.y<=y+height) return true;
        if (ball.x<x){
            if (ball.y>=y && ball.y<=y+height){
                return (x-ball.x)<=Ball.dx/2;
            }
            return (ball.x-x)*(ball.x-x)+(ball.y-y)*(ball.y-y)<=Ball.dx*Ball.dx/4;
        }
        else if (ball.x>=x && ball.x<=x+width){
            if (ball.y<y){
                return y-ball.y<Ball.dx/2;
            }
            return ball.y-(y+height)<Ball.dx/2;
        }
        else{
            if (ball.y>=y && ball.y<=y+height){
                return (x-ball.x)<=Ball.dx/2;
            }
            return (ball.x-x)*(ball.x-x)+(ball.y-y)*(ball.y-y)<=Ball.dx*Ball.dx/4;
        }
    }


    void draw(GraphicsContext gc){
        gc.setFill(Color.WHEAT);
        gc.fillRect(x,y,width,height);
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x,y,width,height);
        gc.setFill(Color.BLUE);
        gc.fillText(""+balls.size(),x+width/2,y+height/2);
    }

    public void addBall(Ball i){
        balls.add(i);
    }

    public void removeBall(Ball i){
        balls.remove(i);
    }

}
