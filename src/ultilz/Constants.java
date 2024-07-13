package ultilz;

import main.Game;

public class Constants {
    public static class BehaviorOpening{
        public static final int BEHAVIOR_OPENING = 1;
        public static final int BEHAVIOR_OPENING_WIDTH_DEFAULT = 20;
        public static final int BEHAVIOR_OPENING_HEIGHT_DEFAULT = 22;
        public static final int BEHAVIOR_OPENING_WIDTH = (int) (BEHAVIOR_OPENING_WIDTH_DEFAULT * Game.SCALE);
        public static final int BEHAVIOR_OPENING_HEIGHT = (int) (BEHAVIOR_OPENING_HEIGHT_DEFAULT * Game.SCALE);


    }
    public static class BehaviorClosing{
        public static final int BEHAVIOR_CLOSING = 1;
        public static final int BEHAVIOR_CLOSING_WIDTH_DEFAULT = 20;
        public static final int BEHAVIOR_CLOSING_HEIGHT_DEFAULT = 22;
        public static final int BEHAVIOR_CLOSING_WIDTH = (int) (BEHAVIOR_CLOSING_WIDTH_DEFAULT * Game.SCALE);
        public static final int BEHAVIOR_CLOSING_HEIGHT = (int) (BEHAVIOR_CLOSING_HEIGHT_DEFAULT * Game.SCALE);

    }
    public static class BombTiles {
        // Bomb info
        public static final int BOMB = 2;
        public static final int BOMB_OFF = 0;
        public static final int BOMB_ON = 1;
        public static final int BOMB_EXPLOTION = 2;
        public static final int BOMB_WIDTH_DEFAULT = 96;
        public static final int BOMB_HEIGHT_DEFAULT = 108;
        public static final int BOMB_WIDTH = (int) (BOMB_WIDTH_DEFAULT * Game.SCALE);
        public static final int BOMB_HEIGHT = (int) (BOMB_HEIGHT_DEFAULT * Game.SCALE);
        public static final int HB_BOMB_WIDTH_DEFAULT = 30;
        public static final int HB_BOMB_HEIGHT_DEFAULT = 31;
        public static final int HB_BOMB_WIDTH = (int) (HB_BOMB_WIDTH_DEFAULT * Game.SCALE);
        public static final int HB_BOMB_HEIGHT = (int) (HB_BOMB_HEIGHT_DEFAULT * Game.SCALE);
        public static final int BOMB_DRAW_OFFSET_X = (int) (34 * Game.SCALE);
        public static final int BOMB_DRAW_OFFSET_Y = (int) (57 * Game.SCALE);
        public static final int BOMB_DMG = 1;

        public static int GetSpriteAmount(int object_type) {
            switch (object_type) {
                case BOMB_OFF:
                    return 1;
                case BOMB_ON:
                    return 10;
                case BOMB_EXPLOTION:
                    return 9;
            }
            return 1;
        }
    }

    public static class Projectiles {
        public static final int CANNON_BALL_DEFAULT_WIDTH = 15;
        public static final int CANNON_BALL_DEFAULT_HEIGHT = 15;

        public static final int CANNON_BALL_WIDTH = (int) (Game.SCALE * CANNON_BALL_DEFAULT_WIDTH) * 2;
        public static final int CANNON_BALL_HEIGHT = (int) (Game.SCALE * CANNON_BALL_DEFAULT_HEIGHT) * 2;

        public static final int BALL_EXPLODE_WIDTH_DEFAULT = 54;
        public static final int BALL_EXPLODE_HEIGHT_DEFAULT = 60;
        public static final int BALL_EXPLODE_WIDTH = (int) (BALL_EXPLODE_WIDTH_DEFAULT * Game.SCALE);
        public static final int BALL_EXPLODE_HEIGHT = (int) (BALL_EXPLODE_HEIGHT_DEFAULT * Game.SCALE);
        public static final float SPEED = 0.75f * Game.SCALE;
    }
    public static class ObjectConstants {
        public static final int BLUE_POTION = 0;
        public static final int RED_POTION = 1;
        public static final int BARREL = 2;
        public static final int BOX = 3;
        public static final int CANNON_LEFT = 5;
        public static final int CANNON_RIGHT = 6;
        public static final int SPIKE = 7;
        public static final int RED_POTION_VALUE = 15;
        public static final int BLUE_POTION_VALUE = 10;

