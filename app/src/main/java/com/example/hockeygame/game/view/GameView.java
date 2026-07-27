package com.example.hockeygame.game.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.hockeygame.R;
import com.example.hockeygame.game.engine.GameEngine;
import com.example.hockeygame.game.model.ArenaType;
import com.example.hockeygame.game.model.Mallet;
import com.example.hockeygame.game.model.Puck;
import com.example.hockeygame.game.model.Score;
import com.example.hockeygame.game.input.SensorInputController;

public class GameView extends View {

    private final Paint debugTextPaint = new Paint();
    private static final float FIELD_MARGIN = 12f;
    private static final float FIELD_CORNER_RADIUS = 30f;

    private final Paint fieldPaint;
    private final Paint linePaint;
    private final Paint puckPaint;
    private final Paint topMalletPaint;
    private final Paint bottomMalletPaint;
    private final Paint goalPaint;
    private final Paint goalFramePaint;

    private final RectF fieldRectangle;

    private ArenaType arenaType = ArenaType.ARCTIC;

    private GameEngine gameEngine;
    private long lastFrameTimeNanos;
    private SensorInputController sensorInputController;

    private float sensorTiltX;
    private float sensorTiltY;
    private static final float MAX_TILT_DEGREES = 18f;
    private static final float SENSOR_DEAD_ZONE = 1.5f;
    private static final float MALLET_SMOOTHING = 0.18f;

    /*
     * Az ütő maximális mozgási sebessége pixel/másodpercben.
     * Ha még gyors, később csökkenthetjük.
     */
    private static final float MALLET_MAX_SPEED = 550f;

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameView(
            Context context,
            AttributeSet attrs,
            int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);

        fieldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        puckPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        topMalletPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bottomMalletPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        goalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        goalFramePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        fieldRectangle = new RectF();

