package com.sgundersen.durak.draw.gl;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.sgundersen.durak.R;
import com.sgundersen.durak.core.match.MatchOutcome;
import com.sgundersen.durak.draw.BoutRenderer;
import com.sgundersen.durak.draw.CardRenderer;
import com.sgundersen.durak.draw.InfoDisplay;
import com.sgundersen.durak.draw.MyHandRenderer;
import com.sgundersen.durak.draw.OrthoCamera;
import com.sgundersen.durak.draw.OtherHandRenderer;
import com.sgundersen.durak.draw.Rectangle;
import com.sgundersen.durak.draw.TalonRenderer;
import com.sgundersen.durak.draw.Transform;
import com.sgundersen.durak.match.MatchClient;

import org.joml.Vector2f;
import org.joml.Vector4f;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GLMatchRenderer implements GLSurfaceView.Renderer {

    private GLShaderProgram shaderProgram;
    private OrthoCamera camera;

    private final Context context;

    private MyHandRenderer myHandRenderer;
    private OtherHandRenderer otherHandRenderer;
    private TalonRenderer talonRenderer;
    private BoutRenderer boutRenderer;
    private InfoDisplay infoDisplay;

    private GLTexture backgroundTexture;
    private Rectangle backgroundRectangle;

    @Getter
    private MatchClient matchClient;

    private boolean canInitializeGraphics = false;

    @Setter
    private boolean canInitializeMatch = false;

    private boolean isInitialized = false;

    public GLMatchRenderer(Context context) {
        this.context = context;
        matchClient = new MatchClient(this);
    }

    public void stop() {
        matchClient.stop();
    }

    private void initialize() {
        if (isInitialized || !canInitializeGraphics || !canInitializeMatch) {
            return;
        }
        CardRenderer cardRenderer = new CardRenderer(context, shaderProgram, 222, 323, 9, 6);
        myHandRenderer = new MyHandRenderer(cardRenderer, matchClient, camera);
        otherHandRenderer = new OtherHandRenderer(cardRenderer, matchClient, camera);
        talonRenderer = new TalonRenderer(cardRenderer, matchClient, camera);
        boutRenderer = new BoutRenderer(cardRenderer, matchClient, camera);
        backgroundTexture = new GLTexture(context, R.drawable.background);
        backgroundRectangle = new Rectangle();
        infoDisplay = new InfoDisplay(matchClient);
        isInitialized = true;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig config) {
        GLES30.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);
        camera = new OrthoCamera();
        shaderProgram = new GLShaderProgram(context, R.raw.vertex, R.raw.fragment, new String[]{"in_Position", "in_TexCoords"});
        canInitializeGraphics = true;
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
        camera.setSize(width, height);
        log.info("Surface changed: {}x{}", width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        matchClient.update();
        if (isInitialized) {
            update();
            draw();
        } else {
            initialize();
        }
    }

    private void update() {
        myHandRenderer.update();
        otherHandRenderer.update();
        talonRenderer.update();
        boutRenderer.update();
        infoDisplay.update(camera);
    }

    private void draw() {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        shaderProgram.bind();
        shaderProgram.setViewProjection(camera.getView(), camera.getProjection());
        shaderProgram.setColor(new Vector4f(1.0f));

        drawBackground();
        myHandRenderer.draw();
        otherHandRenderer.draw();
        talonRenderer.draw();
        boutRenderer.draw();
        infoDisplay.draw(shaderProgram);
    }

    private void drawBackground() {
        Transform backgroundTransform = new Transform();
        backgroundTransform.size.set(camera.width(), camera.height());
        shaderProgram.setModel(backgroundTransform.getMatrix());
        backgroundTexture.bind();
        backgroundRectangle.bind();
        backgroundRectangle.draw();
    }

    public void onTouchDown(Vector2f begin) {
        if (!isInitialized) {
            return;
        }
        if (!matchClient.isFinished()) {
            myHandRenderer.startDrag(begin);
            boutRenderer.startDrag(begin);
        }
    }

    public void onTouchMove(Vector2f begin, Vector2f end, Vector2f delta) {
        if (!isInitialized) {
            return;
        }
        if (!matchClient.isFinished()) {
            myHandRenderer.continueDrag(delta);
            boutRenderer.continueDrag(delta);
        }
    }

    public void onTouchUp(Vector2f begin, Vector2f end, Vector2f delta) {
        if (!isInitialized) {
            return;
        }
        if (!matchClient.isFinished()) {
            myHandRenderer.endDrag();
            boutRenderer.endDrag();
        }
    }

}
