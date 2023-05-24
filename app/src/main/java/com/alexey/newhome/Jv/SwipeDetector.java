package com.alexey.newhome.Jv;


import android.util.Log;
import android.view.MotionEvent;

import com.alexey.newhome.R;


/**Created by JaKo coding (Nikolay) 14.02.2020
 *
 * движение определяется по вектору м/у точкой косания и точкой отрыва
 * в момент отрыва
 * */

public abstract class SwipeDetector {

    private static final String TAG = "myLOG";

    private float startX = 0;
    private float startY = 0;
    private int minToachLen = 10;

    //interactions
    public abstract void onSwipeDetected(Direction direction);

    public SwipeDetector(int minToachLen) {
        this.minToachLen = minToachLen * 5;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                float dx = event.getX() - startX;
                float dy = event.getY() - startY;
                if (calcDist(dx, dy) >= minToachLen) {
                    onSwipeDetected(Direction.get(calcAngle(dx, dy)));
                }
                startY = startX = 0;
                break;
            default:
                startY = startX = 0;
                Log.d(TAG, "onTouchEvent: Error");
        }

        return false;
    }

    private int calcAngle(float dx, float dy) {
        return (int) ((Math.atan2(dy, dx) + Math.PI)*180 /Math.PI +180) % 360;
    }

    private double calcDist(float dx, float dy) {
        return Math.sqrt(dx*dx + dy*dy);
    }

    public enum Direction {
        UN_EXPT(R.anim.stay_enter, R.anim.stay_exit), //stay in
        LEFT(R.anim.left_enter, R.anim.left_exit),
        RIGHT(R.anim.right_enter, R.anim.right_exit),
        UP(R.anim.up_enter, R.anim.up_exit),
        DOWN(R.anim.down_enter, R.anim.down_exit);

        Direction(int enterAnim, int exitAnim) {
            this.enterAnim = enterAnim;
            this.exitAnim = exitAnim;
        }

        private int enterAnim, exitAnim;

        public int getEnterAnim() {
            return enterAnim;
        }

        public int getExitAnim() {
            return exitAnim;
        }

        public static Direction get (int angle){
            Direction res = UN_EXPT;
            if (inRange(angle, 45, 135)) res = UP;
            else if (inRange(angle, 135, 225)) res = RIGHT;
            else if (inRange(angle, 225, 315)) res = DOWN;
            else if (inRange(angle, 315, 360) || inRange(angle, 0, 45)) res = LEFT;
            return  res;
        }

        private static boolean inRange(int val, int min, int max){
            return (val >= min && val <= max);
        }
    }
}

