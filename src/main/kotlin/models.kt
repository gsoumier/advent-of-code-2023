import kotlin.math.abs
import kotlin.math.absoluteValue

data class Coord(
    val x: Int,
    val y: Int,
) : Comparable<Coord> {

    fun to(direction: Direction, times: Int = 1) = Coord(
        x + times * direction.nX,
        y + times * direction.nY,
    )

    fun to(vector: Vector, times: Int = 1) = Coord(
        x + times * vector.nX,
        y + times * vector.nY,
    )

    fun distanceTo(other: Coord): Int {
        return abs(other.y - y) + abs(other.x - x)
    }

    override fun compareTo(other: Coord): Int {
        (y.absoluteValue + x.absoluteValue - other.y.absoluteValue - other.x.absoluteValue).takeUnless { it == 0 }?.let { return it }
        return 1000 * (y - other.y) + x - other.x
    }

    fun mainNeighbours() : List<Coord> {
       return Direction.cardinals().map { to(it) }
    }

    fun extendedNeighbours() : List<Coord> {
       return Direction.extended().map { to(it) }
    }
}

enum class Direction(val nX: Int, val nY: Int) {
    N(0, -1),
    E(1, 0),
    S(0, 1),
    W(-1, 0),
    NE(1, -1),
    SE(1, 1),
    SW(-1, 1),
    NW(-1, -1);

    fun opposite(): Direction {
        return when (this) {
            N -> S
            E -> W
            S -> N
            W -> E
            NE -> SW
            SE -> NW
            SW -> NE
            NW -> SE
        }
    }

    fun quarterClockwise(): Direction {
        return when (this) {
            N -> E
            E -> S
            S -> W
            W -> N
            NE -> SE
            SE -> SW
            SW -> NW
            NW -> NE
        }
    }

    fun quarterAntiClockwise(): Direction {
        return when (this) {
            N -> W
            W -> S
            S -> E
            E -> N
            NE -> NW
            NW -> SW
            SW -> SE
            SE -> NE
        }
    }

    companion object {
        fun extended() = entries

        fun cardinals() = horizontals() + verticals()

        fun interCardinals() = entries - cardinals()

        fun horizontals() = setOf(E, W)

        fun verticals() = setOf(N, S)
    }
}


data class NavigationPoint(val coord: Coord, val direction: Direction) {
    fun goStraight(): NavigationPoint = copy(coord = coord.to(direction))
    fun turnRight(): NavigationPoint = copy(direction = direction.quarterClockwise())
    fun turnLeft(): NavigationPoint = copy(direction = direction.quarterAntiClockwise())
}

data class Vector(val a: Coord, val b: Coord) {
    val nX: Int
        get() = b.x - a.x
    val nY: Int
        get() = b.y - a.y

    fun inverse(): Vector {
        return Vector(b, a)
    }
}





data class CharPoint(val coord: Coord, var value: Char){
    val intValue: Int
        get() {
            return value.digitToInt()
        }
}

class CharMap(
    val charPoints: List<CharPoint>,
) {
    val nbCols: Int = charPoints.maxOf { it.coord.x } + 1
    val nbLines: Int = charPoints.maxOf { it.coord.y } + 1
    val map = charPoints.associateBy { it.coord }

    operator fun get(coord: Coord, mapRepeatable: Boolean = false): CharPoint? {
        return map[coord.takeUnless { mapRepeatable } ?: coord.inInitialMap()]?.value?.let { CharPoint(coord, it) }
    }

    fun changeChar(coord: Coord, newVal: Char): CharMap {
        return CharMap(charPoints.map { if(it.coord == coord) it.copy(value = newVal) else it })
    }

    fun getCharSequence(from: Coord, dir: Direction, size: Int): List<Char>? {
        return (0..<size).map {
            this[from.to(dir, it)] ?: return null
        }.map { it.value }
    }

    fun getWord(from: Coord, dir: Direction, size: Int): String? {
        return getCharSequence(from, dir, size)?.joinToString("") { it.toString() }
    }

    fun Coord.inInitialMap(): Coord {
        return Coord(x.inInitialMap(nbCols), y.inInitialMap(nbLines))
    }

    fun Int.inInitialMap(size: Int): Int {
        val res = this % size
        if(res < 0)
            return res + size
        return res
    }
    fun Int.initialMapCoord(size: Int): Int {
        val res = this / size
        if(this % size < 0)
            return res - 1
        return res
    }

    fun find(predicate: (CharPoint) -> Boolean): CharPoint? {
        return charPoints.find(predicate)
    }

    fun neighboursInMap(coord: Coord, mapRepeatable: Boolean = false): List<Pair<Direction, CharPoint>> {
        return Direction.cardinals().mapNotNull { dir ->
            get(coord.to(dir), mapRepeatable)?.let { dir to it }
        }
    }

    fun mapCoord(it: Coord) : Coord {
        return Coord(it.x.initialMapCoord(nbCols), it.y.initialMapCoord(nbLines))
    }

    val end = nbLines - 1
    val cNW = Coord(0, 0)
    val cNE = Coord(0, end)
    val cSE = Coord(end, end)
    val cSW = Coord(end, 0)
    val corners = setOf(cNW, cNE, cSE, cSW)
    val midW = Coord(0, end / 2)
    val midN = Coord(end / 2, 0)
    val midE = Coord(end, end / 2)
    val midS = Coord(end / 2, end)
    val axis = setOf(midW, midN, midE, midS)
}



data class ZCoord(val coord: Coord, val z: Int)
data class ZCoordLong(val x: Long, val y: Long, val z: Long)

