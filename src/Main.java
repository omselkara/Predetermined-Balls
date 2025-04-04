import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    public static float width = 600f;
    public static float height = 600f;

    int frame = 0;
    GraphicsContext gc;

    public static int col = 150,row = 150;
    public static float chunkWidth =  width/col;
    public static float chunkHeight =  height/row;

    public Chunk[][] chunks;
    ArrayList<Ball> balls;

    int count = 12000;
    Random random = new Random(4070);

    double mouseX = -1;
    double mouseY = -1;

    BufferedImage img;
    FastRGB pixels;

    boolean load = true;
    boolean save = false;

    boolean render = true;
    long old = -1;
    float fps = -1;
    String[] loaded;

    public Chunk getChunk(double x,double y){
        int xindex = (int)(x/chunkWidth);
        int yindex = (int)(y/chunkHeight);
        if (xindex>=0 && xindex<col && yindex>=0 && yindex<row){
            return chunks[yindex][xindex];
        }
        return null;
    }
    public ArrayList<Chunk> GetCloseChunks(double x,double y){
        int xindex = (int)(x/chunkWidth);
        int yindex = (int)(y/chunkHeight);
        ArrayList<Chunk> close = new ArrayList<>();
        if (xindex>=0 && xindex<col && yindex>=0 && yindex<row){
            close.add(chunks[yindex][xindex]);
            if (xindex>0){
                float leftBorder = xindex*chunkWidth;
                if (x<=leftBorder+Ball.dx/2f){
                    close.add(chunks[yindex][xindex-1]);
                    if (yindex>0){
                        float topBorder = yindex*chunkHeight;
                        if (y<=topBorder+Ball.dx/2f) {
                            close.add(chunks[yindex-1][xindex-1]);
                        }
                    }
                    if (yindex+1<row){
                        float bottomBorder = (yindex+1)*chunkHeight;
                        if (y>=bottomBorder-Ball.dx/2f) {
                            close.add(chunks[yindex+1][xindex-1]);
                        }
                    }
                }
            }
            if (xindex+1<col){
                float rightBorder = (xindex+1)*chunkWidth;
                if (x>=rightBorder-Ball.dx/2f){
                    close.add(chunks[yindex][xindex+1]);
                    if (yindex>0){
                        float topBorder = yindex*chunkHeight;
                        if (y<=topBorder+Ball.dx/2f) {
                            close.add(chunks[yindex-1][xindex+1]);
                        }
                    }
                    if (yindex+1<row){
                        float bottomBorder = (yindex+1)*chunkHeight;
                        if (y>=bottomBorder-Ball.dx/2f) {
                            close.add(chunks[yindex+1][xindex+1]);
                        }
                    }
                }
            }
            if (yindex>0){
                float topBorder = yindex*chunkHeight;
                if (y<=topBorder+Ball.dx/2f) {
                    close.add(chunks[yindex-1][xindex]);
                    if (xindex>0) {
                        float leftBorder = xindex * chunkWidth;
                        if (x <= leftBorder + Ball.dx / 2f) {
                            close.add(chunks[yindex-1][xindex-1]);
                        }
                    }
                    if (xindex+1<col) {
                        float rightBorder = (xindex + 1) * chunkWidth;
                        if (x >= rightBorder - Ball.dx / 2f) {
                            close.add(chunks[yindex-1][xindex+1]);
                        }
                    }
                }
            }
            if (yindex+1<row){
                float bottomBorder = (yindex+1)*chunkHeight;
                if (y>=bottomBorder-Ball.dx/2f) {
                    close.add(chunks[yindex+1][xindex]);
                    if (xindex>0) {
                        float leftBorder = xindex * chunkWidth;
                        if (x <= leftBorder + Ball.dx / 2f) {
                            close.add(chunks[yindex+1][xindex-1]);
                        }
                    }
                    if (xindex+1<col) {
                        float rightBorder = (xindex + 1) * chunkWidth;
                        if (x >= rightBorder - Ball.dx / 2f) {
                            close.add(chunks[yindex+1][xindex+1]);
                        }
                    }
                }
            }
        }
        return close;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public int[] getPixel(float x,float y){
        x = ((x/width)*img.getWidth());
        y = ((y/height)*img.getHeight());
        if (x<0 || x>=img.getWidth() || y<0 || y>=img.getHeight()){
            return new int[] {0,0,0};
        }
        short[] pix = pixels.getRGB((int) x,(int) y);
        return new int[] {pix[0],pix[1],pix[2]};
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        img = null;
        try
        {
            img = ImageIO.read(new File("img.png")); // eventually C:\\ImageTest\\pic2.jpg
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if (load){
            loaded = FileInput.readFile("out.txt", true, true);
        }
        pixels = new FastRGB(img);
        chunks = new Chunk[row][col];
        balls = new ArrayList<>();
        for (int y=0;y<row;y++){
            for (int x=0;x<col;x++){
                chunks[y][x] = new Chunk(x,y,chunkWidth,chunkHeight);
            }
        }
        Group root = new Group();
        Scene scene = new Scene(root,width,height);
        primaryStage.setScene(scene);
        primaryStage.show();
        Canvas canvas = new Canvas(width,height);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw(now);
            }
        };
        timer.start();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Collisions");
        root.setOnMouseMoved(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event) {
                mouseX = event.getX();
                mouseY = event.getY();
            }
        });
    }

    public void draw(long now){
        float frameTime = (now-old)/1000000f;
        if (save) {
            for (int s=0;s<100;s++) {
                int amount = 20;
                int perFrame = 10;
                if (frame == count / (amount / perFrame) + 300) {
                    System.out.println("saving");
                    FileOutput.writeToFile("out.txt", "", false, false);
                    for (int i = 0; i < count; i++) {
                        Ball ball = balls.get(i);
                        int x = (int) (ball.x);
                        int y = (int) (ball.y);
                        int[] pix = getPixel(x, y);
                        FileOutput.writeToFile("out.txt", String.format("%d %d", x,y), true, true);
                    }
                    System.out.println("saved");
                }
                if (frame % perFrame == 0) {
                    for (int i = -amount / 2; i < amount / 2; i++) {
                        if (balls.size() < count) {
                            float x = 10;
                            float y = 250 + i * Ball.dx * 2f;
                            float velx = 150f;
                            float vely = 0;
                            Ball ball = new Ball(x, y);
                            ball.accx = velx;
                            ball.accy = vely;
                            if (load) {
                                String[] line = FileInput.readFile("out.txt", true, true)[balls.size()].split(" ");
                                int r = Integer.parseInt(line[0]);
                                int g = Integer.parseInt(line[1]);
                                int b = Integer.parseInt(line[2]);
                                ball.col = Color.rgb(r, g, b);
                            }
                            getChunk(x, y).addBall(ball);
                            balls.add(ball);
                        }
                    }
                }
                frame += 1;
                if (frame%10==0 &&frame <= count / (amount / perFrame) + 300) {
                    System.out.println(String.format("%%%.2f", (float) frame / (count / (amount / perFrame) + 300) * 100));
                }
                for (Ball i : balls) {
                    i.RemoveFromChunk(chunks);
                    i.applyGravity();
                    i.collisions(chunks, GetCloseChunks(i.x, i.y));
                    i.fixPos();
                    i.updatePos(1.5f/Ball.precision);
                    i.fixPos();
                    i.AddToChunk(chunks);
                }
                for (int step = 0; step < Ball.precision; step++) {
                    for (Ball i : balls) {
                        i.RemoveFromChunk(chunks);
                        i.fixPos();
                        i.collisions(chunks, GetCloseChunks(i.x, i.y));
                        i.AddToChunk(chunks);
                    }
                }
            }
        }
        if (render){
            int amount = 20;
            int perFrame = 10;
            if (frame % perFrame == 0) {
                for (int i = -amount / 2; i < amount / 2; i++) {
                    if (balls.size() < count) {
                        float x = 10;
                        float y = 250 + i * Ball.dx * 2f;
                        float velx = 150f;
                        float vely = 0;
                        Ball ball = new Ball(x, y);
                        ball.accx = velx;
                        ball.accy = vely;
                        if (load) {
                            String[] line = loaded[balls.size()].split(" ");;
                            int posx = Integer.parseInt(line[0]);
                            int posy = Integer.parseInt(line[1]);
                            int[] pix = getPixel(posx,posy);
                            ball.col = Color.rgb(pix[0], pix[1], pix[2]);
                        }
                        getChunk(x, y).addBall(ball);
                        balls.add(ball);
                    }
                }
            }
            if (frame%10==0 && frame <= count / (amount / perFrame) + 300) {
                System.out.println(String.format("%%%.2f", (float) frame / (count / (amount / perFrame) + 300) * 100));
            }
            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getWidth());
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getWidth());
            /*for (int y=0;y<row;y++){
                for (int x=0;x<col;x++){
                    //chunks[y][x].draw(gc);
                }
            }
            for (Chunk i : GetCloseChunks(mouseX,mouseY)){
                i.draw(gc);
            }*/
            frame += 1;
            for (Ball i : balls) {
                i.RemoveFromChunk(chunks);
                i.applyGravity();
                i.collisions(chunks, GetCloseChunks(i.x, i.y));
                i.fixPos();
                i.updatePos(1.5f/Ball.precision);
                i.fixPos();
                i.AddToChunk(chunks);
            }
            for (int step = 0; step < Ball.precision; step++) {
                for (Ball i : balls) {
                    i.RemoveFromChunk(chunks);
                    i.fixPos();
                    i.collisions(chunks, GetCloseChunks(i.x, i.y));
                    i.AddToChunk(chunks);
                }
            }
            for (Ball i : balls) {
                i.draw(gc);
            }
        }
        if (frame%10==1) {
            fps = 1000 / frameTime;
        }

        /*gc.setFill(Color.BLACK);
        gc.fillRect(0,0,100,50);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(15));
        gc.fillText(String.format("FrameRate:%.2f",fps),10,25);*/
        old = now;
    }
}