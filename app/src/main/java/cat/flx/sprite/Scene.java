package cat.flx.sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class Scene {

    private String scene[];
    private Paint paint;
    private Game game;
    private BitmapSet bitmapSet;
    private  BitmapSet bitmapSet2;

    private int sceneWidth, sceneHeight, waterLevel;

    private static final String sceneChars = ".{}[#]<->|(+)";
    private static final int[] sceneIndexes =
            new int[] { 23, 21, 22, 44, 45, 46, 35, 36, 37, 40, 26, 27, 28 };

    Scene(Game game) {
        this.game = game;
        this.bitmapSet = game.getBitmapSet();
        this.bitmapSet2 = game.getBitmapSet2();
        paint = new Paint();
    }

    void loadFromFile(int resource) {
        waterLevel = 99999;
        InputStream res = game.getResources().openRawResource(resource);
        BufferedReader reader = new BufferedReader(new InputStreamReader(res));
        List<String> lines = new ArrayList<>();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;
                if (line.startsWith("WATER=")) {
                    String[] parts = line.split("=");
                    waterLevel = Integer.parseInt(parts[1].trim());
                    continue;
                }
                if (line.startsWith("COIN=")) {
                    String[] parts = line.split("=");
                    String[] coors = parts[1].trim().split(",");
                    Coin coin = new Coin(game);
                    coin.x = Integer.parseInt(coors[0].trim()) * 16;
                    coin.y = Integer.parseInt(coors[1].trim()) * 16;
                    game.addCoin(coin);
                    continue;
                }
                if (line.startsWith("ENEMY=")) {
                    String[] parts = line.split("=");
                    String[] coors = parts[1].trim().split(",");
                    Enemy enemy = new Enemy(game);
                    enemy.x1 = Integer.parseInt(coors[0].trim()) * 16;
                    enemy.x2 = Integer.parseInt(coors[1].trim()) * 16;
                    enemy.y = Integer.parseInt(coors[2].trim()) * 16 - 6;
                    enemy.x = enemy.x1;
                    game.addEnemy(enemy);
                    continue;
                }
                if (line.startsWith("ENEMY2=")) {
                    String[] parts = line.split("=");
                    String[] coors = parts[1].trim().split(",");
                    Enemy2 enemy2 = new Enemy2(game);
                    enemy2.x1 = Integer.parseInt(coors[0].trim()) * 16;
                    enemy2.x2 = Integer.parseInt(coors[1].trim()) * 16;
                    for(int i=2; i<coors.length-2; i++) {
                        enemy2.y = Integer.parseInt(coors[i].trim()) * 16 - 6;
                    }
                    enemy2.x = enemy2.x1;
                    game.addEnemy2(enemy2);
                    continue;
                }
                lines.add(line);
            }
            scene = lines.toArray(new String[0]);
            reader.close();
            sceneHeight = scene.length;
            sceneWidth = scene[0].length();
        }
        catch (IOException e) {
            Toast.makeText(game.getContext(), "Error loading scene:" +  e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    boolean isGround(int r, int c) {
        if (r < 0) return false;
        if (r >= sceneHeight) return false;
        if (c < 0) return false;
        if (c >= sceneWidth) return false;
        char sc = scene[r].charAt(c);
        return ("[#]<->".indexOf(sc) != -1);
    }
    boolean isWall(int r, int c) {
        if (r < 0) return false;
        if (r >= sceneHeight) return false;
        if (c < 0) return false;
        if (c >= sceneWidth) return false;
        char sc = scene[r].charAt(c);
        return ("[#]".indexOf(sc) != -1);
    }

    int getSceneWidth() { return sceneWidth; }
    int getSceneHeight() { return sceneHeight; }
    int getWaterLevel() { return waterLevel; }

    int getWidth() { return sceneWidth * 16; }
    int getHeight() { return sceneHeight * 16; }

    void draw(Canvas canvas) {
        if (scene == null) return;
        for(int y = 0; y < scene.length; y++) {
            for(int x = 0; x < scene[0].length(); x++) {
                Bitmap bitmap;
                //Bitmap bitmap2;
                char c = scene[y].charAt(x);
                int i = sceneChars.indexOf(c);
                int index = sceneIndexes[i];
                int bgIdx = 23;
                if (y == waterLevel) bgIdx = 24;
                if (y > waterLevel) bgIdx = 25;
                bitmap = bitmapSet.getBitmap(bgIdx);
                //bitmap2 = bitmapSet2.getBitmap(0);
                //canvas.drawBitmap(bitmap2, x*16, y*16, paint);
                canvas.drawBitmap(bitmap, x*16, y*16, paint);

                if (index == 23) continue;
                bitmap = bitmapSet.getBitmap(index);
                //bitmap2 = bitmapSet2.getBitmap(0);
                //canvas.drawBitmap(bitmap2, x*16, y*16, paint);
                canvas.drawBitmap(bitmap, x*16, y*16, paint);

            }
        }
    }
}