        initializePaints();
        applyArenaColors();
        initializeSensorInput(context);
    }

    private void initializePaints() {
        debugTextPaint.setColor(Color.WHITE);
        debugTextPaint.setTextSize(42f);
        debugTextPaint.setAntiAlias(true);
        debugTextPaint.setStyle(Paint.Style.FILL);
        //------------------------
        linePaint.setColor(
                ContextCompat.getColor(
                        getContext(),
                        R.color.arena_line
                )
        );

        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(5f);

        topMalletPaint.setColor(
                ContextCompat.getColor(
                        getContext(),
                        R.color.top_player_mallet
                )
        );

        bottomMalletPaint.setColor(
                ContextCompat.getColor(
                        getContext(),
                        R.color.bottom_player_mallet
                )
        );

        goalPaint.setColor(
                ContextCompat.getColor(
                        getContext(),
                        R.color.goal_inside
                )
        );

        goalPaint.setStyle(Paint.Style.FILL);

        goalFramePaint.setColor(
                ContextCompat.getColor(
                        getContext(),
                        R.color.goal_frame
                )
        );

        goalFramePaint.setStyle(Paint.Style.STROKE);
        goalFramePaint.setStrokeWidth(7f);
    }

    public void setArenaType(ArenaType arenaType) {
        if (arenaType == null) {
            this.arenaType = ArenaType.ARCTIC;
        } else {
            this.arenaType = arenaType;
        }

        applyArenaColors();

        /*
         * invalidate() hatására az Android ismét meghívja
         * az onDraw() metódust.
         */
        invalidate();
    }

    private void applyArenaColors() {
        int fieldColor;
        int puckColor;

        switch (arenaType) {
            case NEON:
                fieldColor = ContextCompat.getColor(
                        getContext(),
                        R.color.neon_field
                );

                puckColor = ContextCompat.getColor(
                        getContext(),
                        R.color.neon_puck
                );
                break;

            case CLASSIC:
                fieldColor = ContextCompat.getColor(
                        getContext(),
                        R.color.classic_field
                );

                puckColor = ContextCompat.getColor(
                        getContext(),
                        R.color.classic_puck
                );
                break;

            case ARCTIC:
            default:
                fieldColor = ContextCompat.getColor(
                        getContext(),
                        R.color.arctic_field
                );

                puckColor = ContextCompat.getColor(
                        getContext(),
                        R.color.arctic_puck
                );
                break;
        }

        fieldPaint.setColor(fieldColor);
        puckPaint.setColor(puckColor);
    }


    @Override
    protected void onSizeChanged(
            int width,
            int height,
            int oldWidth,
            int oldHeight
    ) {
        super.onSizeChanged(
                width,
                height,
                oldWidth,
                oldHeight
        );

        fieldRectangle.set(
                FIELD_MARGIN,
                FIELD_MARGIN,
                width - FIELD_MARGIN,
                height - FIELD_MARGIN
        );

        initializeGameEngine(width, height);
    }

    private void initializeGameEngine(int width, int height) {
        float puckRadius =
                Math.min(width, height) * 0.038f;

        float malletRadius =
                Math.min(width, height) * 0.065f;

        Puck puck = new Puck(puckRadius);

        Mallet topMallet = new Mallet(malletRadius);
        Mallet bottomMallet = new Mallet(malletRadius);

        Score score = new Score();

        gameEngine = new GameEngine(
                puck,
                topMallet,
                bottomMallet,
                score
        );

        gameEngine.setFieldSize(
                width,
                height,
                FIELD_MARGIN
        );
        gameEngine.resetPositions();
        gameEngine.startPuck(400f,300f);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        long currentTimeNanos = System.nanoTime();

        if (lastFrameTimeNanos != 0L && gameEngine != null) {
            float deltaTime =
                    (currentTimeNanos - lastFrameTimeNanos)
                            / 1_000_000_000f;
            deltaTime = Math.min(deltaTime, 0.05f);
            gameEngine.update(deltaTime);
            updateBottomMalletFromSensor();
        }

        lastFrameTimeNanos = currentTimeNanos;

        drawField(canvas);
        drawGoals(canvas);
        drawFieldMarkings(canvas);
        drawPlayers(canvas);
        drawPuck(canvas);

        canvas.drawText(
                "Tilt X: " + String.format("%.1f", sensorTiltX),
                40f,
                80f,
                debugTextPaint
        );

        canvas.drawText(
                "Tilt Y: " + String.format("%.1f", sensorTiltY),
                40f,
                125f,
                debugTextPaint
        );

        postInvalidateOnAnimation();
    }

    private void drawField(Canvas canvas) {
        canvas.drawRoundRect(
                fieldRectangle,
                FIELD_CORNER_RADIUS,
                FIELD_CORNER_RADIUS,
                fieldPaint
        );

        canvas.drawRoundRect(
                fieldRectangle,
                FIELD_CORNER_RADIUS,
                FIELD_CORNER_RADIUS,
                linePaint
        );
    }

    private void drawFieldMarkings(Canvas canvas) {
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        /*
         * Középvonal
         */
        canvas.drawLine(
                FIELD_MARGIN,
                centerY,
                getWidth() - FIELD_MARGIN,
                centerY,
                linePaint
        );

        /*
         * Középkör
         */
        float centerCircleRadius =
                Math.min(getWidth(), getHeight()) * 0.13f;

        canvas.drawCircle(
                centerX,
                centerY,
                centerCircleRadius,
                linePaint
        );

        /*
         * Középpont
         */
        Paint centerPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerPointPaint.setColor(linePaint.getColor());
        centerPointPaint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(
                centerX,
                centerY,
                7f,
                centerPointPaint
        );
    }

    private void drawGoals(Canvas canvas) {
        float goalWidth = getWidth() * 0.38f;
        float goalDepth = getHeight() * 0.045f;

        float goalLeft = (getWidth() - goalWidth) / 2f;
        float goalRight = goalLeft + goalWidth;

        /*
         * Felső kapu
         */
        RectF topGoal = new RectF(
                goalLeft,
                FIELD_MARGIN,
                goalRight,
                FIELD_MARGIN + goalDepth
        );

        /*
         * Alsó kapu
         */
        RectF bottomGoal = new RectF(
                goalLeft,
                getHeight() - FIELD_MARGIN - goalDepth,
                goalRight,
                getHeight() - FIELD_MARGIN
        );

        canvas.drawRoundRect(
                topGoal,
                14f,
                14f,
                goalPaint
        );

        canvas.drawRoundRect(
                topGoal,
                14f,
                14f,
                goalFramePaint
        );

        canvas.drawRoundRect(
                bottomGoal,
                14f,
                14f,
                goalPaint
        );

        canvas.drawRoundRect(
                bottomGoal,
                14f,
                14f,
                goalFramePaint
        );
    }

    private void drawPlayers(Canvas canvas) {
        if (gameEngine == null) {
            return;
        }

        Mallet topMallet =
                gameEngine.getTopMallet();

        Mallet bottomMallet =
                gameEngine.getBottomMallet();

        drawMallet(
                canvas,
                topMallet.getX(),
                topMallet.getY(),
                topMallet.getRadius(),
                topMalletPaint
        );

        drawMallet(
                canvas,
                bottomMallet.getX(),
                bottomMallet.getY(),
                bottomMallet.getRadius(),
                bottomMalletPaint
        );
    }

    private void drawMallet(
            Canvas canvas,
            float centerX,
            float centerY,
            float radius,
            Paint paint
    ) {
        canvas.drawCircle(
                centerX,
                centerY,
                radius,
                paint
        );

        Paint malletOutlinePaint =
                new Paint(Paint.ANTI_ALIAS_FLAG);

        malletOutlinePaint.setColor(Color.WHITE);
        malletOutlinePaint.setStyle(Paint.Style.STROKE);
        malletOutlinePaint.setStrokeWidth(5f);

        canvas.drawCircle(
                centerX,
                centerY,
                radius,
                malletOutlinePaint
        );

        /*
         * Belső kör, hogy jobban hasonlítson
         * egy léghokiütőre.
         */
        malletOutlinePaint.setStrokeWidth(3f);

        canvas.drawCircle(
                centerX,
                centerY,
                radius * 0.48f,
                malletOutlinePaint
        );
    }

    private void drawPuck(Canvas canvas) {
        if (gameEngine == null) {
            return;
        }

        Puck puck = gameEngine.getPuck();

        canvas.drawCircle(
                puck.getX(),
                puck.getY(),
                puck.getRadius(),
                puckPaint
        );

        Paint puckOutlinePaint =
                new Paint(Paint.ANTI_ALIAS_FLAG);

        puckOutlinePaint.setColor(Color.WHITE);
        puckOutlinePaint.setStyle(Paint.Style.STROKE);
        puckOutlinePaint.setStrokeWidth(4f);

        canvas.drawCircle(
                puck.getX(),
                puck.getY(),
                puck.getRadius(),
                puckOutlinePaint
        );
    }

    private void initializeSensorInput(Context context) {
        sensorInputController =
                new SensorInputController(
                        context,
                        (tiltX, tiltY) -> {
                            sensorTiltX = tiltX;
                            sensorTiltY = tiltY;
                        }
                );
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (sensorInputController != null) {
            sensorInputController.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (sensorInputController != null) {
            sensorInputController.stop();
        }

        super.onDetachedFromWindow();
    }

    private void updateBottomMalletFromSensor() {
        if (gameEngine == null) {
            return;
        }

        float tiltX = applyDeadZone(sensorTiltX);
        float tiltY = applyDeadZone(sensorTiltY);

        float normalizedX = clamp(
                tiltX / MAX_TILT_DEGREES,
                -1f,
                1f
        );

        float normalizedY = clamp(
                tiltY / MAX_TILT_DEGREES,
                -1f,
                1f
        );

        normalizedY = -normalizedY;

        /*
         * Az X irány megfordítása.
         * Balra döntéskor balra kell mozognia.
         */

        Mallet bottomMallet =
                gameEngine.getBottomMallet();

        float radius = bottomMallet.getRadius();

        float minimumX =
                FIELD_MARGIN + radius;

        float maximumX =
                getWidth() - FIELD_MARGIN - radius;

        float minimumY =
                getHeight() / 2f + radius;

        float maximumY =
                getHeight() - FIELD_MARGIN - radius;

        float centerX =
                (minimumX + maximumX) / 2f;

        float centerY =
                (minimumY + maximumY) / 2f;

        float horizontalRange =
                (maximumX - minimumX) / 2f;

        float verticalRange =
                (maximumY - minimumY) / 2f;

        float targetX =
                centerX + normalizedX * horizontalRange;

        float targetY =
                centerY + normalizedY * verticalRange;

        /*
         * Nem ugrik azonnal a célhelyre,
         * hanem fokozatosan közelít hozzá.
         */
        float smoothedX =
                bottomMallet.getX()
                        + (targetX - bottomMallet.getX())
                        * MALLET_SMOOTHING;

        float smoothedY =
                bottomMallet.getY()
                        + (targetY - bottomMallet.getY())
                        * MALLET_SMOOTHING;

        gameEngine.setBottomMalletPosition(
                smoothedX,
                smoothedY
        );
    }

    private float applyDeadZone(float value) {
        if (Math.abs(value) < SENSOR_DEAD_ZONE) {
            return 0f;
        }

        return value;
    }

    private float clamp(
            float value,
            float minimum,
            float maximum
    ) {
        return Math.max(
                minimum,
                Math.min(value, maximum)
        );
    }
}