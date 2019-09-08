package flogiston.cg.labs

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.TimeUtils
import flogiston.cg.labs.domain.Coordinates
import flogiston.cg.labs.domain.Primitive
import flogiston.cg.labs.domain.SpecialEffect

class Modele : ApplicationAdapter() {
    private lateinit var shapeRenderer : ShapeRenderer
    private var initialTimeMoment = -1L
    private var lastFrameTimeMoment = -1L
    private lateinit var centerOfScreen : Coordinates
    private lateinit var specialEffect : SpecialEffect


    override fun create() {
        super.create()
        initialTimeMoment = TimeUtils.nanoTime()
        centerOfScreen = Coordinates(Gdx.graphics.width / 2.0f, Gdx.graphics.height / 2.0f)
        shapeRenderer = ShapeRenderer()
        val primitives = mutableListOf<Primitive>()
        var angle = 0.0f
        val sector = WHOLE_CIRCLE_ANGLE / NUM_OF_PRIMITIVES
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
            angle += sector
        }
        specialEffect = SpecialEffect(primitives, 12, 700000000)
    }

    override fun render() {
        super.render()
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        drawSpecialEffect(centerOfScreen, specialEffect)
        //drawOffsetRhombus(8)
    }

    override fun dispose() {
        super.dispose()
        shapeRenderer.dispose()
    }

    private fun drawSpecialEffect(position : Coordinates, specialEffect: SpecialEffect) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = specialEffect.primitives.first().color
        val sector = WHOLE_CIRCLE_ANGLE / NUM_OF_PRIMITIVES
        val fractialPeriodPart = (TimeUtils.nanoTime() - initialTimeMoment).toFloat()/specialEffect.period % 1.0f
        val fractialFramePart = (TimeUtils.nanoTime() - lastFrameTimeMoment).toFloat() / specialEffect.period
        val offsetAngle = sector * fractialFramePart
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

    /*private fun drawOffsetRhombus(numRhombes : Int) {
        val centerOfScreen = Pair(Gdx.graphics.width / 2.0f, Gdx.graphics.height / 2.0f)
        val rotateAround = Pair(0.0f, 0.0f)
        val side = Gdx.graphics.width / 4.0f
        val scale = 1.0f
        val scaleX = scale
        val scaleY = scale
        var angle = 0.0f
        val sector : Float = WHOLE_CIRCLE_ANGLE / numRhombes
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.BLUE
        for (i in 0..numRhombes - 1) {
            shapeRenderer.rect(
                    centerOfScreen.first,
                    centerOfScreen.second,
                    rotateAround.first,
                    rotateAround.second,
                    side,
                    side,
                    scaleX,
                    scaleY,
                    angle
            )
            angle += sector
        }
        shapeRenderer.end()
    }*/

    companion object {
        const val WHOLE_CIRCLE_ANGLE = 360.0f
        const val NUM_OF_PRIMITIVES = 12
    }
}