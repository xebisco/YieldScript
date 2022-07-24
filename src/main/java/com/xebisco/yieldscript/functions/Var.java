package com.xebisco.yieldscript.functions;

import com.xebisco.yieldscript.Function;
import com.xebisco.yieldscript.Program;
import com.xebisco.yieldscript.SyntaxException;

public class Var implements Function {
    @Override
    public void execute(Program program, String... args) {
        if(args[1].hashCode() != "=".hashCode() || args.length != 3)
            throw new SyntaxException();
        Integer i = null;
        try {
            i = Integer.parseInt(args[2]);
        } catch (NumberFormatException ignore) {
        }
        if(i == null)
            i = program.getVar(args[2], program.getFuncLayer());
        program.getCache().put(args[0] + "\2" + program.getFuncLayer(), i);
    }
}
