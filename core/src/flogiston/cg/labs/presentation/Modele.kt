package flogiston.cg.labs.presentation

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport
import flogiston.cg.labs.domain.Coordinates
import flogiston.cg.labs.domain.Primitive
import flogiston.cg.labs.domain.SpecialEffect

class Modele : ApplicationAdapter() {
    private var drawObject : (position : Coordinates) -> Unit = this::drawRhombus
    private lateinit var shapeRenderer : ShapeRenderer
    private lateinit var stage : Stage
    private var initialTimeMoment = -1L
    private var lastFrameTimeMoment = -1L
    private lateinit var centerOfScreen : Coordinates
    private lateinit var specialEffect : SpecialEffect


    override fun create() {
        super.create()
        initialTimeMoment = TimeUtils.nanoTime()
        stage = Stage(ScreenViewport())
        Gdx.input.inputProcessor = stage
        centerOfScreen = Coordinates(Gdx.graphics.width / 2.0f, Gdx.graphics.height / 2.0f)
        shapeRenderer = ShapeRenderer()
        val btnSkin = Skin(Gdx.files.internal("freezing/freezing-ui.json"))
        val buttonWidth = Gdx.graphics.width / 3.0f
        val buttonHight = Gdx.graphics.height / 16.0f
        val primitiveBtn = TextButton("primitive", btnSkin)
        primitiveBtn.setPosition(0.0f, 0.0f)
        primitiveBtn.setSize(buttonWidth, buttonHight)
        primitiveBtn.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                drawObject = this@Modele::drawRhombus
                return true
            }
        })
        val patternBtn = TextButton("pattern", btnSkin)
        patternBtn.setPosition(buttonWidth, 0.0f)
        patternBtn.setSize(buttonWidth, buttonHight)
        patternBtn.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                drawObject = this@Modele::drawPattern
                return true
            }
        })
        val specialEffectBtn = TextButton("specialEffect", btnSkin)
        specialEffectBtn.setPosition(buttonWidth + buttonWidth, 0.0f)
        specialEffectBtn.setSize(buttonWidth, buttonHight)
        specialEffectBtn.addListener(object : InputListener(){
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                drawObject = this@Modele::drawSpecialEffect
                return true
            }
        })
        stage.addActor(primitiveBtn)
        stage.addActor(patternBtn)
        stage.addActor(specialEffectBtn)
        val primitives = mutableListOf<Primitive>()
        var angle = 0.0f
        for (i in 0..NUM_OF_PRIMITIVES - 1) {
            primitives += Primitive(
                    Gdx.graphics.width / 4.0f,
                    Coordinates(0.0f, 0.0f),
                    1.0f,
                    1.0f,
                    angle,
                    0.0f,
                    Color.BLUE
                    )
            angle += SECTOR
        }
        specialEffect = SpecialEffect(primitives, 12, 700000000)
    }

    override fun render() {
        super.render()
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act()
        stage.draw()
        drawObject(centerOfScreen)
    }

    override fun dispose() {
        super.dispose()
        shapeRenderer.dispose()
        stage.dispose()

    }

    private fun drawSpecialEffect(position : Coordinates) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = specialEffect.primitives.first().color
        val fractialPeriodPart = (TimeUtils.nanoTime() - initialTimeMoment).toFloat()/specialEffect.period % 1.0f
        val fractialFramePart = (TimeUtils.nanoTime() - lastFrameTimeMoment).toFloat() / specialEffect.period
        val offsetAngle = SECTOR * fractialFramePart
        for (i in 0..specialEffect.numOfPrimitives - 1) {
            val primitive = specialEffect.primitives[i]
            if (i % 2 == 0) {
                primitive.rotateAngle = (primitive.rotateAngle + offsetAngle) % 360.0f
                primitive.increase = if (fractialPeriodPart <= 0.5f)
                    specialEffect.maxIncrease * (0.25f - fractialPeriodPart * fractialPeriodPart)
                else
                    specialEffect.maxIncrease * (2 * fractialPeriodPart - fractialPeriodPart * fractialPeriodPart - 0.75f)
            } else {
                primitive.rotateAngle = (primitive.rotateAngle - offsetAngle) % 360.0f
                primitive.increase = specialEffect.maxIncrease * (fractialPeriodPart - fractialPeriodPart * fractialPeriodPart)
            }
            shapeRenderer.rect(
                    position.x,
                    position.y,
                    primitive.origin.x,
                    primitive.origin.y,
                    primitive.side + primitive.increase,
                    primitive.side + primitive.increase,
                    primitive.scaleX,
                    primitive.scaleY,
                    primitive.rotateAngle
            )
        }
        lastFrameTimeMoment = TimeUtils.nanoTime()
        shapeRenderer.end()
    }

    private fun drawPattern(position : Coordinates) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.BLUE
        for (i in 0..specialEffect.numOfPrimitives - 1) {
            val primitive = specialEffect.primitives[i]
            shapeRenderer.rect(
                    position.x,
                    position.y,
                    primitive.origin.x,
                    primitive.origin.y,
                    primitive.side,
                    primitive.side,
                    primitive.scaleX,
                    primitive.scaleY,
                    primitive.rotateAngle
            )
        }
        shapeRenderer.end()
    }

    private fun drawRhombus(position : Coordinates) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.BLUE
        val side = Gdx.graphics.width / 4.0f
        shapeRenderer.rect(
                centerOfScreen.x,
                centerOfScreen.y,
                0.0f,
                0.0f,
                side,
                side,
                1.0f,
                1.0f,
                315.0f
        )
        shapeRenderer.end()
    }

    companion object {
        const val WHOLE_CIRCLE_ANGLE = 360.0f
        const val NUM_OF_PRIMITIVES = 12
        const val SECTOR = WHOLE_CIRCLE_ANGLE / NUM_OF_PRIMITIVES
    }
}