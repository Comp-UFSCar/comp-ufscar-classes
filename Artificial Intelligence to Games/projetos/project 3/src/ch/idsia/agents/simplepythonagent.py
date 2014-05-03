__author__="Sergey Karakovskiy, sergey at idsia fullstop ch"
__date__ ="$Feb 18, 2009 1:01:12 AM$"

class SimplePyAgent:
#    class MarioAgent(Agent):
    """ example of usage of AmiCo
    """

    def getAction(self, obs):
        ret = (0, 1, 0, 0, 0)
        return ret

    def giveReward(self, reward):
        pass

    def _getName(self):
        if self._name is None:
            self._name = self.__class__.__name__
        return self._name

    def _setName(self, newname):
        """Change name to newname. Uniqueness is not guaranteed anymore."""
        self._name = newname

    _name = None
    name = property(_getName, _setName)

    def __repr__(self):
        """ The default representation of a named object is its name. """
        return "<%s '%s'>" % (self.__class__.__name__, self.name)

    def newEpisode(self):
        pass