        public static final int CONTAINER_WIDTH_DEFAULT = 40;
        public static final int CONTAINER_HEIGHT_DEFAULT = 30;
        public static final int CONTAINER_WIDTH = (int) (Game.SCALE * CONTAINER_WIDTH_DEFAULT) * 2;
        public static final int CONTAINER_HEIGHT = (int) (Game.SCALE * CONTAINER_HEIGHT_DEFAULT) * 2;

        public static final int POTION_WIDTH_DEFAULT = 12;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static final int POTION_WIDTH = (int) (Game.SCALE * POTION_WIDTH_DEFAULT) * 2;
        public static final int POTION_HEIGHT = (int) (Game.SCALE * POTION_HEIGHT_DEFAULT) * 2;

        public static final int CANNON_WIDTH_DEFAULT = 40;
        public static final int CANNON_HEIGHT_DEFAULT = 26;
        public static final int CANNON_WIDTH = (int) (CANNON_WIDTH_DEFAULT * Game.SCALE) * 2;
        public static final int CANNON_HEIGHT = (int) (CANNON_HEIGHT_DEFAULT * Game.SCALE) * 2;
        public static final int CANNON_DRAW_OFFSET_Y = (int) (3 * 2 * Game.SCALE);//lech trai

        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static final int SPIKE_WIDTH = (int) (SPIKE_WIDTH_DEFAULT * Game.SCALE) * 2;
        public static final int SPIKE_HEIGHT = (int) (SPIKE_HEIGHT_DEFAULT * Game.SCALE) * 2;
        public static final int SPIKE_DRAW_OFFSET_Y = (int) (16 * Game.SCALE);//lech duoi
        public static final int SPIKE_DMG = 1;


        public static final int CANNON_ATTACK_EFFECT_WIDTH_DEFAULT = 20;
        public static final int CANNON_ATTACK_EFFECT_HEIGHT_DEFAULT = 28;
        public static final int CANNON_ATTACK_EFFECT_WIDTH = (int) (CANNON_ATTACK_EFFECT_WIDTH_DEFAULT * Game.SCALE) * 2;
        public static final int CANNON_ATTACK_EFFECT_HEIGHT= (int) (CANNON_ATTACK_EFFECT_HEIGHT_DEFAULT * Game.SCALE) * 2;


        public static final int PALM_TREE = 8;
        public static final int PALM_TREE_WIDTH_DEFAULT = 51;
        public static final int PALM_TREE_HEIGHT_DEFAULT = 53;
        public static final int PALM_TREE_WIDTH = (int) (PALM_TREE_WIDTH_DEFAULT * Game.SCALE) ;
        public static final int PALM_TREE_HEIGHT = (int) (PALM_TREE_HEIGHT_DEFAULT * Game.SCALE) ;
        public static final int HB_PALM_TREE_WIDTH = 37;
        public static final int HB_PALM_TREE_HEIGHT = 28;

        public static int GetSpriteAmount(int object_type) {
            switch (object_type) {
                case RED_POTION, BLUE_POTION:
                    return 7;
                case BARREL, BOX:
                    return 8;
                case CANNON_LEFT, CANNON_RIGHT:
                    return 7;
                case PALM_TREE:
                    return 4;
            }
            return 1;
        }
    }

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
        public static final int CRABBY_WIDTH = (int) (CRABBY_WIDTH_DEFAULT * Game.SCALE) * 2;
        public static final int CRABBY_HEIGHT = (int) (CRABBY_HEIGHT_DEFAULT * Game.SCALE) * 2;
        public static final int HB_CRABBY_WIDTH_DEFAULT = 22;//size co ban cua hitbox
        public static final int HB_CRABBY_HEIGHT_DEFAULT = 19;
        public static final int HB_CRABBY_WIDTH = (int) (HB_CRABBY_WIDTH_DEFAULT * Game.SCALE) * 2;
        public static final int HB_CRABBY_HEIGHT = (int) (HB_CRABBY_HEIGHT_DEFAULT * Game.SCALE) * 2;
        public static final int CRABBY_DRAW_OFFSET_X = (int) (26 * 2 * Game.SCALE);//lech trai
        public static final int CRABBY_DRAW_OFFSET_Y = (int) (9 * 2 * Game.SCALE);//lech tren
        public static final int CRABBY_MAX_HEALTH = 50;
        public static final int CRABBY_DMG = 1;

