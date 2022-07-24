package com.xebisco.yieldscript;

import com.xebisco.yieldscript.functions.Print;
import com.xebisco.yieldscript.functions.Var;

import java.util.*;

public class Program {
    private Random random = new Random();
    private Map<String, Integer> cache = new HashMap<>();
    private Map<Integer, String> variables = new HashMap<>();

    private int funcLayer;

    private Set<Function> functions = new HashSet<>();

    private Map<String, List<String>> appFunction = new HashMap<>();

    public Program() {
        functions.add(new Print());
        functions.add(new Var());
    }

    public Integer getVar(String name, int funcLayer) {
        for (String v : cache.keySet()) {
            String vs = v.split("\2")[1];
            int i = Integer.parseInt(vs);
            if ((i == funcLayer || i == 0) && v.split("\2")[0].hashCode() == name.hashCode()) {
                return cache.get(v);
            }
        }
        try {
            int i = Integer.parseInt(name);
            if (variables.containsKey(i))
                return i;
        } catch (NumberFormatException ignore) {

        }
        return null;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public Map<Integer, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<Integer, String> variables) {
        this.variables = variables;
    }

    public Map<String, Integer> getCache() {
        return cache;
    }

    public void setCache(Map<String, Integer> cache) {
        this.cache = cache;
    }

    public Set<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(Set<Function> functions) {
        this.functions = functions;
    }

    public int getFuncLayer() {
        return funcLayer;
    }

    public void setFuncLayer(int funcLayer) {
        this.funcLayer = funcLayer;
    }

    public Map<String, List<String>> getAppFunction() {
        return appFunction;
    }

    public void setAppFunction(Map<String, List<String>> appFunction) {
        this.appFunction = appFunction;
    }
}
