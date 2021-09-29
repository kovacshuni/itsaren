package com.hunorkovacs.itsaren

object Night extends App {

  def smileyTimes(n: Int): String = {
    var smileys = ""
    for (_ <- 1 to n) {
      smileys = smileys + ":)"
    }
    return smileys
  }

  val smileys: String = smileyTimes(15)
  println(smileys)

}
