__author__="Sergey Karakovskiy"
__date__ ="$Mar 18, 2010 10:48:28 PM$"

class Inspectable(object):
    """ All derived classes gains the ability to print the names and values of all their fields"""
    def __repr__(self):
        return '<%s: %s>' % (self.__class__.__name__,
            dict([(x,y) for (x,y) in self.__dict__.items() if not x.startswith('_')]) )


class EvaluationInfo(Inspectable):
    def __init__(self, evInfo):
        print "widthCells = ", evInfo[0]
        print "widthPhys  = ", evInfo[1]
        print "flowersDevoured = ", evInfo[2]
        print "killsByFire = ", evInfo[3]
        print "killsByShell = ", evInfo[4]
        print "killsByStomp = ",  evInfo[5]
        print "killsTotal = ", evInfo[6]
        print "marioMode = ", evInfo[7]
        print "marioStatus = ", evInfo[8]
        print "mushroomsDevoured = ", evInfo[9]
        print "marioCoinsGained = ", evInfo[10]
        print "timeLeft = ", evInfo[11]
        print "timeSpent = ", evInfo[12]
        print "hiddenBlocksFound = ", evInfo[13]

        self.widthCells = evInfo[0]
        self.widthPhys = evInfo[1]
        self.flowersDevoured = evInfo[2]
        self.killsByFire = evInfo[3]
        self.killsByShell = evInfo[4]
        self.killsByStomp = evInfo[5]
        self.killsTotal = evInfo[6]
        self.marioMode = evInfo[7]
        self.marioStatus = evInfo[8]
        self.mushroomsDevoured = evInfo[9]
        self.marioCoinsGained = evInfo[10]
        self.timeLeft = evInfo[11]
        self.timeSpent = evInfo[12]
        self.hiddenBlocksFound = evInfo[13]

