import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Ball {
    public Color col = Color.WHITE;
    public float x,y,xold,yold;
    public float accx,accy;
    public static float dx = 6f;
    public static final float g = 0.3f;
    public static int precision = 10;
    public static float energyLoss = 0.7f;

    public Ball(float x,float y){
        this.x = x;
        this.y = y;
        xold = x;
        yold = y;
        accx = 0;
        accy = 0;
    }


    public void updatePos(float dt){
        float velx = x-xold;
        float vely = y-yold;
        xold = x;
        yold = y;
        x += velx+accx*dt*dt;
        y += vely+accy*dt*dt;
        accx = 0;
        accy = 0;
    }

    public void fixPos(){
        if (x+dx/2>Main.width){
            x = Main.width-dx/2;
        }
        if (x-dx/2<0){
            x = dx/2;
        }
        if (y+dx/2>Main.height){
            y = Main.height-dx/2;
        }
        if (y-dx/2<0){
            y = dx/2;
        }
    }

    public void applyGravity(){
        accy += g;
    }

    public void collisions(Chunk[][] all,ArrayList<Chunk> chunks){
        for (Chunk chunk : chunks){
            for (int i=0;i<chunk.balls.size();i++) {
                Ball ball = chunk.balls.get(i);
                if (ball != this && colliding(ball)) {
                    float dirx = x - ball.x;
                    float diry = y - ball.y;
                    float dist = (float) Math.sqrt(dirx * dirx + diry * diry);
                    dirx = dirx / dist;
                    diry = diry / dist;
                    x += 0.5f * dirx * (dx - dist)*energyLoss;
                    y += 0.5f * diry * (dx - dist)*energyLoss;
                    //fixPos();
                    ball.RemoveFromChunk(all);
                    ball.x -= 0.5f * dirx * (dx - dist)*energyLoss;
                    ball.y -= 0.5f * diry * (dx - dist)*energyLoss;
                    //ball.fixPos();
                    ball.AddToChunk(all);
                }
            }
        }
    }


    public void RemoveFromChunk(Chunk[][] all){
        int xindex = (int)(x/Main.chunkWidth);
        int yindex = (int)(y/Main.chunkHeight);
        if (yindex>=0 && yindex<all.length && xindex>=0 && xindex<all[0].length) {
            all[yindex][xindex].removeBall(this);
        }
    }
    public void AddToChunk(Chunk[][] all){
        int xindex = (int)(x/Main.chunkWidth);
        int yindex = (int)(y/Main.chunkHeight);
        if (yindex>=0 && yindex<all.length && xindex>=0 && xindex<all[0].length) {
            all[yindex][xindex].addBall(this);
        }
    }

    public boolean colliding(Ball other){
        return (other.y-y)*(other.y-y)+(other.x-x)*(other.x-x)<dx*dx;
    }

    public void draw(GraphicsContext gc){
        gc.setFill(col);
        gc.fillOval(x-dx/2,y-dx/2,dx,dx);
    }
}
