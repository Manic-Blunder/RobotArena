import gameObjects.core.Arena
import gameObjects.core.Team.*

fun main() {
    GameFrame

    val team1 = ZOONER
    val team2 = MAGENTA
    val maxGenomes = 240

    val neats = listOf(team1.newNeat(maxGenomes, 20), team2.newNeat(maxGenomes, 20))
    neats.forEach { it.evolve() }
    val runningScoreBoard = mutableMapOf(Pair(team1, 0), Pair(team2, 0))

    var count = 0

    while (true) {
        count++
        val arenas = (neats[0].genomes zip neats[1].genomes).chunked(4).map { genomePairs -> Arena(genomePairs, team1, team2) }

        GameScreen.arena = arenas.maxByOrNull { it.doods.maxByOrNull{ dood -> dood.genome.score }?.genome?.score ?: 0.0 }
        while (arenas.any(Arena::active)) {
            arenas.forEach(Arena::update)
            GameFrame.repaint()
        }

        arenas.forEach { arena ->
            val winner = arena.winner
            val score1 = arena.scoreBoard[team1] ?: 0
            val score2 = arena.scoreBoard[team2] ?: 0
            winner?.let { runningScoreBoard[it] = runningScoreBoard[it]?.plus(1) ?: 0 }
            arena.doods.forEach {
                if (it.team == winner) it.onWin(Pair(score1, score2)) else it.onLoss(Pair(score1, score2))
                it.recalculateGenomeScore()
            }
        }

        neats[0].printSpecies()
//        println("Gen $count: [${team1.teamName}: ${runningScoreBoard[team1] ?: 0} | ${runningScoreBoard[team2] ?: 0} :${team2.teamName}]")

        neats.forEach { it.evolve() }
    }
}