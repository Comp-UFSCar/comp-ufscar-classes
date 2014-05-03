/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Mario AI nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package ch.idsia.utils;


import ch.idsia.agents.Agent;
import ch.idsia.agents.AgentsPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, firstName_at_idsia_dot_ch
 * Date: May 5, 2009
 * Time: 9:34:33 PM
 * Package: ch.idsia.utils
 */
public class ParameterContainer
{
protected HashMap<String, String> optionsHashMap = new HashMap<String, String>();
private static List<String> allowedOptions = null;
protected static HashMap<String, String> defaultOptionsHashMap = null;
private static final String[] allowed = new String[]{
        "-ag",   // path to agent
//        "-amico",
        "-echo", // echo options
        "-ewf",  // System.exit(0) when evaluation finished
        "-fc",   // frozencreatures
        "-cgr",  //level: Creatures gravity
        "-mgr",  //level: Mario gravity
        "-gv",   // GaveViewer <on|off>
        "-gvc",  // GameViewer continuous updates of the screen <on|off>
        "-gmm",  // level: Green mushroom mode <0|1>. 0(default) -- hurts Mario; 1 -- kills Mario.
        "-i",    // Invulnerability for Mario <on|off>
        "-jp",  // Mario jump power
        "-ld",  // level: difficulty
        "-ll",  // level: length
        "-lla", // level: ladder
        "-ls",  // level: seed
        "-lt",  // level: type
        "-lh",  // level: height [16-20]
        "-lde", // level: dead ends count
        "-lca",  // level: cannons count
        "-lhs", // level: HillStraight count
        "-ltb", // level: Tubes count
        "-lb",  // level: blocks count
        "-lco", // level: coins count
        "-lg",  // level: gaps count
        "-lhb", // level: hidden blocks count
        "-le",  // level: enemies; set up with bit mask
        "-lf",  // level: flat level
        "-mm",  // Mario Mode <0|1|2>
        "-mix", // TODO description
        "-miy", // TODO description
        "-mer", // Mario Ego Row
        "-mec", // Mario Ego Column
        "-fps", // Frames Per Second - update frequency
        "-pr",  // Power resoration (cheat) (bring Mario to FIRE state if "Shoot/RUN" key activated.
        "-punj",// Enable bytecode counting
        "-rfh", // receptive field height (observation )
        "-rfw", // receptive field length (observation )
        "-srf", // show receptive field  (observation )
        "-tc",   // TODO description
        "-tl",   // time limit <int>
        "-trace",// TODO description
        "-vaot", // View always on top <on|off>
        "-vis",  // Visualization <on|off>
        "-vlx",  // View location x coordinate <int>
        "-vly",  // View location y coordinate <int>
        "-vw",   // View width <int>
        "-vh",   // View height <int>
        "-ze",   // Zoom level enemies
        "-zs",   // Zoom level scene
        "-stop", // Start the gamplay/(TODO: description in details for recording) and stop
        "-s",    // path to the file where level will be saved
        "-rec",   // Recording <on|off>
        "-z", //enable Scale2X on startup
        "-w", //wind
        "-ice", //ice
        "-ex", //exitX
        "-ey" //exitY
};

public ParameterContainer()
{
    if (allowedOptions == null)
    {
        allowedOptions = new ArrayList<String>();
        Collections.addAll(allowedOptions, allowed);
    }

    InitDefaults();
}

public void setParameterValue(String param, String value)
{
    try
    {
        if (allowedOptions.contains(param))
        {
            optionsHashMap.put(param, value);
        } else
        {
            throw new IllegalArgumentException("Parameter " + param + " is not valid. Typo?");
        }
    }
    catch (IllegalArgumentException e)
    {

        System.err.println("Error: Undefined parameter '" + param + " " + value + "'");
        System.err.println(e.getMessage());
        System.err.println("Some defaults might be used instead");
    }
}

public String getParameterValue(String param)
{
    String ret;
    ret = optionsHashMap.get(param);
    if (ret != null)
        return ret;

    if (!allowedOptions.contains(param))
    {
        System.err.println("Parameter " + param + " is not valid. Typo?");
        return "";
    }

    ret = defaultOptionsHashMap.get(param);
//        System.err.println("[MarioAI INFO] ~ Default value '" + ret + "' for " + param +
//                " used");
    optionsHashMap.put(param, ret);
    return ret;
//        if (allowedOptions.contains(param))
//        {
//            if (optionsHashMap.get(param) == null)
//            {
//                System.err.println("[MarioAI INFO] ~ Default value '" + defaultOptionsHashMap.get(param) + "' for " + param +
//                        " used");
//                optionsHashMap.put(param, defaultOptionsHashMap.get(param));
//            }
//            return optionsHashMap.get(param);
//        }
//        else
//        {
//            System.err.println("Parameter " + param + " is not valid. Typo?");
//            return "";
//        }
}

public int i(String s)
{
    return Integer.parseInt(s);
}

public float f(String s)
{
    return Float.parseFloat(s);
}

public String s(boolean b)
{
    return b ? "on" : "off";
}

public String s(Object i)
{
    return String.valueOf(i);
}

public String s(Agent a)
{
    try
    {
        if (AgentsPool.getAgentByName(a.getName()) == null)
            AgentsPool.addAgent(a);
        return a.getName();
    } catch (NullPointerException e)
    {
        System.err.println("ERROR: Agent Not Found");
        return "";
    }
}

public Agent a(String s)
{
    return AgentsPool.getAgentByName(s);
}

public boolean b(String s)
{
    if ("on".equals(s))
        return true;
    else if ("off".equals(s))
        return false;
    else
        throw new Error("\n[MarioAI] ~ Wrong parameter value got <" + s +
                "> whereas expected 'on' or 'off'\n[MarioAI] ~ Execution Terminated");
}

public static void InitDefaults()
{
    if (defaultOptionsHashMap == null)
    {
        defaultOptionsHashMap = new HashMap<String, String>();
//            AgentsPool.setCurrentAgent(new HumanKeyboardAgent());
        defaultOptionsHashMap.put("-ag", "ch.idsia.agents.controllers.BehaviorTreeAgent");
//        defaultOptionsHashMap.put("-amico", "off");
        defaultOptionsHashMap.put("-echo", "off"); //defaultOptionsHashMap.put("-echo","off");
        defaultOptionsHashMap.put("-ewf", "on"); //defaultOptionsHashMap.put("-exitWhenFinished","off");
        defaultOptionsHashMap.put("-fc", "off");
        defaultOptionsHashMap.put("-cgr", "1.0"); //Gravity creatures
        defaultOptionsHashMap.put("-mgr", "1.0"); //Gravity Mario
        defaultOptionsHashMap.put("-gv", "off"); //defaultOptionsHashMap.put("-gameViewer","off");
        defaultOptionsHashMap.put("-gvc", "off"); //defaultOptionsHashMap.put("-gameViewerContinuousUpdates","off");
        defaultOptionsHashMap.put("-gmm", "0");
        defaultOptionsHashMap.put("-i", "off"); // Mario Invulnerability
        defaultOptionsHashMap.put("-jp", "7");  // Mario Jump Power
        defaultOptionsHashMap.put("-ld", "0"); //defaultOptionsHashMap.put("-levelDifficulty","0");
        defaultOptionsHashMap.put("-ll", "256"); //defaultOptionsHashMap.put("-levelLength","320");
        defaultOptionsHashMap.put("-lla", "off"); //defaultOptionsHashMap.put("-levelLength","320");
        defaultOptionsHashMap.put("-ls", "0"); //defaultOptionsHashMap.put("-levelRandSeed","1");
        defaultOptionsHashMap.put("-lt", "0"); //defaultOptionsHashMap.put("-levelType","1");
        defaultOptionsHashMap.put("-fps", "24");
        defaultOptionsHashMap.put("-mm", "2"); //Mario Mode
        defaultOptionsHashMap.put("-mix", "32"); //Mario Initial physical Position
        defaultOptionsHashMap.put("-miy", "32"); //Mario Initial physical Position
        defaultOptionsHashMap.put("-pr", "off"); //defaultOptionsHashMap.put("-powerRestoration","off");
        defaultOptionsHashMap.put("-rfh", "19");
        defaultOptionsHashMap.put("-rfw", "19");
        defaultOptionsHashMap.put("-srf", "off");
//            defaultOptionsHashMap.put("-t", "on"); //defaultOptionsHashMap.put("-timer","on");
        defaultOptionsHashMap.put("-tl", "200"); //Time Limit
        defaultOptionsHashMap.put("-tc", "off"); //defaultOptionsHashMap.put("-toolsConfigurator","off");
        defaultOptionsHashMap.put("-trace", "off"); // Trace Mario path through the level, output to std and default file
        defaultOptionsHashMap.put("-vaot", "on"); //defaultOptionsHashMap.put("-viewAlwaysOnTop","off");
        defaultOptionsHashMap.put("-vlx", "0"); //defaultOptionsHashMap.put("-viewLocationX","0");
        defaultOptionsHashMap.put("-vly", "0"); //defaultOptionsHashMap.put("-viewLocationY","0");
        defaultOptionsHashMap.put("-vis", "on"); //defaultOptionsHashMap.put("-visual","on");
        defaultOptionsHashMap.put("-vw", "320"); //defaultOptionsHashMap.put("-visual","on");
        defaultOptionsHashMap.put("-vh", "240"); //defaultOptionsHashMap.put("-visual","on");
        defaultOptionsHashMap.put("-zs", "1");  // ZoomLevel of LevelScene observation
        defaultOptionsHashMap.put("-ze", "0"); //  ZoomLevel of Enemies observation
        defaultOptionsHashMap.put("-lh", "15"); //level height
        defaultOptionsHashMap.put("-lde", "off"); //level: dead ends count
        defaultOptionsHashMap.put("-lca", "on"); //level: cannons count
        defaultOptionsHashMap.put("-lhs", "on"); //level: HillStraight count
        defaultOptionsHashMap.put("-ltb", "on"); //level: tubes count
        defaultOptionsHashMap.put("-lco", "on"); //level: coins count
        defaultOptionsHashMap.put("-lb", "on"); //level: blocks count
        defaultOptionsHashMap.put("-lg", "on"); //level: gaps count
        defaultOptionsHashMap.put("-lhb", "off"); //level: hidden blocks count
        defaultOptionsHashMap.put("-le", ""); //level: creatures
        defaultOptionsHashMap.put("-lf", "off"); //level: flat level
        defaultOptionsHashMap.put("-stop", "off"); //is gameplay stopped
        defaultOptionsHashMap.put("-s", ""); //path to the file where level will be saved
        defaultOptionsHashMap.put("-rec", "off"); //path to the file where recorded game will be saved
        defaultOptionsHashMap.put("-z", "off"); //enable Scale2X on startup
        defaultOptionsHashMap.put("-w", "0"); //wind for Mario
        defaultOptionsHashMap.put("-ice", "0"); //wind for Mario
        defaultOptionsHashMap.put("-mer", "9"); //Mario Ego row
        defaultOptionsHashMap.put("-mec", "9"); //Mario Ego column
        defaultOptionsHashMap.put("-ey", "0"); //exit x
        defaultOptionsHashMap.put("-ex", "0"); //exit y
        defaultOptionsHashMap.put("-punj", "off"); //exit y
    }
}

public static int getTotalNumberOfOptions()
{
    return defaultOptionsHashMap.size();
}

public static int getNumberOfAllowedOptions()
{
    return allowed.length;
}

public static String getDefaultParameterValue(String param)
{
    if (allowedOptions.contains(param))
    {
        assert (defaultOptionsHashMap.get(param) != null);
        return defaultOptionsHashMap.get(param);
    } else
    {
        System.err.println("Requires for Default Parameter " + param + " Failed. Typo?");
        return "";
    }
}
}