package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.ArrayList;
import java.util.List;

public class EnemyFleet {

    private static final int ROWS_COUNT = 3;
    private static final int COLUMNS_COUNT = 10;
    private static final int STEP = ShapeMatrix.ENEMY.length + 1;
    private List<EnemyShip> ships;
    private Direction direction = Direction.RIGHT;


    private void createShips() {
        ships = new ArrayList<EnemyShip>();
        for (int x = 0; x < COLUMNS_COUNT; x++) {
            for (int y = 0; y < ROWS_COUNT; y++) {
                ships.add(new EnemyShip(x * STEP, y * STEP + 12));
            }
        }
        ships.add(new Boss(STEP * COLUMNS_COUNT / 2 - ShapeMatrix.BOSS_ANIMATION_FIRST.length / 2 - 1, 5));
    }

    public EnemyFleet() {
        createShips();
    }

    public void draw(Game game) {
        for (EnemyShip enemy : ships) {
            enemy.draw(game);
        }
    }

    private double getLeftBorder() {
        double x = 500;
        for (EnemyShip enemy : ships) {
            x = enemy.x < x ? enemy.x : x;
        }
        return x;
    }

    private double getRightBorder() {
        double x = 0;
        for (EnemyShip enemy : ships) {
            x = enemy.x + enemy.width > x ? enemy.x + enemy.width : x;
        }
        return x;
    }

    private double getSpeed() {
        double maxSpeed = 2.0;
        double speed = 3.0 / ships.size();
        return maxSpeed > speed ? speed : maxSpeed;
//        if (maxSpeed > speed) {
//            return speed;
//        } else {
//            return maxSpeed;
//        }

    }

    public void move() {
        if (ships.size() == 0) {
        } else if (direction == Direction.LEFT && getLeftBorder() < 0) {
            direction = Direction.RIGHT;
            for (EnemyShip enemy : ships) {
                enemy.move(Direction.DOWN, getSpeed());
            }

        } else if (direction == Direction.RIGHT && getRightBorder() > SpaceInvadersGame.WIDTH) {
            direction = Direction.LEFT;
            for (EnemyShip enemy : ships) {
                enemy.move(Direction.DOWN, getSpeed());
            }
        } else {
            for (EnemyShip enemy : ships) {
                enemy.move(direction, getSpeed());
            }
        }


    }

    public Bullet fire(Game game) {
        if (ships.size() == 0) {
            return null;
        } else {
            int x = game.getRandomNumber(100 / SpaceInvadersGame.COMPLEXITY);
            int x2 = game.getRandomNumber(ships.size());
            if (x > 0) {
                return null;
            } else {
                return ships.get(x2).fire();
            }

        }
    }

    public int verifyHit(List<Bullet> bullets) {
        if (bullets.size() == 0) {
            return 0;
        } else {
            int sumScore = 0;
            for (EnemyShip enemy : ships) {

                for (int i = 0; i < bullets.size(); i++) {
                    if (enemy.isCollision(bullets.get(i)) && enemy.isAlive && bullets.get(i).isAlive) {
                        enemy.kill();
                        bullets.get(i).kill();
                        sumScore += enemy.score;
                    }
                }

            }
            return sumScore;
        }
    }

    public void deleteHiddenShips() {
        List<EnemyShip> copy = new ArrayList<>(ships);
        for (EnemyShip enemy : copy) {
            if (!enemy.isVisible()) {
                ships.remove(enemy);
            }
        }
    }

    public double getBottomBorder() {
        double bottom = 0;
        for (EnemyShip enemy : ships) {
            bottom = (enemy.y + enemy.height) > bottom ? (enemy.y + enemy.height) : bottom;
        }
        return bottom;
    }

    public int getShipsCount() {
        return ships.size();
    }

}
