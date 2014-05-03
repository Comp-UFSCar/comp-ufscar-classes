# -*- coding: utf-8 -*-
__author__ = "Sergey Karakovskiy, sergey at idsia fullstop ch"
__date__ = "$May 1, 2009 2:46:34 AM$"

from marioagent import MarioAgent

class ForwardAgent(MarioAgent):
    """ In fact the Python twin of the
        corresponding Java ForwardAgent.
    """
    action = None
    actionStr = None
    KEY_JUMP = 3
    KEY_SPEED = 4
    levelScene = None
    mayMarioJump = None
    isMarioOnGround = None
    marioFloats = None
    enemiesFloats = None
    isEpisodeOver = False
    marioState = None
    
    trueJumpCounter = 0;
    trueSpeedCounter = 0;

    """default values for observation details"""
    receptiveFieldWidth = 19
    receptiveFieldHeight = 19
    marioEgoRow = 9
    marioEgoCol = 9
    
    agentName = "AmiCo Python Forward Agent"


    def reset(self):
        self.isEpisodeOver = False
        self.trueJumpCounter = 0;
        self.trueSpeedCounter = 0;
        
    def __init__(self):
        """Constructor"""
        self.trueJumpCounter = 0
        self.trueSpeedCounter = 0
        self.action = [0, 0, 0, 0, 0, 0]
        self.action[1] = 1
        self.actionStr = ""
        self.agentName = "Python Forward Agent"

    def getReceptiveFieldCellValue(self, x, y):
        if (x < 0 or x >= self.marioEgoRow or y < 0 or y >= self.marioEgoCol):
            return 0
        return self.levelScene[x][y]
        
    def setObservationDetails(self, rfWidth, rfHeight, egoRow, egoCol):
        self.receptiveFieldWidth = rfWidth
        self.receptiveFieldHeight = rfHeight
        self.marioEgoRow = egoRow;
        self.marioEgoCol = egoCol;
        
    def _dangerOfGap(self):
        fromX = self.marioEgoRow
        fromY = self.marioEgoCol
        self.marioEgoRow = fromX + 1
        self.marioEgoCol = fromY + 1

        if (fromX > 3):
            fromX -= 2;

        for x in range(fromX, self.receptiveFieldWidth):
            f = True
            for y in range(fromY, self.receptiveFieldHeight):
                if  (self.getReceptiveFieldCellValue(y, x) != 0):
                    f = False
            if (f or self.getReceptiveFieldCellValue(self.marioEgoRow + 1, self.marioEgoCol) == 0 or \
                (self.marioState > 0 and \
                (self.getReceptiveFieldCellValue(self.marioEgoRow + 1, self.marioEgoCol - 1) != 0 or \
                self.getReceptiveFieldCellValue(self.marioEgoRow + 1, self.marioEgoCol) != 0))):
                return True
        return False

    def giveIntermediateReward(self, reward):
        pass

    def _a2(self):
        """ Interesting, sometimes very useful behaviour which might prevent falling down into a gap!
        Just substitue getAction by this method and see how it behaves.
        """
