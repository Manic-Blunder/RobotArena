package gameObjects.core

import brains.neat.Genome
import fuzzyMath.shapes.Vector2
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.lang.Exception
import kotlin.random.Random

class Arena(genomes: List<Pair<Genome, Genome>>, private val team1: Team, private val team2: Team) {
    val scoreBoard = mutableMapOf(Pair(team1, 0), Pair(team2, 0))
    val winner
        get() = if (scoreBoard[team1] != scoreBoard[team2]) scoreBoard.maxByOrNull { it.value }?.key else null
    val bestDude
        get() = doods.filter { it.team == team1 }.maxByOrNull { it.genome.score }

    var ticks = 1000
    var onKillTickBonus = 100

    val width = GameScreen.width.toDouble()
    val height = GameScreen.height.toDouble() - 300

    val structures: ArrayList<Structure> = arrayListOf(
        Structure(Vector2(0.0, 0.0), Vector2(width, WALL_THICKNESS)),
        Structure(Vector2(0.0, 0.0), Vector2(WALL_THICKNESS, height)),
        Structure(Vector2(0.0, height - WALL_THICKNESS), Vector2(width, WALL_THICKNESS)),
        Structure(Vector2(width - WALL_THICKNESS, 0.0), Vector2(WALL_THICKNESS, height)),
    )

    private val team1StartIndex = width / 2 - 100
    private val team2StartIndex = width / 2 + 100
    val bullets: ArrayList<Bullet> = arrayListOf()
    val doods: ArrayList<Dood> = arrayListOf()

    init {
        fun randomWide(minX: Int, minY: Int, maxX: Int, maxY: Int): Structure {
            val position =
                Point(minX + Random.nextInt(maxX), minY + Random.nextInt((maxY - WALL_THICKNESS * 3).toInt()))
            return Structure(Vector2.fromPoint(position), Vector2(WALL_THICKNESS * 2.0, WALL_THICKNESS * 6.0))
        }

        fun randomTall(minX: Int, minY: Int, maxX: Int, maxY: Int): Structure {
            val position =
                Point(minX + Random.nextInt((maxX - WALL_THICKNESS * 3).toInt()), minY + Random.nextInt(maxY))
            return Structure(Vector2.fromPoint(position), Vector2(WALL_THICKNESS * 6.0, WALL_THICKNESS * 2.0))
        }

        repeat(3) {
            structures.add(
                randomWide(
                    WALL_THICKNESS.toInt(),
                    WALL_THICKNESS.toInt(),
                    (width - WALL_THICKNESS * 2).toInt(),
                    (height - WALL_THICKNESS * 2).toInt()
                )
            )
        }
        repeat(3) {
            structures.add(
                randomTall(
                    WALL_THICKNESS.toInt(),
                    WALL_THICKNESS.toInt(),
                    (width - WALL_THICKNESS * 2).toInt(),
                    (height - WALL_THICKNESS * 2).toInt()
                )
            )
        }

        genomes.forEachIndexed { i, it ->
            doods.add(team1.newDood(this, it.first, Vector2(team1StartIndex, height / 2 - 75.0 + i * 50)))
            doods.add(team2.newDood(this, it.second, Vector2(team2StartIndex, height / 2 - 75.0 + i * 50)))
        }
    }

    fun render(g: Graphics) {
        if (GameScreen.renderSpecial) {
            bestDude?.specialRender(g)
        }

        if (GameScreen.renderArena) {
            bullets.toList().forEach { it.render(g) }
            doods.toList().forEachIndexed { i, it -> it.render(g) }
            structures.toList().forEach { it.render(g) }
        }

        g.drawString(ticks.toString(), 50, 50)
        g.drawString(
            "${team1.teamName} : ${scoreBoard[team1]} | ${scoreBoard[team2]} : ${team2.teamName}",
            (width / 2 - 25).toInt(),
            50
        )
    }


    fun update(): Boolean {
        if (active()) {
            doods.forEach { it.processNeuralOutputs(it.genome.calculate(it.neuralInputs)) }
            bullets.forEach { it.update() }
            doods.forEach { it.update() }
            bullets.removeIf { it.isOutOfBounds || it.isDead }
            ticks--
            return true
        }
        return false
    }

    fun active(): Boolean {
        return ticks > 0
    }

    fun onDoodKilled(killer: Dood, victim: Dood) {
        if (!victim.isDead) {
            victim.die()
            if (killer.team == victim.team) { // Killed a fren... :(
                scoreBoard[killer.team] = scoreBoard[killer.team]?.minus(1) ?: 0
                killer.onBetrayal(victim)
                victim.onBetrayed(killer)
            } else { // Killed an enemy! Woo :D
                // This is to make low-action matches faster, and more deadly matches longer
                ticks += onKillTickBonus
                onKillTickBonus--
                scoreBoard[killer.team] = scoreBoard[killer.team]?.plus(1) ?: 0
                killer.onKill(victim)
                victim.onKilled(killer)
            }
        }
    }

    companion object {
        private const val WALL_THICKNESS = 20.0
    }
}