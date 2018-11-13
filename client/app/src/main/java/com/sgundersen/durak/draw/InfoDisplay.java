package com.sgundersen.durak.draw;

import com.sgundersen.durak.draw.gl.GLShaderProgram;
import com.sgundersen.durak.draw.gl.GLTexture;
import com.sgundersen.durak.match.MatchClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InfoDisplay {

    private final MatchClient matchClient;

    private final Label youAreAttacking = new Label("Attack your opponent!", 192);
    private final Label youAreDefending = new Label("Defend yourself!", 192);

    private final Label victory = new Label("You are victorious!", 320);
    private final Label defeat = new Label("You were defeated...", 320);
    private final Label drawLabel = new Label("Draw!", 320);

    private final Rectangle rectangle = new Rectangle();
    private final GLTexture background = new GLTexture(0xBB111111);
    private final GLTexture timer = new GLTexture(0xAA11FF22);
    private final Transform backgroundTransform = new Transform();
    private final Transform timerTransform = new Transform();

    public void update(OrthoCamera camera) {
        youAreAttacking.update(camera.width() / 2.0f, 24.0f);
        youAreDefending.update(camera.width() / 2.0f, 24.0f);
        backgroundTransform.size.set(camera.width(), 144.0f);
        timerTransform.size.set(camera.width(), 16.0f);
        timerTransform.position.y = backgroundTransform.size.y - timerTransform.size.y / 2.0f;
        victory.update(camera.width() / 2.0f, camera.height() / 3.0f);
        defeat.update(camera.width() / 2.0f, camera.height() / 3.0f);
        drawLabel.update(camera.width() / 2.0f, camera.height() / 3.0f);
    }

    public void draw(GLShaderProgram shaderProgram) {
        shaderProgram.setModel(backgroundTransform.getMatrix());
        background.bind();
        rectangle.bind();
        rectangle.draw();
        shaderProgram.setModel(timerTransform.getMatrix());
        timer.bind();
        rectangle.draw();
        if (matchClient.getState().isAttacking()) {
            youAreAttacking.draw(shaderProgram);
        } else if (matchClient.getState().isDefending()) {
            youAreDefending.draw(shaderProgram);
        }
        switch (matchClient.getState().getOutcome()) {
            case NotYetDecided:
                break;
            case Victory:
                victory.draw(shaderProgram);
                break;
            case Defeat:
                defeat.draw(shaderProgram);
                break;
            case Draw:
                drawLabel.draw(shaderProgram);
                break;
            default:
                break;
        }
    }

}
