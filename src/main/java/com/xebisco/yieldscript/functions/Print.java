package com.xebisco.yieldscript.functions;

import com.xebisco.yieldscript.Function;
import com.xebisco.yieldscript.Program;

public class Print implements Function {
    @Override
    public void execute(Program program, String... args) {
        for(String s : args) {
            System.out.println(program.getVariables().get(program.getVar(s, program.getFuncLayer())));
        }
    }
}
