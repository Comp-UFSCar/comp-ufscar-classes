/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs387_assignment_5;

import generator.GameMap;
import generator.Generator;
import generator.GameMapExporter;

/**
 *
 * @author lucasdavid
 */
public class Cs387_assignment_5 {

    public static void main(String argv[]) {
        
        new GameMapExporter(new Generator().create(24, 33)).export();

    }
}
