package flogiston.cg.labs.domain

class SpecialEffect (
        val primitives : List<Primitive>,
        var numOfPrimitives : Int,
        var period : Long
){
    val maxIncrease : Float = primitives.first().side / 2
}