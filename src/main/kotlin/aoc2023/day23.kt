package aoc2023

import AocRunner
import CharPoint
import Coord
import Direction
import InputType
import StringLineParser
import toCharMap
import kotlin.math.max

class Day23(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    23,
    StringLineParser,
    inputType
) {
    val charMap = lines.toCharMap()
    val startPoint = charMap[Coord(1, 0)]!!
    val end = Coord(nbCols - 2, nbLines - 1)
    val endPoint = charMap[end]!!

    val intersections = charMap.charPoints.filter { charMap.neighboursInMap(it.coord).filter { it.charPoint.value != '#' }.size > 2 } + startPoint + endPoint

    override fun partOne(): Long {
        return getLongestPathsForIntersection(
            startPoint,
            endPoint,
            emptySet(),
            intersections.associateWith { findConnections(it, true) }).inc().toLong()
    }

    private fun getNextIntersection(
        start: CharPoint,
        direction: Direction,
        visitedInter: Set<Coord>,
        slopeSlippery: Boolean,
        checkIntersection: Boolean = true,
    ): Triple<CharPoint, Int, Direction>? {
        var current = start
        var dir = direction
        var straightPathLength = 1
        var neighbours = possibleNeighbours(current, visitedInter, dir, slopeSlippery, checkIntersection)
        var singleNeighbour = neighbours.singleOrNull()
        while (singleNeighbour != null) {
            singleNeighbour.takeIf { it.charPoint.coord == end }?.let { return Triple(charMap[end]!!,straightPathLength, it.direction) }
            current = singleNeighbour.charPoint
            dir = singleNeighbour.direction
            straightPathLength++
            neighbours = possibleNeighbours(current, visitedInter, dir, slopeSlippery)
            singleNeighbour = neighbours.singleOrNull()
        }
        return Triple(current, straightPathLength, dir).takeIf { neighbours.isNotEmpty() }
    }

    private fun possibleNeighbours(
        start: CharPoint,
        visited: Set<Coord>,
        direction: Direction,
        slopeSlippery: Boolean,
        checkIntersection: Boolean = true,
    ) = charMap.neighboursInMap(start.coord)
        .filter { it.charPoint.value != '#' }
        .filter { it.direction != direction.opposite() }
        .filter { !checkIntersection || it.charPoint.coord !in visited }
        .filter { !slopeSlippery || it.charPoint.slopeDirection() != it.direction.opposite() }
        .filter { !slopeSlippery || start.slopeDirection()?.let { slopeDir -> slopeDir == it.direction } ?: true }

    override fun partTwo(): Long {
        return getLongestPathsForIntersection(
            startPoint,
            endPoint,
            emptySet(),
            intersections.associateWith { findConnections(it, false) }).inc().toLong()
    }

    private fun findConnections(point: CharPoint, slopeSlippery: Boolean): List<Pair<CharPoint, Int>> {
        return charMap.neighboursInMap(point.coord)
            .filter { it.charPoint.value != '#' }
            .mapNotNull { getNextIntersection(it.charPoint, it.direction, emptySet(), slopeSlippery, false) }
            .map { it.first to it.second }
    }

    private fun getLongestPathsForIntersection(
        start: CharPoint,
        end: CharPoint,
        visitedInter: Set<Coord> = emptySet(),
        connections: Map<CharPoint, List<Pair<CharPoint, Int>>>,
    ): Int {
        if(start == end){
            return 0
        }

        var maxDistance = Int.MIN_VALUE
        connections[start]?.filter {
            it.first.coord !in visitedInter
        }?.forEach {
            (neighbour, dist) -> maxDistance = max(maxDistance, dist + getLongestPathsForIntersection(neighbour, end, visitedInter + start.coord, connections))
        }

        return maxDistance
    }

}

private fun CharPoint.slopeDirection(): Direction? {
    return when (value) {
        '<' -> Direction.W
        '>' -> Direction.E
        '^' -> Direction.N
        'v' -> Direction.S
        else -> null
    }
}


fun main() {
    Day23().run()
}