        //Whale enemy info
        public static final int WHALE = 1;
        public static final int WHALE_IDLE = 0;
        public static final int WHALE_RUNNING = 1;
        public static final int WHALE_ATTACK = 2;
        public static final int WHALE_SWALOW = 3;
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
        public static final int HB_WHALE_HEIGHT_DEFAULT = 40;
        public static final int HB_WHALE_WIDTH = (int) (HB_WHALE_WIDTH_DEFAULT * Game.SCALE );
        public static final int HB_WHALE_HEIGHT = (int) (HB_WHALE_HEIGHT_DEFAULT * Game.SCALE);
        public static final int WHALE_DRAW_OFFSET_X = (int) (3 * Game.SCALE);//lech trai
        public static final int WHALE_DRAW_OFFSET_Y = (int) (4 * Game.SCALE);//lech tren
        public static final int WHALE_MAX_HEALTH = 100;
        public static final int WHALE_DMG = 10;
        //captain enemy info
        public static final int CAPTAIN = 2;
        public static final int CAPTAIN_IDLE = 0;
        public static final int CAPTAIN_RUNNING = 1;
        public static final int CAPTAIN_JUMP_ANTICIPATION = 2;
        public static final int CAPTAIN_JUMP = 3;
        public static final int CAPTAIN_FALL = 4;
        public static final int CAPTAIN_GROUND = 5;
        public static final int CAPTAIN_ATTACK = 6;
        public static final int CAPTAIN_SCARE_RUN = 7;
        public static final int CAPTAIN_HIT = 8;
        public static final int CAPTAIN_DEAD_HIT = 9;
        public static final int CAPTAIN_DEAD_GROUND = 10;


        public static final int CAPTAIN_WIDTH_DEFAULT = 80;//size co ban cua 1 frames
        public static final int CAPTAIN_HEIGHT_DEFAULT = 72;
        public static final int CAPTAIN_WIDTH = (int) (CAPTAIN_WIDTH_DEFAULT * Game.SCALE);
        public static final int CAPTAIN_HEIGHT = (int) (CAPTAIN_HEIGHT_DEFAULT * Game.SCALE);
        public static final int HB_CAPTAIN_WIDTH_DEFAULT = 30;//size co ban cua hitbox
        public static final int HB_CAPTAIN_HEIGHT_DEFAULT = 50;
        public static final int HB_CAPTAIN_WIDTH = (int) (HB_CAPTAIN_WIDTH_DEFAULT * Game.SCALE );
        public static final int HB_CAPTAIN_HEIGHT = (int) (HB_CAPTAIN_HEIGHT_DEFAULT * Game.SCALE);
        public static final int CAPTAIN_DRAW_OFFSET_X = (int) (20 * Game.SCALE);//lech trai
        public static final int CAPTAIN_DRAW_OFFSET_Y = (int) (22 * Game.SCALE);//lech tren
        public static final int CAPTAIN_MAX_HEALTH = 100;
        public static final int CAPTAIN_DMG = 10;
        //
        public static final int ATTACK_EFFECT_WIDTH_DEFAULT = 118;
        public static final int ATTACK_EFFECT_HEIGHT_DEFAULT = 24 ;
        public static final int ATTACK_EFFECT_WIDTH = (int) (ATTACK_EFFECT_WIDTH_DEFAULT *2 * Game.SCALE);
        public static final int ATTACK_EFFECT_HEIGHT = (int) (ATTACK_EFFECT_HEIGHT_DEFAULT *2* Game.SCALE);