#        if (self.mayMarioJump):
#                    print "m: %d, %s, %s, 12: %d, 13: %d, j: %d" \
#            % (self.getReceptiveFieldCellValue(self.marioEgoRow + 2, self.marioEgoCol + 2), self.mayMarioJump, self.isMarioOnGround, \
#            self.getReceptiveFieldCellValue(self.marioEgoRow + 2, self.marioEgoCol + 3), self.getReceptiveFieldCellValue(self.marioEgoRow + 2, self.marioEgoCol + 3), self.trueJumpCounter)
#        else:
#            if self.levelScene == None:
#                print "Bad news....."
#            print "m: %d, 12: %d, 13: %d, j: %d" \
#                % (self.getReceptiveFieldCellValue(self.marioEgoRow + 2, self.marioEgoCol + 2), \
#                self.getReceptiveFieldCellValue(self.marioEgoRow + 2, self.marioEgoCol + 3), self.getReceptiveFieldCellValue(self.marioEgoRow + 2, self.marioEgoCol + 3), self.trueJumpCounter)

        a = [0, 0, 0, 0, 0, 0]
        a[1] = 1

        danger = self._dangerOfGap()
        if (self.getReceptiveFieldCellValue(self.marioEgoRow + 2, self.marioEgoCol + 3) != 0 or \
            self.getReceptiveFieldCellValue(self.marioEgoRow + 2, self.marioEgoCol + 4) != 0 or danger):
            if (self.mayMarioJump or \
                (not self.isMarioOnGround and a[self.KEY_JUMP] == 1)):
                a[self.KEY_JUMP] = 1
            self.trueJumpCounter += 1
        else:
            a[self.KEY_JUMP] = 0;
            self.trueJumpCounter = 0

        if (self.trueJumpCounter > 16):
            self.trueJumpCounter = 0
            self.action[self.KEY_JUMP] = 0;

        a[self.KEY_SPEED] = danger

        actionStr = ""

        for i in range(6):
            if a[i] == 1:
                actionStr += '1'
            elif a[i] == 0:
                actionStr += '0'
            else:
                print "something very dangerous happen...."

        actionStr += "\r\n"
        #print "action: " , actionStr
        return actionStr

    def getAction(self):
        """ Possible analysis of current observation and sending an action back
        """
    	#print "M: mayJump: %s, onGround: %s, level[11,12]: %d, level[11,13]: %d, jc: %d" % (self.mayMarioJump, self.isMarioOnGround, self.levelScene[11,12], self.levelScene[11,13], self.trueJumpCounter)
    	if (self.isEpisodeOver):
    	    return (1, 1, 1, 1, 1, 1)
               
            #print "LevelScene: \n"
            #print self.levelScene
            
    	danger = self._dangerOfGap()
        #print "entered getAction1"
        if (self.getReceptiveFieldCellValue(self.marioEgoRow, self.marioEgoCol + 2) != 0 or \
            self.getReceptiveFieldCellValue(self.marioEgoRow, self.marioEgoCol + 1) != 0 or danger):
            #print "entered getAction2"
            if (self.mayMarioJump or \
                (not self.isMarioOnGround and self.action[self.KEY_JUMP] == 1)):
                #print "entered getAction3"
                self.action[self.KEY_JUMP] = 1
            #print "entered getAction4"
            self.trueJumpCounter += 1
        else:
            # print "entered getAction5"
            self.action[self.KEY_JUMP] = 0;
            self.trueJumpCounter = 0
        # print "entered getAction6"
        if (self.trueJumpCounter > 16):
            self.trueJumpCounter = 0
            self.action[self.KEY_JUMP] = 0;

        self.action[self.KEY_SPEED] = danger
        return tuple(self.action)

    def getName(self):
        return self.agentName

    def integrateObservation(self, squashedObservation, squashedEnemies, marioPos, enemiesPos, marioState):
        """This method stores the observation inside the agent"""
        #print "Py: got observation::: squashedObservation: \n", squashedObservation
        #print "Py: got observation::: squashedEnemies: \n", squashedEnemies
        #print "Py: got observation::: marioPos: \n", marioPos
        #print "Py: got observation::: enemiesPos: \n", enemiesPos
        #print "Py: got observation::: marioState: \n", marioState
                
        row = self.receptiveFieldHeight
        col = self.receptiveFieldWidth
        levelScene=[]
        enemiesObservation=[]
        
        for i in range(row):
            levelScene.append(squashedObservation[i*col:i*col+col])
	    
        for i in range(row):
            enemiesObservation.append(squashedEnemies[i*col:i*col+col])
        
        self.marioFloats = marioPos
        self.enemiesFloats = enemiesPos
        self.mayMarioJump = marioState[3]
        self.isMarioOnGround = marioState[2]
        self.marioState = marioState[1]
        self.levelScene = levelScene
        #self.printLevelScene()

    def printLevelScene(self):
        ret = ""
        for x in range(self.receptiveFieldWidth):
            tmpData = ""
            for y in range(self.receptiveFieldHeight):
                tmpData += self.mapElToStr(self.getReceptiveFieldCellValue(x, y));
            ret += "\n%s" % tmpData;
        print ret

    def mapElToStr(self, el):
        """maps element of levelScene to str representation"""
        s = "";
        if  (el == 0):
            s = "##"
        s += "#MM#" if (el == 95) else str(el)
        while (len(s) < 4):
            s += "#";
        return s + " "

    def printObs(self):
        """for debug"""
        print repr(self.observation)
