package com.xebisco.yieldscript;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class Interpreter {
    private Program program;

    public Interpreter() {

    }

    public void interpret(InputStream inputStream) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            List<String> removeVariablesValues = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                removeVariablesValues.clear();
                if (line.hashCode() != "".hashCode()) {
                    line = line.replace("\\'", "\50");
                    StringBuilder variableValueToRemoveBuilder = new StringBuilder();
                    boolean onQuote = false;
                    for (char c : line.toCharArray()) {
                        if (c == '\'') {
                            onQuote = !onQuote;
                            if (!onQuote) {
                                variableValueToRemoveBuilder.append(c);
                                removeVariablesValues.add(variableValueToRemoveBuilder.toString());
                                variableValueToRemoveBuilder = new StringBuilder();
                            }
                        }
                        if (onQuote) {
                            variableValueToRemoveBuilder.append(c);
                        }
                    }
                    for (String value : removeVariablesValues) {
                        value = value.replace("\50", "'");
                        int id = program.getVariables().size();
                        program.getVariables().put(id, value.substring(1, value.length() - 1));
                        line = line.replace(value, String.valueOf(id));
                    }
                    String[] sepLine = line.split(" ");
                    StringBuilder outLine = new StringBuilder();
                    for (String p : sepLine) {
                        if (p.hashCode() != "".hashCode()) {
                            outLine.append(p).append(" ");
                        }
                    }
                    if (outLine.length() > 0) {
                        outLine.setLength(outLine.length() - 1);
                    }
                    String[] specialChars = {"=", ","};
                    for (String c : specialChars) {
                        String[] sp = outLine.toString().split(c);
                        StringBuilder ol = new StringBuilder();
                        for (int i = 0; i < sp.length; i++) {
                            String p = sp[i];
                            if (p.hashCode() != "".hashCode()) {
                                ol.append(p);
                                if (i < sp.length - 1) {
                                    if (p.charAt(p.length() - 1) != ' ')
                                        ol.append(" ");
                                    ol.append(c);

                                    if (sp[i + 1].charAt(0) != ' ')
                                        ol.append(" ");
                                }
                            }
                        }
                        outLine = new StringBuilder(ol);
                    }
                    if (outLine.length() > 0) {
                        String l = outLine.toString();
                        lines.add(l);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        execute(lines, 0);
        List<String> lns = program.getAppFunction().get("main");
        if (lns != null)
            execute(lns, 1);
    }

    private void execute(List<String> lines, int funcLayer) {
        lines.removeIf(Objects::isNull);
        String actFunc = "";
        int actFuncLayer = 0;
        program.setFuncLayer(funcLayer);
        ArrayList<String> funcLines = new ArrayList<>(), toRemove = new ArrayList<>();
        for (String line : lines) {
            String[] pcs = line.split(" ");
            if (pcs[0].hashCode() == "func".hashCode() || pcs[0].hashCode() == "if".hashCode()) {
                if (actFuncLayer == 0) {
                    funcLines.clear();
                    actFunc = pcs[1];
                }
                actFuncLayer++;
            }
            if (actFuncLayer > 0) {
                toRemove.add(line);
                funcLines.add(line);
            }
            if (pcs[0].hashCode() == "end".hashCode()) {
                actFuncLayer--;
                if (actFuncLayer == 0) {
                    funcLines.remove(0);
                    //funcLines.remove(funcLines.size() - 1);
                    program.getAppFunction().put(actFunc, funcLines.subList(0, funcLines.size() - 1));
                }
            }
        }
        lines.removeAll(toRemove);
        for (String line : lines) {
            String[] pcs = line.split(" ");
            String[] pcs2 = new String[pcs.length - 1];
            System.arraycopy(pcs, 1, pcs2, 0, pcs.length - 1);
            boolean find = false;
            for (Function f : program.getFunctions()) {
                String name = f.getClass().getSimpleName().toLowerCase();
                if (name.hashCode() == pcs[0].hashCode()) {
                    f.execute(program, pcs2);
                    find = true;
                    break;
                }
            }
            if (!find) {
                List<String> lns = program.getAppFunction().get(pcs[0]);
                if (lns != null)
                    execute(lns, funcLayer + 1);
                else throw new SyntaxException("Cound not find function '" + pcs[0] + "'");
            }
        }
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }
}
