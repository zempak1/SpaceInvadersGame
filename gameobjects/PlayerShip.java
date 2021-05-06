package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.List;

public class PlayerShip extends Ship {

    private Direction direction = Direction.UP;

    public PlayerShip() {
        super(SpaceInvadersGame.WIDTH / 2.0, SpaceInvadersGame.HEIGHT - ShapeMatrix.PLAYER.length - 1);
        setStaticView(ShapeMatrix.PLAYER);

    }

    public void verifyHit(List<Bullet> bullets) {
        if (bullets.size() == 0) {
        } else if (isAlive) {
            for (int i = 0; i < bullets.size(); i++) {
                if (bullets.get(i).isAlive) {
                    if (isCollision(bullets.get(i)) == true) {
                        kill();
                        bullets.get(i).kill();
                    }
                }
            }
        }
    }

    @Override
    public void kill() {
        if (!isAlive) {
        } else {
            isAlive = false;
            setAnimatedView(false, ShapeMatrix.KILL_PLAYER_ANIMATION_FIRST,
                    ShapeMatrix.KILL_PLAYER_ANIMATION_SECOND,
                    ShapeMatrix.KILL_PLAYER_ANIMATION_THIRD,
                    ShapeMatrix.DEAD_PLAYER);
        }
    }

    public void setDirection(Direction newDirection) {
        if (newDirection != Direction.DOWN) {
            direction = newDirection;
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public void move() {
        if (isAlive) {
            if (direction == Direction.LEFT) {
                x--;
            } else if (direction == Direction.RIGHT) {
                x++;
            }
            if (x < 0) {
                x = 0;
            } else if (x + width > SpaceInvadersGame.WIDTH) {
                x = SpaceInvadersGame.WIDTH - width;
            }
        }
    }

    @Override
    public Bullet fire() {
        if (!isAlive) {
            return null;
        } else {
            return new Bullet(x + 2, y - ShapeMatrix.BULLET.length, Direction.UP);
        }
    }

    public void win() {
        setStaticView(ShapeMatrix.WIN_PLAYER);
    }
}
