/**
 * Created by igotti on 04.02.17.
 */

fun log(x: Double) = Math.log(x)
fun exp(x: Double) = Math.exp(x)
fun abs(x: Double) = Math.abs(x)

fun L(alpha: Double, beta: Double, Lmax: Double, Q: Double) : Double {
    return Lmax * (1 - beta * exp(-alpha * Q))
}

fun Q(alpha: Double, beta: Double, Lmax: Double, L: Double) : Double {
    return (log(Lmax * beta / (Lmax - L))) / alpha
}

var alpha = 0.0095
var beta = 1.0
var Lmax = 2000.0

val Ls0 = arrayOf(201.6, 381.6, 581.6, 1007.0)
val Ls1 = arrayOf(187.1, 358.8, 539.4, 981.4)

fun inRange(range: List<Double>, worker: (Double) -> Unit) {
    var start = range[0]
    val end = range[1]
    val delta = range[2]
    while (start < end) {
        worker(start)
        start += delta
    }
}

fun main(args: Array<String>) {
    val alphaRange = listOf(0.0, 0.5, 0.001)
    val betaRange = listOf(0.9, 1.1, 0.1)
    val lmaxRange = listOf(100.0, 3000.0, 0.1)
    var minDelta = 100000.0
    var bestLmax = 0.0
    var bestAlpha = 0.0
    var bestBeta = 0.0
    val Ls = Ls1
    inRange(alphaRange, { alpha ->
        Unit
        inRange(betaRange, { beta ->
            inRange(lmaxRange, { Lmax ->
                Unit
                var delta = 0.0
                var Q = 1.0
                for (a in Ls) {
                    val l = L(alpha, beta, Lmax, Q)
                    delta += abs(a - l)
                    Q *= 2
                }
                if (delta < minDelta) {
                    minDelta = delta
                    bestAlpha = alpha
                    bestBeta = beta
                    bestLmax = Lmax
                }
            })
        })
    })
    println(">>> alpha=${bestAlpha} beta=${bestBeta} Lmax=${bestLmax} delta=${minDelta}")
    var Q = 1.0
    for (a in Ls) {
        val l = L(bestAlpha, bestBeta, bestLmax, Q)
        println("q=${Q} L=${l} exp=${a} conc=${Q(bestAlpha, bestBeta, bestLmax, a)}")
        Q *= 2
    }
}
/*
LS0
>>> alpha=0.1520000000000001 beta=1.0 Lmax=1431.1999999997954 delta=77.06877033560659
q=1.0 L=201.8159726032753 exp=201.6 conc=0.9988443419088588
q=2.0 L=375.17352793586053 exp=381.6 conc=2.0401586447680042
q=4.0 L=651.9993923279396 exp=581.6 conc=3.4309400112298194
q=8.0 L=1006.9730666597478 exp=1007.0 conc=8.000417697791223

LS1
>>> alpha=0.1340000000000001 beta=1.0 Lmax=1492.19999999974 delta=87.78453876446531
q=1.0 L=187.1367055988724 exp=187.1 conc=0.9997901109250505
q=2.0 L=350.8046090382454 exp=358.8 conc=2.052459524641419
q=4.0 L=619.1377841383472 exp=539.4 conc=3.3477746417643433
q=8.0 L=981.3853419345089 exp=981.4 conc=8.00021414835346
 */