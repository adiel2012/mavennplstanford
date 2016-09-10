/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.interfaces;

import core.Pair;
import java.util.SortedSet;

/**
 *
 * @author LABORATORIO 5
 */
public interface IKeyWordExtractor {
    public SortedSet<Pair> extract(String text);
}
