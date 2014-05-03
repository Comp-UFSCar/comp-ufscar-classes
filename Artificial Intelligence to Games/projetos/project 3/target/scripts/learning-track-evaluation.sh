#!/bin/sh

cd ../../bin
java -classpath .:../lib/jdom.jar ch.idsia.scenarios.champ.LearningTrack -ag competition.gic2010.learning.sergeykarakovskiy.SergeyKarakovskiy_MLPAgent -rec SergeyKarakovskiy_MLPAgent -vis off -lf on -lg on
java -classpath .:../lib/jdom.jar ch.idsia.scenarios.champ.LearningTrack -ag competition.gic2010.learning.sergeykarakovskiy.SergeyKarakovskiy_MLPAgent -rec SergeyKarakovskiy_MLPAgent -vis off -lco off -lb on -le off -lhb off -lg on -ltb on -lhs off -lca on -lde on -ld 5 -ls 133829
java -classpath .:../lib/jdom.jar ch.idsia.scenarios.champ.LearningTrack -ag competition.gic2010.learning.sergeykarakovskiy.SergeyKarakovskiy_MLPAgent -rec SergeyKarakovskiy_MLPAgent -vis off -lde on -i on -ld 30 -ls 133434
java -classpath .:../lib/jdom.jar ch.idsia.scenarios.champ.LearningTrack -ag competition.gic2010.learning.sergeykarakovskiy.SergeyKarakovskiy_MLPAgent -rec SergeyKarakovskiy_MLPAgent -vis off -lde on -i on -ld 30 -ls 133434 -lhb on
java -classpath .:../lib/jdom.jar ch.idsia.scenarios.champ.LearningTrack -ag competition.gic2010.learning.sergeykarakovskiy.SergeyKarakovskiy_MLPAgent -rec SergeyKarakovskiy_MLPAgent -vis off -lla on -le off -lhs on -lde on -ld 5 -ls 1332656
java -classpath .:../lib/jdom.jar ch.idsia.scenarios.champ.LearningTrack -ag competition.gic2010.learning.sergeykarakovskiy.SergeyKarakovskiy_MLPAgent -rec SergeyKarakovskiy_MLPAgent -vis off -le off -lhs on -lde on -ld 5 -ls 1332656