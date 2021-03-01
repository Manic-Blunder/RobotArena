package gameObjects.core

import brains.neat.Genome
import brains.neat.Neat
import fuzzyMath.shapes.Vector2
import gameObjects.doods.*
import gameObjects.neats.*

enum class Team(val teamName: String) {
    //define team here, define neat algorithm, defind dood
    CYAN("Cyan") {
        override fun newDood(arena: Arena, genome: Genome, position: Vector2) = CyanDood(arena, genome, position)
        override fun newNeat(maxGenomes: Int, minGenomes: Int) = CyanNeat(maxGenomes, minGenomes)
    },
    ORANGE("Orange") {
        override fun newDood(arena: Arena, genome: Genome, position: Vector2) = OrangeDood(arena, genome, position)
        override fun newNeat(maxGenomes: Int, minGenomes: Int) = OrangeNeat(maxGenomes, minGenomes)
    },
    MAGENTA("Magenta") {
        override fun newDood(arena: Arena, genome: Genome, position: Vector2) = MagentaDood(arena, genome, position)
        override fun newNeat(maxGenomes: Int, minGenomes: Int) = MagentaNeat(maxGenomes, minGenomes)
    },
    GREEN("GreenPesterligs") {
        override fun newDood(arena: Arena, genome: Genome, position: Vector2) = GreenDood(arena, genome, position)
        override fun newNeat(maxGenomes: Int, minGenomes: Int) = GreenNeat(maxGenomes, minGenomes)
    },
    ALEX("Super Dork") {
        override fun newDood(arena: Arena, genome: Genome, position: Vector2) = AlexDood(arena, genome, position)
        override fun newNeat(maxGenomes: Int, minGenomes: Int) = AlexNeat(maxGenomes, minGenomes)
    },
    ZOONER("Super Awesome") {
        override fun newDood(arena: Arena, genome: Genome, position: Vector2) = ZoonerDood(arena, genome, position)
        override fun newNeat(maxGenomes: Int, minGenomes: Int) = ZoonerNeat(maxGenomes, minGenomes)
    },
    ;

    abstract fun newDood(arena: Arena, genome: Genome, position: Vector2): Dood
    abstract fun newNeat(maxGenomes: Int, minGenomes: Int) : Neat
}