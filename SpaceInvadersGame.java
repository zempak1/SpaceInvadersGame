package com.javarush.games.spaceinvaders;

import com.javarush.engine.cell.*;
import com.javarush.games.spaceinvaders.gameobjects.*;

import java.util.ArrayList;
import java.util.List;

public class SpaceInvadersGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private List<Star> stars;
    private EnemyFleet enemyFleet;
    public final static int COMPLEXITY = 5;
    private List<Bullet> enemyBullets;
    private List<Bullet> playerBullets;
    private PlayerShip playerShip;
    private boolean isGameStopped;
    private int animationsCount;
    private static final int PLAYER_BULLETS_MAX = 1;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        showGrid(true);
        createGame();
    }

    private void createGame() {
        enemyFleet = new EnemyFleet();
        enemyBullets = new ArrayList<Bullet>();
        playerShip = new PlayerShip();
        playerBullets = new ArrayList<Bullet>();
        isGameStopped = false;
        animationsCount = 0;
        score = 0;
        setScore(score);
        createStars();
        setTurnTimer(40);
        drawScene();
    }


    //Отрисовка
    private void drawScene() {
        drawField();
        enemyFleet.draw(this);
        for (Bullet list : enemyBullets) {
            list.draw(this);
        }
        playerShip.draw(this);
        for (Bullet list : playerBullets) {
            list.draw(this);
        }
    }

    private void drawField() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                setCellValueEx(x, y, Color.BLACK, "");
            }
        }

        for (Star x : stars) {
            x.draw(this);
        }
    }

    private void createStars() {
        stars = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            int x1 = getRandomNumber(4, 60);
            int y1 = getRandomNumber(4, 60);
            stars.add(new Star(x1, y1));
        }
    }

    @Override
    public void onTurn(int x) {
        moveSpaceObjects();
        check();
        setScore(score);
        Bullet bullet = enemyFleet.fire(this);
        if (bullet != null) {
            enemyBullets.add(bullet);
        }

        drawScene();
    }

    private void moveSpaceObjects() {
        enemyFleet.move();
        for (Bullet list : enemyBullets) {
            list.move();
        }
        playerShip.move();
        for (Bullet list : playerBullets) {
            list.move();
        }
    }

    private void removeDeadBullets() {
        List<Bullet> copy = new ArrayList<Bullet>(enemyBullets);
        for (Bullet list : copy) {
            if (list.isAlive == false || list.y >= HEIGHT - 1) {
                enemyBullets.remove(list);
            }
        }

        List<Bullet> copy2 = new ArrayList<>(playerBullets);
        for (Bullet list : copy2) {
            if (list.isAlive == false || list.y + list.height < 0) {
                playerBullets.remove(list);
            }
        }
    }

    private void check() {
        playerShip.verifyHit(enemyBullets);
        score += enemyFleet.verifyHit(playerBullets);
        enemyFleet.deleteHiddenShips();
        removeDeadBullets();
        double bottom = enemyFleet.getBottomBorder();
        if (bottom >= playerShip.y) {
            playerShip.kill();
        }
        if (!playerShip.isAlive) {
            stopGameWithDelay();
        }
        int shipsCount = enemyFleet.getShipsCount();
        if (shipsCount == 0) {
            playerShip.win();
            stopGameWithDelay();
        }
    }

    private void stopGame(boolean isWin) {
        isGameStopped = true;
        stopTurnTimer();
        if (isWin) {
            showMessageDialog(Color.AQUA, "YOU WIN", Color.GREEN, 50);
        } else {
            showMessageDialog(Color.AQUA, "YOU LOOSE", Color.RED, 50);
        }
    }

    private void stopGameWithDelay() {
        animationsCount++;
        if (animationsCount >= 10) {
            stopGame(playerShip.isAlive);
        }
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.SPACE && isGameStopped == true) {
            createGame();
        } else if (key == Key.LEFT) {
            playerShip.setDirection(Direction.LEFT);
        } else if (key == Key.RIGHT) {
            playerShip.setDirection(Direction.RIGHT);
        } else if (key == Key.SPACE) {
            Bullet x = playerShip.fire();
            if (x != null && playerBullets.size() < PLAYER_BULLETS_MAX) {
                playerBullets.add(x);
            }
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if (key == Key.LEFT && playerShip.getDirection() == Direction.LEFT) {
            playerShip.setDirection(Direction.UP);
        } else if (key == Key.RIGHT && playerShip.getDirection() == Direction.RIGHT) {
            playerShip.setDirection(Direction.UP);
        }
    }

    @Override
    public void setCellValueEx(int x, int y, Color color, String string) {
        if (x < 0 || x > WIDTH - 1 || y < 0 || y > HEIGHT - 1) {
        } else {
            super.setCellValueEx(x, y, color, string);
        }
    }
}
