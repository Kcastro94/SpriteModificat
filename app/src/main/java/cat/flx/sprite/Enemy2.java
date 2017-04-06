package cat.flx.sprite;

/**
 * Created by DAM on 6/4/17.
 */

public class Enemy2 extends Character {


    private static int[][] states = {
            {5, 6, 7, 8, 9, 0, 1, 2, 3, 4}
    };

    int[][] getStates() { return states; }

    int x1, x2, dir;

    Enemy2(Game game) {
        super(game);
        padLeft = padTop = 6;
        colWidth = 20; colHeight = 16;
        dir = 1;
    }

    void physics() {
        x += dir;
        if ((x <= x1) || (x >= x2)) dir = -dir;
    }
}
