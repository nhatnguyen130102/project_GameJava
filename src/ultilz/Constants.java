package ultilz;

import main.Game;

public class Constants {
    public static class EnemyConstants {
        //state enemy

        //Crabby enemy info
        public static final int CRABBY = 0;
        public static final int CRABBY_IDLE = 0;
        public static final int CRABBY_RUNNING = 1;
        public static final int CRABBY_ATTACK = 2;
        public static final int CRABBY_HIT = 3;
        public static final int CRABBY_DEAD = 4;
        public static final int CRABBY_WIDTH_DEFAULT = 72;//size co ban cua 1 frames
        public static final int CRABBY_HEIGHT_DEFAULT = 32;
        public static final int CRABBY_WIDTH = (int) (CRABBY_WIDTH_DEFAULT * Game.SCALE);
        public static final int CRABBY_HEIGHT = (int) (CRABBY_HEIGHT_DEFAULT * Game.SCALE);
        public static final int HB_CRABBY_WIDTH_DEFAULT = 22;//size co ban cua hitbox
        public static final int HB_CRABBY_HEIGHT_DEFAULT = 19;
        public static final int HB_CRABBY_WIDTH = (int) (HB_CRABBY_WIDTH_DEFAULT * Game.SCALE);
        public static final int HB_CRABBY_HEIGHT = (int) (HB_CRABBY_HEIGHT_DEFAULT * Game.SCALE);
        public static final int CRABBY_DRAW_OFFSET_X = (int) (26 * Game.SCALE);//lech trai
        public static final int CRABBY_DRAW_OFFSET_Y = (int) (9 * Game.SCALE);//lech tren
        public static final int CRABBY_MAX_HEALTH = 50;
        public static final int CRABBY_DMG = 10;
        //Whale enemy info
        public static final int WHALE = 1;
        public static final int WHALE_IDLE = 0;
        public static final int WHALE_RUNNING = 1;
        public static final int WHALE_ATTACK = 2;
        private static final int WHALE_SWALOW = 3;
        public static final int WHALE_HIT = 4;
        public static final int WHALE_DEAD_HIT = 5;
        public static final int WHALE_DEAD_GROUND = 6;
        public static final int WHALE_JUMP = 7;
        public static final int WHALE_GROUND = 8;
        public static final int WHALE_FALL = 9;
        public static final int WHALE_JUMP_ANTICIPATION = 10;
        public static final int WHALE_WIDTH_DEFAULT = 68;//size co ban cua 1 frames
        public static final int WHALE_HEIGHT_DEFAULT = 46;
        public static final int WHALE_WIDTH = (int) (WHALE_WIDTH_DEFAULT * Game.SCALE);
        public static final int WHALE_HEIGHT = (int) (WHALE_HEIGHT_DEFAULT * Game.SCALE);
        public static final int HB_WHALE_WIDTH_DEFAULT = 50;//size co ban cua hitbox
        public static final int HB_WHALE_HEIGHT_DEFAULT = 30;
        public static final int HB_WHALE_WIDTH = (int) (HB_WHALE_WIDTH_DEFAULT * Game.SCALE );
        public static final int HB_WHALE_HEIGHT = (int) (HB_WHALE_HEIGHT_DEFAULT * Game.SCALE);
        public static final int WHALE_DRAW_OFFSET_X = (int) (3 * Game.SCALE);//lech trai
        public static final int WHALE_DRAW_OFFSET_Y = (int) (16 * Game.SCALE);//lech tren
        public static final int WHALE_MAX_HEALTH = 100;
        public static final int WHALE_DMG = 10;

        //
        public static int GetSpriteAmount(int enemy_type, int enemy_state) {
            switch (enemy_type) {
                case CRABBY -> {
                    switch (enemy_state) {
                        case CRABBY_IDLE:
                            return 9;
                        case CRABBY_RUNNING:
                            return 6;
                        case CRABBY_ATTACK:
                            return 7;
                        case CRABBY_HIT:
                            return 4;
                        case CRABBY_DEAD:
                            return 5;
                    }
                }
                case WHALE -> {
                    switch (enemy_state) {
                        case WHALE_IDLE:
                            return 44;
                        case WHALE_RUNNING:
                            return 14;
                        case WHALE_ATTACK:
                            return 11;
                        case WHALE_SWALOW:
                            return 10;
                        case WHALE_HIT:
                            return 7;
                        case WHALE_DEAD_HIT:
                            return 6;
                        case WHALE_DEAD_GROUND:
                            return 4;
                        case WHALE_JUMP:
                            return 4;
                        case WHALE_GROUND:
                            return 3;
                        case WHALE_FALL:
                            return 2;
                        case WHALE_JUMP_ANTICIPATION:
                            return 1;
                    }
                }
            }
            return 0;
        }

        //lay mau toi da cua enemy
        public static int GetMaxHealth(int enemy_type) {
            switch (enemy_type) {
                case WHALE:
                    return WHALE_MAX_HEALTH;
                case CRABBY:
                    return CRABBY_MAX_HEALTH;// mau toi da cua enemy
                default:
                    return 1;
            }
        }

        // lay dmg cua enemy
        public static int GetEnemyDmg(int enemy_type) {
            switch (enemy_type) {
                case WHALE:
                    return WHALE_DMG;
                case CRABBY:
                    return CRABBY_DMG;//dmg toi da cua enemy
                default:
                    return 0;
            }
        }
    }

    public static class Enviroment {
        public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
        public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
        public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
        public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;
        public static final int SMALL_CLOUD_WIDTH = (int) (74 * Game.SCALE);
        public static final int SMALL_CLOUD_HEIGHT = (int) (24 * Game.SCALE);
        public static final int BIG_CLOUD_WIDTH = (int) (448 * Game.SCALE);
        public static final int BIG_CLOUD_HEIGHT = (int) (101 * Game.SCALE);

    }

    public static class UI {
        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE);
        }

        public static class Buttons {
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class UrmButton {
            public static final int URM_SIZE_DEFAULT = 56;
            public static final int URM_SIZE = (int) (URM_SIZE_DEFAULT * Game.SCALE);
        }

        public static class VolumeButton {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;
            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);
        }
    }

    public static class Directions {
        public static final int LEFT = 0;
        public static final int RIGHT = 2;
        public static final int UP = 1;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants {
        public static final int RUNNING = 1;
        public static final int IDLE = 0;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        //        public static final int GROUND = 4;
        public static final int HIT = 5;
        public static final int ATTACK = 4;
        public static final int DEAD = 6;
        //        public static final int ATTACK_JUMP_1 = 7;
//        public static final int ATTACK_JUMP_2 = 8;
        public static final int HB_PLAYER_WIDTH_DEFAULT = 20;
        public static final int HB_PLAYER_HEIGHT_DEFAULT = 27;
        public static final int HB_PLAYER_WIDTH = (int) (HB_PLAYER_WIDTH_DEFAULT * Game.SCALE);
        public static final int HB_PLAYER_HEIGHT = (int) (HB_PLAYER_HEIGHT_DEFAULT * Game.SCALE);
        public static final int PLAYER_DMG = 10;

        public static int getSpriteAmout(int player_action) {
            switch (player_action) {
                case DEAD:
                    return 9;
                case RUNNING:
                    return 6;
                case IDLE:
                    return 5;
                case HIT:
                    return 4;
                case JUMP:
                case ATTACK:
                    return 3;
                case FALLING:
                default:
                    return 1;
            }
        }
    }
}