        public static int GetSpriteAmount(int enemy_type, int enemy_state) {
            switch (enemy_type) {
                case CAPTAIN -> {
                    switch (enemy_state) {
                        case CAPTAIN_IDLE:
                            return 32;
                        case CAPTAIN_RUNNING:
                            return 14;
                        case CAPTAIN_JUMP_ANTICIPATION:
                            return 1;
                        case CAPTAIN_JUMP:
                            return 4;
                        case CAPTAIN_FALL:
                            return 2;
                        case CAPTAIN_GROUND:
                            return 3;
                        case CAPTAIN_ATTACK:
                            return 7;
                        case CAPTAIN_SCARE_RUN:
                            return 12;
                        case CAPTAIN_HIT:
                            return 8;
                        case CAPTAIN_DEAD_HIT:
                            return 6;
                        case CAPTAIN_DEAD_GROUND:
                            return 4;
                    }
                }
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
                            return 5;
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
                case CAPTAIN:
                    return CAPTAIN_MAX_HEALTH;
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
                case CAPTAIN:
                    return CAPTAIN_DMG;
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
        public static final int IDLE = 0;
        public static final int DOOR_IN = 1;
        public static final int DOOR_OUT = 2;
        public static final int RUNNING = 3;
        public static final int HIT = 4;
        public static final int DEAD = 5;
        public static final int JUMP = 6;
        public static final int DEAD_GROUND = 7;
        public static final int GROUND = 8;
        public static final int FALLING = 9;
        //        public static final int JUMP_ANTICIPATION = 10;
        public static final int ATTACK = 10;
        public static int PLAYER_WIDTH_DEFAULT = 58;
        public static int PLAYER_HEIGHT_DEFAULT = 58;
        public static int PLAYER_WIDTH = (int) (PLAYER_WIDTH_DEFAULT * Game.SCALE);
        public static int PLAYER_HEIGHT = (int) (PLAYER_HEIGHT_DEFAULT * Game.SCALE);
        public static final float PLAYER_DRAW_OFFSET_X = 20 * Game.SCALE; // phần thừa ngang của hitbox 19
        public static final float PLAYER_DRAW_OFFSET_Y = 18 * Game.SCALE; // phần thừa dọc của hitbox 18
        //        public static final int ATTACK_JUMP_1 = 7;
//        public static final int ATTACK_JUMP_2 = 8;
        public static final int HB_PLAYER_WIDTH_DEFAULT = 28;//28
        public static final int HB_PLAYER_HEIGHT_DEFAULT = 40;//40
        public static final int HB_PLAYER_WIDTH = (int) (HB_PLAYER_WIDTH_DEFAULT * Game.SCALE);
        public static final int HB_PLAYER_HEIGHT = (int) (HB_PLAYER_HEIGHT_DEFAULT * Game.SCALE);
        public static final int PLAYER_DMG = 10;

        public static int getSpriteAmout(int player_action) {
            switch (player_action) {
                case DEAD:
                    return 6;
                case RUNNING:
                    return 14;
                case IDLE:
                    return 26;
                case HIT:
                    return 8;
                case DEAD_GROUND:
                    return 4;
                case JUMP:
                case FALLING:
                case ATTACK:
                default:
                    return 1;
            }
        }
    }
//    public static class PlayerConstants {
//        public static final int RUNNING = 1;
//        public static final int IDLE = 0;
//        public static final int JUMP = 2;
//        public static final int FALLING = 3;
//        //        public static final int GROUND = 4;
//        public static final int HIT = 5;
//        public static final int ATTACK = 4;
//        public static final int DEAD = 6;
//        //        public static final int ATTACK_JUMP_1 = 7;
////        public static final int ATTACK_JUMP_2 = 8;
//        public static int PLAYER_WIDTH_DEFAULT = 64;
//        public static int PLAYER_HEIGHT_DEFAULT = 40;
//        public static int PLAYER_WIDTH = (int) (PLAYER_WIDTH_DEFAULT * Game.SCALE*2);
//        public static int PLAYER_HEIGHT = (int) (PLAYER_HEIGHT_DEFAULT * Game.SCALE*2);
//        public static final int HB_PLAYER_WIDTH_DEFAULT = 20;
//        public static final int HB_PLAYER_HEIGHT_DEFAULT = 27;
//        public static final int HB_PLAYER_WIDTH = (int) (HB_PLAYER_WIDTH_DEFAULT * Game.SCALE)*2;
//        public static final int HB_PLAYER_HEIGHT = (int) (HB_PLAYER_HEIGHT_DEFAULT * Game.SCALE)*2;
//        public static final int PLAYER_DMG = 10;
//
//        public static int getSpriteAmout(int player_action) {
//            switch (player_action) {
//                case DEAD:
//                    return 9;
//                case RUNNING:
//                    return 6;
//                case IDLE:
//                    return 5;
//                case HIT:
//                    return 4;
//                case JUMP:
//                case ATTACK:
//                    return 3;
//                case FALLING:
//                default:
//                    return 1;
//            }
//        }
//    }
}
