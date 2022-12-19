package day16.util

//class Path(var curValve: Valve, val openValves: ArrayList<Valve>, var remaining: Int, var pressureReleased: Int)
class Path(var curValve: Valve, val targetValves: Set<Valve>, val remaining: Int)