package flogiston.cg.labs

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import kotlin.math.absoluteValue

class Modele : ApplicationAdapter() {
    private lateinit var shapeRenderer : ShapeRenderer


    override fun create() {
        super.create()
        shapeRenderer = ShapeRenderer()
    }

    override fun render() {
        super.render()
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        drawOffsetRhombus(8)
    }

    override fun dispose() {
        super.dispose()
        shapeRenderer.dispose()
    }

    private fun drawOffsetRhombus(numRhombes : Int) {
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
    }

    companion object {
        const val WHOLE_CIRCLE_ANGLE = 360.0f
    }
}