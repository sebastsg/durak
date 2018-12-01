package com.sgundersen.durak.draw.gl;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.sgundersen.durak.R;
import com.sgundersen.durak.control.StateController;
import com.sgundersen.durak.draw.BackgroundRenderer;
import com.sgundersen.durak.draw.BoutRenderer;
import com.sgundersen.durak.draw.CardRenderer;
import com.sgundersen.durak.draw.InfoDisplay;
import com.sgundersen.durak.draw.MyHandRenderer;
import com.sgundersen.durak.draw.OrthoCamera;
import com.sgundersen.durak.draw.OtherHandRenderer;
import com.sgundersen.durak.draw.TalonRenderer;
import com.sgundersen.durak.control.MatchClient;
import com.sgundersen.durak.control.MatchController;

import org.joml.Vector2f;
import org.joml.Vector4f;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import lombok.Getter;

public class GLMatchRenderer implements GLSurfaceView.Renderer {

    private static Vector2f CARD_SIZE = new Vector2f(222.0f, 323.0f);

    private final Context context;

    @Getter
    private final StateController stateController;

    @Getter
    private MatchController matchController;

    private boolean surfaceCreated = false;
    private boolean isInitialized = false;

    private GLShaderProgram shaderProgram;
    private OrthoCamera camera;

    private MyHandRenderer myHandRenderer;
    private OtherHandRenderer otherHandRenderer;
    private TalonRenderer talonRenderer;
    private BoutRenderer boutRenderer;
    private InfoDisplay infoDisplay;
    private BackgroundRenderer backgroundRenderer;

    public GLMatchRenderer(Context context, StateController stateController) {
        this.context = context;
        this.stateController = stateController;
        camera = new OrthoCamera();
        matchController = new MatchController(stateController.getMatchClient(), camera, CARD_SIZE);
    }

    private void initialize() {
        MatchClient matchClient = stateController.getMatchClient();
        if (isInitialized || !surfaceCreated || !matchClient.isInitialized()) {
            return;
        }
        CardRenderer cardRenderer = new CardRenderer(context, shaderProgram, CARD_SIZE, 9, 6);
        myHandRenderer = new MyHandRenderer(cardRenderer, matchController.getMyHandController());
        otherHandRenderer = new OtherHandRenderer(cardRenderer, matchController.getOtherHandController());
        boutRenderer = new BoutRenderer(cardRenderer, matchController.getBoutController());
        talonRenderer = new TalonRenderer(cardRenderer, matchClient, camera);
        backgroundRenderer = new BackgroundRenderer(context);
        infoDisplay = new InfoDisplay(matchClient);
        isInitialized = true;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig config) {
        GLES30.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);
        shaderProgram = new GLShaderProgram(context, R.raw.vertex, R.raw.fragment, new String[]{"in_Position", "in_TexCoords"});
        surfaceCreated = true;
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
        camera.setSize(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        stateController.update();
        if (isInitialized) {
            update();
            draw();
        } else {
            initialize();
        }
    }

    private void update() {
        matchController.update();
        talonRenderer.update();
        infoDisplay.update(camera);
    }

    private void draw() {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        shaderProgram.bind();
        shaderProgram.setViewProjection(camera.getView(), camera.getProjection());
        shaderProgram.setColor(new Vector4f(1.0f));
        backgroundRenderer.draw(shaderProgram, camera);
        myHandRenderer.draw();
        otherHandRenderer.draw();
        talonRenderer.draw();
        boutRenderer.draw();
        infoDisplay.draw(shaderProgram);
    }

}
