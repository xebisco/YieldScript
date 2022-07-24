package com.xebisco.yieldscript.test;

import com.xebisco.yieldscript.Interpreter;
import com.xebisco.yieldscript.Program;

public class Main {
    public static void main(String[] args) {
        Program program = new Program();
        Interpreter interpreter = new Interpreter();
        interpreter.setProgram(program);
        interpreter.interpret(Main.class.getResourceAsStream("/test.yld"));
    }
}
