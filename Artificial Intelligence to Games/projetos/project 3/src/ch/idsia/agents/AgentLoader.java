package ch.idsia.agents;

import ch.idsia.agents.controllers.human.HumanKeyboardAgent;
import ch.idsia.tools.punj.PunctualJudge;
import ch.idsia.utils.wox.serial.Easy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: 15.03.11
 * Time: 21:19
 * Package: ch.idsia.agents
 */
public final class AgentLoader
{
private static final AgentLoader _instance = new AgentLoader();

private AgentLoader() {}

public static AgentLoader getInstance()
{
    return _instance;
}

public Agent loadAgent(String name, boolean isPunj)
{
    Agent agent = null;

    try
    {
        if (name.endsWith(".py"))
            agent = new AmiCoAgent(name);
        else
            agent = (Agent) Class.forName(name).newInstance();
    } catch (ClassNotFoundException e)
    {
        System.out.println("[~ Mario AI ~] :" + name + " is not a class name; trying to load a wox definition with that name.");
        try
        {
            agent = (Agent) Easy.load(name);
        } catch (Exception ex)
        {
            System.err.println("[~ Mario AI ~] :" + name + " is not a wox definition");
            agent = null;
        }

        if (agent == null)
        {
            System.err.println("[~ Mario AI ~] : wox definition has not been found as well. Loading <HumanKeyboardAgent> instead");
            agent = new HumanKeyboardAgent();
        }
        System.out.println("[~ Mario AI ~] : agent = " + agent);
    } catch (Exception e)
    {
//            e.printStackTrace ();
        agent = new HumanKeyboardAgent();
        System.err.println("[~ Mario AI ~] : Agent is null. Loading agent with name " + name + " failed.");
        System.out.println("Agent has been set to default: " + agent);
//            System.exit (1);
    }

    if (isPunj)
    {
        try
        {
            PunctualJudge punj = new PunctualJudge();
            String classPath = agent.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            String className = agent.getClass().getName().replace(".", "/") + ".class";

            byte[] byteClass = punj.instrumentClass(classPath + className);

            Class c = punj.buildClass(byteClass, agent.getClass().getName());
            agent = (Agent) c.newInstance();
        } catch (IOException e)
        {
            System.err.println("Unknown error occurred while trying to instrument a class");
            e.printStackTrace();
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
    return agent;
}
}
