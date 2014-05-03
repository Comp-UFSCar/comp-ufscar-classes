# -*- coding: utf-8 -*-
__author__ = "Sergey Karakovskiy, sergey at idsia fullstop ch"
__date__ = "$August 26, 2010 1:33:34 PM$"

from marioagent import MarioAgent

class ForwardJumpingAgent(MarioAgent):
    """ In fact the Python twin of the
        corresponding Java ForwardJumpingAgent.
    """
    action = None
    actionStr = None
    KEY_JUMP = 3
    KEY_SPEED = 4
    #levelScene = None
    mayMarioJump = None
    isMarioOnGround = None
    marioFloats = None
    enemiesFloats = None
    isEpisodeOver = False
    marioState = None
    
    def getName(self):
        return self.agentName

    def reset(self):
        self.action = [0, 0, 0, 0, 0, 0]
        self.action[1] = 1
        self.action[self.KEY_SPEED] = 1
        self.isEpisodeOver = False
        
    def __init__(self):
        """Constructor"""
        self.reset()
        self.actionStr = ""
        self.agentName = "Python Forward Jumping Agent"
        
    def getAction(self):
        """ Possible analysis of current observation and sending an action back
        """
	#print "M: mayJump: %s, onGround: %s, level[11,12]: %d, level[11,13]: %d, jc: %d" % (self.mayMarioJump, self.isMarioOnGround, self.levelScene[11,12], self.levelScene[11,13], self.trueJumpCounter)
	if (self.isEpisodeOver):
	    return (1, 1, 1, 1, 1, 1)
           
        self.action[self.KEY_SPEED] = self.action[self.KEY_JUMP] =  self.mayMarioJump or not self.isMarioOnGround;

        t = tuple(self.action)
        return t

    def integrateObservation(self, squashedObservation, squashedEnemies, marioPos, enemiesPos, marioState):
        """This method stores the observation inside the agent"""
        #print "Py: got observation::: squashedObservation: \n", squashedObservation
        #print "Py: got observation::: squashedEnemies: \n", squashedEnemies
        #print "Py: got observation::: marioPos: \n", marioPos
        #print "Py: got observation::: enemiesPos: \n", enemiesPos
        #print "Py: got observation::: marioState: \n", marioState
        #a = numpy.array(squashedObservation)
        #row = 19
        #col = 19
        #a.resize((row,col))
        #print "\n a== \n", a
        #levelScene = a
        #enemiesObservation = numpy.array(squashedEnemies)
        #enemiesObservation.resize((row,col))
        self.marioFloats = marioPos
        self.enemiesFloats = enemiesPos
        self.mayMarioJump = marioState[3]
        self.isMarioOnGround = marioState[2]
        #self.levelScene = levelScene
        self.marioState = marioState[1]
        #self.printLevelScene()

    def setObservationDetails(self, rfWidth, rfHeight, egoRow, egoCol):
        self.receptiveFieldWidth = rfWidth
        self.receptiveFieldHeight = rfHeight
        self.marioEgoRow = egoRow;
        self.marioEgoCol = egoCol;
