/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.idsia.agents.controllers.behavior.BasicTypes;

import java.util.HashMap;

/**
 *
 * @author lucasdavid
 */
public class Blackboard {

    HashMap<String, String> map = new HashMap<>();

    public String get(String _field) {
        return map.get(_field);
    }

    public void set(String _field, String _value) {
        map.put(_field, _value);
    }

}
