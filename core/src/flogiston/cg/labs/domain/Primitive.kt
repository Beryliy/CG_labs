package flogiston.cg.labs.domain

import com.badlogic.gdx.graphics.Color

class Primitive (
        var side : Float,
        val origin : Coordinates,
        val scaleX : Float,
        val scaleY : Float,
        var rotateAngle : Float,
        var increase : Float,
        val color : Color
) {
}